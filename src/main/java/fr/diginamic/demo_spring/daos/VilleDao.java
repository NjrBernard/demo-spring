package fr.diginamic.demo_spring.daos;

import fr.diginamic.demo_spring.entites.Departement;
import fr.diginamic.demo_spring.entites.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Classe d'accès à la liste des villes
 */
@Repository
public class VilleDao {

    @PersistenceContext
    private EntityManager em;

    public List<Ville> getVilles() {
        TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v JOIN FETCH v.departement", Ville.class);
        return query.getResultList();
    }

    public Ville getVilleById(int id) {
        return em.find(Ville.class, id);
    }

    public List<Ville> getGrandesVilles(int n, Departement departement) {
        TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v WHERE v.departement = :departement ORDER BY v.population DESC", Ville.class);
        query.setParameter("departement", departement);
        query.setMaxResults(n);
        return query.getResultList();
    }

    public List<Ville> getVillesByDepartement(Departement departement) {
        TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v WHERE v.departement = :departement", Ville.class);
        query.setParameter("departement", departement);
        return query.getResultList();
    }

    public void createVille(Ville ville) {
        em.persist(ville);
    }

    public void updateVille(Ville ville) {
        em.merge(ville);
    }

    public void deleteVille(Ville ville) {
        em.remove(ville);
    }

    public List<Ville> findVilleByPrefixe(String prefixe) {
        TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v WHERE lower(v.nom) LIKE :prefixe ", Ville.class);
        query.setParameter("prefixe", prefixe + "%");
        return query.getResultList();
    }

    public List<Ville> findVillesByPopMin(int popMin) {
        TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v WHERE v.population >= :popMin", Ville.class);
        query.setParameter("popMin", popMin);
        return query.getResultList();
    }

    public List<Ville> findVillesByPopMax(int popMax) {
        TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v WHERE v.population <= :popMax", Ville.class);
        query.setParameter("popMax", popMax);
        return query.getResultList();
    }

    public List<Ville> findVillesByPopBornee(int popMin, int popMax) {
        TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v WHERE v.population <= :popMax AND v.population >= :popMin" , Ville.class);
        query.setParameter("popMax", popMax);
        query.setParameter("popMin", popMin);
        return query.getResultList();
    }

    public List<Ville> findVillesByDepartementPopBornee(Departement departement, int popMin, int popMax) {
        TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v WHERE v.departement = :departement AND v.population BETWEEN :popMin AND :popMax", Ville.class);
        query.setParameter("departement", departement);
        query.setParameter("popMin", popMin);
        query.setParameter("popMax", popMax);
        return query.getResultList();
    }
}
