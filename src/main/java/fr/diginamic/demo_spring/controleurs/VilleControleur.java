package fr.diginamic.demo_spring.controleurs;


import fr.diginamic.demo_spring.entites.Departement;
import fr.diginamic.demo_spring.entites.Ville;
import fr.diginamic.demo_spring.entites.VilleDto;
import fr.diginamic.demo_spring.exceptions.ExceptionFonctionnelle;
import fr.diginamic.demo_spring.services.DepartementService;
import fr.diginamic.demo_spring.services.VilleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import java.util.List;


/**
 * Point d'entrée pour toutes les opération (CRUD) sur les villes
 */
@RestController
@RequestMapping("/villes")
public class VilleControleur implements VilleControleurDoc{

    /** Classe de service pour le traitement des cas d'utilisation et des controles métier */
    @Autowired
    private VilleService villeService;

    @Autowired
    private DepartementService departementService;

    @ExceptionHandler({ExceptionFonctionnelle.class})
    public ResponseEntity<String> exceptionHandler(ExceptionFonctionnelle e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @GetMapping("/map")
    public Iterable<Ville> getVillesPagination(@RequestParam int page, @RequestParam int size) {
        PageRequest pagination = PageRequest.of(page, size);
        return villeService.getVillesPagination(pagination);
    }

    @Override
    @GetMapping
    public Iterable<Ville> getVilles() {
        return villeService.getVilles();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<String> getVilleById(@PathVariable Integer id) throws ExceptionFonctionnelle {
        Ville ville = villeService.getVilleById(id);
        return ResponseEntity.ok(ville.toString());
    }

    @GetMapping("/{codeDepartement}/{n}")
    public ResponseEntity<String> getGrandesVilles(@PathVariable int n, @PathVariable String codeDepartement) throws ExceptionFonctionnelle {
        List<Ville> villes = villeService.getGrandesVilles(n, codeDepartement);
        return ResponseEntity.ok(villes.toString());
    }

    @GetMapping("/{codeDepartement}/{popMin}/{popMax}")
    public ResponseEntity<String> getVillesByDepartementPopBornee (@PathVariable String codeDepartement, @PathVariable int popMin, @PathVariable int popMax) throws ExceptionFonctionnelle {
        List<Ville> villes = villeService.getVillesByDepartementPopBornee(codeDepartement, popMin, popMax);
        if (villes.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville n'a une population comprise entre " + popMin + " et " + popMax + " habitants");
        }
        return ResponseEntity.ok(villes.toString());
    }

    /**
     * Permet de créer une nouvelle ville
     * Il faut renseigner le nom et la population dans le body de la requete en format JSON
     * Et le departement dans l'URL sous le format ../create?=departement={codeDepartement}
     * @param ville
     * @param result
     * @return Retourne un message de validation ou d'erreur
     * @throws ExceptionFonctionnelle
     */
    @Operation(summary = "Créé une nouvelle ville avec ses paramètres")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne un message de confirmation de création",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "400",
                    description = "Retourne une erreur en cas de nom de ville déjà existant",
                    content = {@Content()})})
    @PostMapping("/create")
    public ResponseEntity<String> createVille(@Valid @RequestBody VilleDto ville, BindingResult result) throws ExceptionFonctionnelle {
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            return ResponseEntity.badRequest().body(errors.getFirst().getDefaultMessage());
        }

        boolean creationDepartement = villeService.createVille(ville);
        if (creationDepartement) {
            return ResponseEntity.ok("Le département avec le code " +  ville.getCodeDepartement() + " a été ajouté avec succès\n" +
                    "La ville " + ville.getNom() + " a été ajoutée avec succès");
        }

        return ResponseEntity.ok("La ville " + ville.getNom() + " a été ajoutée avec succès");
    }


    /**
     * Permet de modifier une ville
     * On entre le nom et la population dans le body de la requete au format JSON
     * Le département est récupéré dans l'url sous la forme ../update?departement={codeDepartement}
     * @param id
     * @param ville
     * @return Retourne un message de validation ou d'erreur
     * @throws ExceptionFonctionnelle
     */
    @Operation(summary = "Modifie une ville grâce à son identifiant passé dans l'url, et ses paramètres")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne une ville modifiée",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Retourne un message d'erreur de ville non trouvée",
                    content = {@Content()})})
    @PutMapping("/put/{id}")
    public ResponseEntity<String> updateVilleById(@PathVariable Integer id, @RequestBody VilleDto ville) throws ExceptionFonctionnelle {

        villeService.updateVille(id, ville);
        return ResponseEntity.ok("Ville modifiée avec succès");
    }

    /**
     * Supprime une ville grâce à son id
     * L'id est placé dans l'URL sous forme ../delete/{id}
     * @param id
     * @return Retourne un message de validation de suppression, ou d'erreur
     * @throws ExceptionFonctionnelle
     */
    @Operation(summary = "Supprime une ville grâce à son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supprimer une ville",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Retourne une erreur d'identifiant inexistant",
                    content = {@Content()})})
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteVilleParId(@PathVariable Integer id) throws ExceptionFonctionnelle {
        Ville v = villeService.getVilleById(id);
        String nom = v.getNom();
        villeService.deleteVille(v);
        return ResponseEntity.ok("La ville " + nom + " a été supprimée avec succès");
    }

    /**
     * Cherche une liste de villes avec un préfixe passé en paramètre
     * Le paramètre se met dans l'URL sous forme ../recherche/nom/{prefixe}
     * @param prefixe
     * @return Retourne la liste des villes trouvées
     * @throws ExceptionFonctionnelle
     */
    @Operation(summary = "Retourne une liste de villes avec un préfixe passé en paramètre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne une liste de villes non nulle",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Retourne une liste nulle",
                    content = {@Content()})})
    @GetMapping("/recherche/nom/{prefixe}")
    public List<Ville> findVilleByPrefixe(@PathVariable String prefixe) throws ExceptionFonctionnelle {
        return villeService.findVilleByPrefixe(prefixe);
    }

    /**
     * Cherche une liste de villes donc la population est supérieure au nombre passé en paramètre
     * La recherche se fait grâce à l'URL sous forme ../recherche/pop/min/{parametre}
     * @param popMin
     * @return Retourne la liste des villes trouvées
     * @throws ExceptionFonctionnelle
     */
    @GetMapping("/recherche/pop/min/{popMin}")
    public List<Ville> findVilleParPopulationMin(@PathVariable int popMin) throws ExceptionFonctionnelle {
        return villeService.findVillesByPopMin(popMin);
    }

    /**
     * Cherche une liste de villes donc la population est inférieure au nombre passé en paramètre
     * La recherche se fait grâce à l'URL sous forme ../recherche/pop/max/{parametre}
     * @param popMax
     * @return Retourne la liste des villes trouvées
     * @throws ExceptionFonctionnelle
     */
    @GetMapping("/recherche/pop/max/{popMax}")
    public List<Ville> findVilleParPopulationMax(@PathVariable int popMax) throws ExceptionFonctionnelle {
        return villeService.findVillesByPopMax(popMax);
    }

    /**
     * Cherche une liste de villes donc la population est inférieure au popMax passé en paramètre, et supérieure au nombre popMin passé en paramètre
     * La recherche se fait grâce à l'URL sous forme ../recherche/pop/min/{parametre}
     * @param popMin
     * @param popMax
     * @return Retourne la liste des villes trouvées
     * @throws ExceptionFonctionnelle
     */
    @GetMapping("/recherche/pop/{popMin}/{popMax}")
    public List<Ville> findVilleParPopulationBornee(@PathVariable int popMin, @PathVariable int popMax) throws ExceptionFonctionnelle {
        return villeService.findVillesByPopBornee(popMin, popMax);
    }
}


