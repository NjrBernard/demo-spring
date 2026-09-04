package fr.diginamic.demo_spring.controleurs;

import fr.diginamic.demo_spring.entites.Ville;
import fr.diginamic.demo_spring.exceptions.ExceptionFonctionnelle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.awt.print.Pageable;
import java.util.List;

public interface VilleControleurDoc {

    /**
     * Cherche une liste de villes
     * @return Retourne toutes les villes trouvées
     */
    Iterable<Ville> getVilles();

    /**
     * Cherche une ville par son ID
     * @param id
     * @return Retourne une ville au format JSON
     * @throws ExceptionFonctionnelle
     */
    ResponseEntity<String> getVilleById(Integer id) throws ExceptionFonctionnelle;
}
