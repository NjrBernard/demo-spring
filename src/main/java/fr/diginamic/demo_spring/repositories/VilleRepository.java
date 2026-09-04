package fr.diginamic.demo_spring.repositories;

import fr.diginamic.demo_spring.entites.Ville;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface VilleRepository extends CrudRepository<Ville, Integer> {

    public Page<Ville> findAll(Pageable pageable);

    public List<Ville> getVillesByNomStartingWithIgnoreCase(String prefixe);

    public List<Ville> getVillesByPopulationGreaterThanOrderByPopulationDesc(int min);

    public List<Ville> getVillesByPopulationLessThanOrderByPopulationDesc(int max);

    public List<Ville> getVillesByPopulationBetweenOrderByPopulationDesc(int min, int max);

    public List<Ville> getVillesByDepartementCodeAndPopulationGreaterThanOrderByPopulationDesc(String code, int min);

    public List<Ville> getVillesByDepartementCodeAndPopulationBetweenOrderByPopulationDesc(String code, int min, int max);

    public List<Ville> getVillesByDepartementCodeOrderByPopulationDesc(String code, Pageable pageable);
}
