package dao;

import entities.Campeonato;

import java.util.Optional;

public class CampeonatoDAO extends GenericDAO<Campeonato> {

    public CampeonatoDAO() {
        super(Campeonato.class);
    }

    public Optional<Campeonato> buscarPorNome(String nome) {
        return em.createQuery("SELECT c FROM Campeonato c WHERE LOWER(c.nome) = LOWER(:nome)", Campeonato.class)
                .setParameter("nome", nome)
                .getResultStream()
                .findFirst();
    }
}
