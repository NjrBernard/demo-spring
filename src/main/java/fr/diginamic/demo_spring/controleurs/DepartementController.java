package fr.diginamic.demo_spring.controleurs;

import fr.diginamic.demo_spring.entites.Departement;
import fr.diginamic.demo_spring.exceptions.ExceptionFonctionnelle;
import fr.diginamic.demo_spring.services.DepartementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departements")
public class DepartementController {

    @Autowired
    private DepartementService departementService;

    @ExceptionHandler({ExceptionFonctionnelle.class})
    public ResponseEntity<String> exceptionHandler(ExceptionFonctionnelle e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * Cherche une liste de départements
     * @return Retourne la liste des départements trouvés
     */
    @GetMapping
    public Iterable<Departement> getDepartements() {
        return departementService.getDepartements();
    }


    /**
     * Permet de créer une liste de départements
     * La création de départements se fait sous forme de liste, il faut donc penser à encadrer le JSON dans le body de la requete par []
     * @param departements
     * @param result
     * @return Retourne un message de validation de création, ou d'erreur
     * @throws ExceptionFonctionnelle
     */
    @Operation(summary = "Crée un département avec ses paramètres")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne un message de confirmation de création",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "400",
                    description = "Retourne une erreur en cas de nom de département déjà existant",
                    content = {@Content()})})
    @PostMapping("/create")
    public ResponseEntity<String> createDepartement(@Valid @RequestBody List<Departement> departements, BindingResult result) throws ExceptionFonctionnelle {
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            return ResponseEntity.badRequest().body(errors.toString());
        }

        for(Departement d: departements) {
            departementService.creerDepartement(d);
        }

        return ResponseEntity.ok("Le(s) département(s) a/ont bien été créé(s)");
    }
}
