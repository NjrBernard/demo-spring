package fr.diginamic.demo_spring.services;

import fr.diginamic.demo_spring.entites.Ville;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class VilleService {

    private List<Ville> villes = new ArrayList<>();

    public VilleService() {
        Ville poussan = new Ville("Poussan", "7000");
        villes.add(poussan);
        Ville balaruc = new Ville("Balaruc", "9000");
        villes.add(balaruc);
    }
    public List<Ville> listeVilles() {


        return villes;
    }

}
