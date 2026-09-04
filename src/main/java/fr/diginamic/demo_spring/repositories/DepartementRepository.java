package fr.diginamic.demo_spring.repositories;

import fr.diginamic.demo_spring.entites.Departement;
import org.springframework.data.repository.CrudRepository;

public interface DepartementRepository extends CrudRepository<Departement,Integer> {

    public Departement getDepartementByCode(String code);

}
