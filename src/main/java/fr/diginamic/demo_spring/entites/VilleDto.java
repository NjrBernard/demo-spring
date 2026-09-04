package fr.diginamic.demo_spring.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
public class VilleDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 2, message = "Le nom de la ville doit contenir au minimum 2 lettres")
    private String nom;

    @Min(value = 1, message = "La population doit être un entier positif" )
    private int population;

    @Size(min = 2, max = 3)
    private String codeDepartement;

    private Integer idDepartement;

    public VilleDto() {}

    public VilleDto(String nom, int population) {
        this.nom = nom;
        this.population = population;
    }

    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public Integer getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(Integer idDepartement) {
        this.idDepartement = idDepartement;
    }

    @Override
    public String toString() {
        return "Ville{" +
                "nom='" + nom + '\'' +
                ", population='" + population + '\'' +
                '}';
    }
}
