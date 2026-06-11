package dao;

import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * DAO genérico com as operações básicas de persistência (CRUD).
 * Cada DAO específico herda desta classe informando a entidade que gerencia.
 */
public abstract class GenericDAO<T> {

    private final Class<T> classeEntidade;
    protected final EntityManager em;

    protected GenericDAO(Class<T> classeEntidade) {
        this.classeEntidade = classeEntidade;
        this.em = JPAUtil.getEntityManager();
    }

    public T salvar(T entidade) {
        return executarEmTransacao(() -> {
            em.persist(entidade);
            return entidade;
        });
    }

    public T atualizar(T entidade) {
        return executarEmTransacao(() -> em.merge(entidade));
    }

    public void remover(T entidade) {
        executarEmTransacao(() -> {
            em.remove(em.contains(entidade) ? entidade : em.merge(entidade));
            return null;
        });
    }

    public T buscarPorId(Long id) {
        return em.find(classeEntidade, id);
    }

    public List<T> buscarTodos() {
        return em.createQuery("FROM " + classeEntidade.getSimpleName(), classeEntidade)
                .getResultList();
    }

    /** Executa a operação dentro de uma transação, com rollback em caso de erro. */
    protected <R> R executarEmTransacao(Operacao<R> operacao) {
        try {
            em.getTransaction().begin();
            R resultado = operacao.executar();
            em.getTransaction().commit();
            return resultado;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @FunctionalInterface
    protected interface Operacao<R> {
        R executar();
    }
}
