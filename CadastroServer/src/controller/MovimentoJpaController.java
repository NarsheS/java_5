package controller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import model.Movimento;

public class MovimentoJpaController implements Serializable {
    private final EntityManagerFactory emf;

    public MovimentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Movimento movimento) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(movimento);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void edit(Movimento movimento) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            movimento = em.merge(movimento);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new Exception("Erro ao editar Movimento: " + ex.getMessage());
        } finally {
            em.close();
        }
    }

    public void destroy(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Movimento movimento = em.find(Movimento.class, id);
            if (movimento != null) {
                em.remove(movimento);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Movimento findMovimento(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Movimento.class, id);
        } finally {
            em.close();
        }
    }

    public List<Movimento> findMovimentoEntities() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT m FROM Movimento m");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public int generateId() {
        EntityManager em = getEntityManager();
        try {
            // Obter o maior valor de ID existente
            Integer maxId = (Integer) em.createQuery("SELECT MAX(m.id) FROM Movimento m").getSingleResult();
            return (maxId != null ? maxId + 1 : 1); // Incrementar ou iniciar em 1 se não houver registros
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
