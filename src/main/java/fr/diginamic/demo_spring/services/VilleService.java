package fr.diginamic.demo_spring.services;

import fr.diginamic.demo_spring.entites.Departement;
import fr.diginamic.demo_spring.entites.Ville;
import fr.diginamic.demo_spring.entites.VilleDto;
import fr.diginamic.demo_spring.exceptions.ExceptionFonctionnelle;
import fr.diginamic.demo_spring.daos.DepartementDao;
import fr.diginamic.demo_spring.daos.VilleDao;
import fr.diginamic.demo_spring.repositories.VilleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;


@Service
public class VilleService {

    @Autowired
    private VilleDao villeDao;
    @Autowired
    private DepartementDao departementDao;
    @Autowired
    private VilleRepository villeRepository;

    public Iterable<Ville> getVilles() {
        return villeRepository.findAll();
    }

    public Iterable<Ville> getVillesPagination(Pageable pageable) {
        return villeRepository.findAll(pageable);
    }

    public Ville getVilleById(Integer id) throws ExceptionFonctionnelle {
        Optional<Ville> optionalV = villeRepository.findById(id);
        if (optionalV.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville ne comporte l'ID " + id);
        }
        Ville v = optionalV.get();
        return v;
    }

    public List<Ville> getGrandesVilles(int n, String codeDepartement) throws ExceptionFonctionnelle {
        if (n < 1) {
            throw new ExceptionFonctionnelle("Le nombre de villes à afficher doit être un entier supérieur à 0");
        }
        Departement d = departementDao.getDepartementByCode(codeDepartement);
        if (d == null) {
            throw new ExceptionFonctionnelle("Ce département n'existe pas");
        }
        List<Ville> v = villeRepository.getVillesByDepartementCodeAndPopulationGreaterThanOrderByPopulationDesc(d.getCode(), n);
        if (v.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville trouvée dans ce département");
        }
        return v;
    }

    public List<Ville> getVillesByDepartementPopBornee(String codeDepartement, int popMin, int popMax) throws ExceptionFonctionnelle {
        Departement departement = departementDao.getDepartementByCode(codeDepartement);
        if (departement == null) {
            throw new ExceptionFonctionnelle("Ce département n'existe pas");
        }
        if (popMin > popMax) {
            throw new ExceptionFonctionnelle("La population minimale doit être inférieure à la population maximale");
        }
        if (popMin < 0) {
            throw new ExceptionFonctionnelle("La recherche de population doit utiliser des entiers positifs");
        }

        return villeRepository.getVillesByDepartementCodeAndPopulationBetweenOrderByPopulationDesc(codeDepartement, popMin, popMax);
    }

    @Transactional
    public boolean createVille(VilleDto ville) throws ExceptionFonctionnelle {
        List<Ville> villes = villeDao.getVilles();
        if (ville.getCodeDepartement() == null && ville.getIdDepartement() == null) {
            throw new ExceptionFonctionnelle("Vous devez renseigné soit le code département, soit l'ID");
        }
        if (ville.getCodeDepartement() != null && ville.getIdDepartement() != null) {
            throw new ExceptionFonctionnelle("Vous devez renseigné soit le code département, soit l'ID, mais pas les 2");
        }
        for (Ville v : villes) {
            if (v.getNom().equals(ville.getNom())) {
                throw new ExceptionFonctionnelle("Ville déjà existante");
            }
        }
        Departement departement;
        boolean creationDepartement = false;

        if (ville.getCodeDepartement() != null) {
            departement = departementDao.getDepartementByCode(ville.getCodeDepartement());
            if (departement == null) {
                departement = new Departement(ville.getCodeDepartement());
                departementDao.creerDepartement(departement);
                creationDepartement = true;
            }
        }

        else {
            departement = departementDao.getDepartementById(ville.getIdDepartement());
            if (departement == null) {
                    throw new ExceptionFonctionnelle("");
                }
            }

        Ville v = new Ville(ville.getNom(), ville.getPopulation());
        v.setDepartement(departement);

        villeRepository.save(v);
        return creationDepartement;
    }

    @Transactional
    public void updateVille(Integer id, VilleDto ville) throws ExceptionFonctionnelle {

        Optional<Ville> optionalV = villeRepository.findById(id);
        if (optionalV.isEmpty()) {
            throw new ExceptionFonctionnelle("La ville avec l'ID " + id + " n'existe pas");
        }

        Ville v = optionalV.get();
        v.setNom(ville.getNom());
        v.setPopulation(ville.getPopulation());

        if (ville.getIdDepartement() != null) {
            v.setDepartement(departementDao.getDepartementById(ville.getIdDepartement()));
        }
        else {
            v.setDepartement(departementDao.getDepartementByCode(ville.getCodeDepartement()));
        }
        villeRepository.save(v);
    }

    @Transactional
    public void deleteVille(Ville ville) throws ExceptionFonctionnelle {
        Optional<Ville> optionalV =  villeRepository.findById(ville.getId());
        if (optionalV.isEmpty()) {
            throw new ExceptionFonctionnelle("La ville avec l'ID " + ville.getId() + " n'existe pas");
        }
        Ville v = optionalV.get();
        villeRepository.delete(v);
    }

    public List<Ville> findVilleByPrefixe(String prefixe) throws ExceptionFonctionnelle {
        List<Ville> villes = villeRepository.getVillesByNomStartingWithIgnoreCase(prefixe);
        if (villes.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville ne commence par " + prefixe);
        }
        if (prefixe == null) {
            throw new ExceptionFonctionnelle("Le prefixe doit contenir au moins un lettre");
        }
        return villes;
    }

    public List<Ville> findVillesByPopMin(int popMin) throws ExceptionFonctionnelle {
        List<Ville> villes = villeRepository.getVillesByPopulationGreaterThanOrderByPopulationDesc(popMin);
        if (villes.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville contient moins de " + popMin + "habitants");
        }
        return villes;
    }

    public List<Ville> findVillesByPopMax(int popMax) throws ExceptionFonctionnelle {
        List<Ville> villes = villeRepository.getVillesByPopulationLessThanOrderByPopulationDesc(popMax);
        if (villes.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville contient plus de " + popMax + "habitants");
        }
        return villes;
    }

    public List<Ville> findVillesByPopBornee(int popMin, int popMax) throws ExceptionFonctionnelle {
        List<Ville> villes = villeRepository.getVillesByPopulationBetweenOrderByPopulationDesc(popMin, popMax);
        if (popMin > popMax) {
            throw new ExceptionFonctionnelle("La population minimale doit être supérieur à la population maximal");
        }
        if (villes.isEmpty()) {
            throw new ExceptionFonctionnelle("Aucune ville contient des habitants entre " + popMin + " et " + popMax);
        }
        return villes;
    }


}
