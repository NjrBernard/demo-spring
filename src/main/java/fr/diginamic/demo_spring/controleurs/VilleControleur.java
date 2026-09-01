package fr.diginamic.demo_spring.controleurs;


import fr.diginamic.demo_spring.entites.Ville;
import fr.diginamic.demo_spring.services.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleControleur {

    @Autowired
    private VilleService villeService;

    @GetMapping
    public List listeVilles() {
        return villeService.listeVilles();

    }

    @GetMapping("/{id}")
    public ResponseEntity<String> trouverVilleParId(@PathVariable int id) {
        List<Ville> villes = villeService.listeVilles();
        for (Ville ville : villes) {
            if (ville.getId()==id) {
                return ResponseEntity.ok(ville.toString());
            }
        }
        return ResponseEntity.badRequest().body("Ville pas trouvée");
    }

    @PostMapping("/create")
    public ResponseEntity<String> ajouterVille(@RequestBody Ville ville) {
        List<Ville> villes = villeService.listeVilles();
        for  (Ville v : villes) {
            if (v.getNom().equals(ville.getNom())) {
                return ResponseEntity.badRequest().body("La ville existe déjà");
            }
        }
        villes.add(ville);
        return ResponseEntity.ok("La ville " + ville.getNom() + " a été ajoutée avec succès");
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<String> modifierVilleParId(@RequestBody Ville ville, @PathVariable int id) {
        List<Ville> villes = villeService.listeVilles();
        for (Ville v : villes) {
            if (v.getId() == id) {
                v.setNom(ville.getNom());
                v.setPopulation(ville.getPopulation());
                return ResponseEntity.ok(v.toString());
            }
        }
        return ResponseEntity.badRequest().body("Ville non trouvée");
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> supprimerVilleParId(@PathVariable int id) {
        List<Ville> villes = villeService.listeVilles();
        for (Ville v : villes) {
            if (v.getId() == id) {
                String nom = v.getNom();
                villes.remove(v);
                return ResponseEntity.ok("La ville " + nom + " a bien été supprimée");
            }
        }
        return ResponseEntity.badRequest().body("La ville n'a pas été trouvée");
    }


}


