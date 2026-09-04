package fr.diginamic.demo_spring.daos;

import fr.diginamic.demo_spring.entites.Departement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DepartementDao {

    @PersistenceContext
    private EntityManager em;

    public List<Departement> getDepartements() {
        TypedQuery<Departement> query = em.createQuery("SELECT d FROM Departement d", Departement.class);
        return query.getResultList();
    }

    public Departement getDepartementByCode(String code) {
        TypedQuery<Departement> query = em.createQuery("SELECT d FROM Departement d WHERE d.code = :code", Departement.class);
        query.setParameter("code", code);
        try {
            return query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public Departement getDepartementById(int id) {
        TypedQuery<Departement> query = em.createQuery("SELECT d FROM Departement d WHERE d.id = :id", Departement.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public void creerDepartement(Departement departement) {
        em.persist(departement);
    }

}
