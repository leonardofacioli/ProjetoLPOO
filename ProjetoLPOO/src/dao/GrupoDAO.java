package dao;

import entities.Grupo;

import java.util.Optional;

public class GrupoDAO extends GenericDAO<Grupo> {

    public GrupoDAO() {
        super(Grupo.class);
    }

    public Optional<Grupo> buscarPorNome(String nome) {
        return em.createQuery("SELECT g FROM Grupo g WHERE LOWER(g.nome) = LOWER(:nome)", Grupo.class)
                .setParameter("nome", nome)
                .getResultStream()
                .findFirst();
    }

    public long contar() {
        return em.createQuery("SELECT COUNT(g) FROM Grupo g", Long.class).getSingleResult();
    }
}
