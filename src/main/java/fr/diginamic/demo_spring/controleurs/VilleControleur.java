package fr.diginamic.demo_spring.controleurs;


import fr.diginamic.demo_spring.entites.Ville;
import fr.diginamic.demo_spring.exceptions.ExceptionFonctionnelle;
import fr.diginamic.demo_spring.services.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @ExceptionHandler({ExceptionFonctionnelle.class})
    public ResponseEntity<String> exceptionHandler(ExceptionFonctionnelle e) {
        return ResponseEntity.badRequest().body(e.getMessage());
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
    public ResponseEntity<String> ajouterVille(@RequestBody Ville ville) throws ExceptionFonctionnelle {
        List<Ville> villes = villeService.listeVilles();
        for  (Ville v : villes) {
            if (v.getNom().equals(ville.getNom())) {
                return ResponseEntity.badRequest().body("La ville existe déjà");
            }
        }
        if (ville.getPopulation() < 10) {
            throw new ExceptionFonctionnelle("La ville doit avoir au moins 10 habitants");
        }
        if (ville.getNom().length() < 2) {
            throw new ExceptionFonctionnelle("Le nom de la ville doit contenir au moins 2 caractères");
        }
        villes.add(ville);
        return ResponseEntity.ok("La ville " + ville.getNom() + " a été ajoutée avec succès");
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<String> modifierVilleParId(@RequestBody Ville ville, @PathVariable int id) throws ExceptionFonctionnelle {
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
    public ResponseEntity<String> supprimerVilleParId(@PathVariable int id) throws ExceptionFonctionnelle {
        List<Ville> villes = villeService.listeVilles();
        for (Ville v : villes) {
            if (v.getId() == id) {
                String nom = v.getNom();
                villes.remove(v);
                return ResponseEntity.ok("La ville " + nom + " a bien été supprimée");
            }
        }
        throw new ExceptionFonctionnelle("La ville n'a pas été trouvée");
    }

    @GetMapping("/recherche/nom/{nom}")
    public List<Ville> trouverVilleParNom(@PathVariable String nom) throws ExceptionFonctionnelle {
        List<Ville> villes = villeService.listeVilles();
        List<Ville> villesAvecPrefixe = new ArrayList<>();
        for (Ville v : villes) {
            if (v.getNom().startsWith(nom)) {
                villesAvecPrefixe.add(v);
            }
        }
        if (villesAvecPrefixe.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville dont le nom commence par " + nom + " n'a été trouvée");
        }
        return villesAvecPrefixe;
    }

    @GetMapping("/recherche/pop/{popMin}")
    public List<Ville> trouverVilleParPopulationMin(@PathVariable int popMin) throws ExceptionFonctionnelle {
        List<Ville> villes = villeService.listeVilles();
        List<Ville> villesPeuplees = new ArrayList<>();
        for (Ville v : villes) {
            if (v.getPopulation() >= popMin) {
                villesPeuplees.add(v);
            }
        }
        if (villesPeuplees.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville n'a une population supérieur à " + popMin);
        }
        return villesPeuplees;
    }

    @GetMapping("/recherche/pop/{popMin}/{popMax}")
    public List<Ville> trouverVilleParPopulationBornee(@PathVariable int popMin, @PathVariable int popMax) throws ExceptionFonctionnelle {
        if (popMin > popMax) {
            throw new ExceptionFonctionnelle("La population minimale doit être inférieure à la population maximal");
        }
        List<Ville> villes = villeService.listeVilles();
        List<Ville> villesPeuplees = new ArrayList<>();
        for (Ville v : villes) {
            if (v.getPopulation() >= popMin && v.getPopulation() <= popMax) {
                villesPeuplees.add(v);
            }
        }
        if (villesPeuplees.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville n'a une population supérieur à " + popMin + " et inférieure à " + popMax);
        }
        return villesPeuplees;
    }
}


