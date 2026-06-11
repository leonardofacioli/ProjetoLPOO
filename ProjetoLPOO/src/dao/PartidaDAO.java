package dao;

import entities.Partida;

import java.util.List;

public class PartidaDAO extends GenericDAO<Partida> {

    public PartidaDAO() {
        super(Partida.class);
    }

    /** Partidas que ainda não tiveram o resultado registrado. */
    public List<Partida> buscarPendentes() {
        return em.createQuery(
                        "SELECT p FROM Partida p WHERE p.golsA IS NULL ORDER BY p.dataHora",
                        Partida.class)
                .getResultList();
    }
}
