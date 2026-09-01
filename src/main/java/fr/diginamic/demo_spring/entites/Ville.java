package fr.diginamic.demo_spring.entites;

import java.util.Objects;

public class Ville {
    private int id;
    private String nom;
    private String population;

    private static int compteur = 0;

    public Ville(String nom, String population) {
        this.id = compteur++;
        this.nom = nom;
        this.population = population;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return "Ville{" +
                "nom='" + nom + '\'' +
                ", population='" + population + '\'' +
                '}';
    }
}
