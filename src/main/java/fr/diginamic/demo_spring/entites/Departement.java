package fr.diginamic.demo_spring.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Null
    @Size(min = 2, max = 50)
    private String nom;

    @NotNull
    @Size(min = 2, max = 3)
    private String code;


    @OneToMany(mappedBy = "departement")
    @JsonIgnore
    private List<Ville> villes = new ArrayList<Ville>();

    public Departement() {}

    public Departement(String code) {
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Ville> getVilles() {
        return villes;
    }

    public void setVilles(List<Ville> villes) {
        this.villes = villes;
    }
}
