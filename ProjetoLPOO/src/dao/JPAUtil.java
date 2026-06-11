package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Responsável por criar e fornecer o EntityManager da aplicação.
 * Em aplicação desktop (Swing) usamos um único EntityManager compartilhado,
 * o que mantém as entidades gerenciadas durante toda a sessão e evita
 * problemas de lazy loading na interface.
 */
public final class JPAUtil {

    private static final EntityManagerFactory FACTORY =
            Persistence.createEntityManagerFactory("ProjetoLPOO-PU");

    private static EntityManager entityManager;

    private JPAUtil() {
    }

    public static synchronized EntityManager getEntityManager() {
        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = FACTORY.createEntityManager();
        }
        return entityManager;
    }

    public static synchronized void fechar() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        if (FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}
