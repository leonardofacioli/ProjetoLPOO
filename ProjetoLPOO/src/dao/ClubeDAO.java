package dao;

import entities.Clube;

import java.util.Optional;

public class ClubeDAO extends GenericDAO<Clube> {

    public ClubeDAO() {
        super(Clube.class);
    }

    public Optional<Clube> buscarPorNome(String nome) {
        return em.createQuery("SELECT c FROM Clube c WHERE LOWER(c.nome) = LOWER(:nome)", Clube.class)
                .setParameter("nome", nome)
                .getResultStream()
                .findFirst();
    }

    public long contar() {
        return em.createQuery("SELECT COUNT(c) FROM Clube c", Long.class).getSingleResult();
    }
}
