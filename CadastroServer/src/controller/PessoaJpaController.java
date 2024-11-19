package controller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import model.Pessoa;

public class PessoaJpaController implements Serializable {
    private EntityManagerFactory emf;

    public PessoaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pessoa pessoa) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pessoa);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void edit(Pessoa pessoa) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pessoa);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new Exception("Erro ao atualizar a pessoa: " + pessoa.getPessoaId(), e);
        } finally {
            em.close();
        }
    }

    public void destroy(int id) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Pessoa pessoa = em.find(Pessoa.class, id);
            if (pessoa == null) {
                throw new Exception("Pessoa com ID " + id + " não encontrada.");
            }
            em.remove(pessoa);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Pessoa findPessoa(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pessoa.class, id);
        } finally {
            em.close();
        }
    }

    public List<Pessoa> findPessoaEntities() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT p FROM Pessoa p");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
