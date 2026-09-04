package fr.diginamic.demo_spring.services;

import fr.diginamic.demo_spring.entites.Departement;
import fr.diginamic.demo_spring.exceptions.ExceptionFonctionnelle;
import fr.diginamic.demo_spring.daos.DepartementDao;
import fr.diginamic.demo_spring.repositories.DepartementRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartementService {

    @Autowired
    private DepartementDao departementDao;

    @Autowired
    private DepartementRepository departementRepository;

    public Iterable<Departement> getDepartements(){
        return departementRepository.findAll();
    }

    public Departement getDepartementByCode(String code) throws ExceptionFonctionnelle {
        Departement departement = departementRepository.getDepartementByCode(code);
        if (departement == null){
            throw new ExceptionFonctionnelle("Aucun département n'a le code " +  code);
        }
        return departement;
    }

    public Departement getDepartementById(Integer id) throws ExceptionFonctionnelle {
        Optional<Departement> departement = departementRepository.findById(id);
        if (departement.isEmpty()){
            throw new ExceptionFonctionnelle("Aucun département n'a l'ID' " +  id);
        }
        Departement d = departement.get();
        return d;
    }

    @Transactional
    public void creerDepartement(Departement departement) throws ExceptionFonctionnelle {
        Iterable<Departement> departements = departementRepository.findAll();
        for (Departement d : departements) {
            if (departement.getNom().equals(d.getNom())) {
                throw new ExceptionFonctionnelle("Ce département existe déjà");
            }
        }
        departementRepository.save(departement);
    }
}
