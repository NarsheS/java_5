package controller;

import model.Produtos;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

public class ProdutosJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public ProdutosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Método para listar todos os produtos
    public List<Produtos> findProdutosEntities() {
        return findProdutosEntities(true, -1, -1);
    }

    public List<Produtos> findProdutosEntities(int maxResults, int firstResult) {
        return findProdutosEntities(false, maxResults, firstResult);
    }

    private List<Produtos> findProdutosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Produtos.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    // Método para encontrar um produto pelo ID
    public Produtos findProduto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produtos.class, id);
        } finally {
            em.close();
        }
    }

    // Método para criar um novo produto
    public void create(Produtos produto) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(produto);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    // Método para editar um produto existente
    public void edit(Produtos produto) throws Exception {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(produto);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    // Método para remover um produto pelo ID
    public void destroy(Integer id) throws Exception {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Produtos produto;
            try {
                produto = em.getReference(Produtos.class, id);
                produto.getProdutoId();
            } catch (Exception ex) {
                throw new Exception("O produto com ID " + id + " não existe.", ex);
            }
            em.remove(produto);
            tx.commit();
        } finally {
            em.close();
        }
    }

    // Método para contar o número total de produtos
    public int getProdutosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(em.getCriteriaBuilder().count(cq.from(Produtos.class)));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
