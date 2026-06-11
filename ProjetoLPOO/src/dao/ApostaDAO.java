package dao;

import entities.Aposta;
import entities.Participante;
import entities.Partida;

public class ApostaDAO extends GenericDAO<Aposta> {

    public ApostaDAO() {
        super(Aposta.class);
    }

    public boolean jaApostou(Participante participante, Partida partida) {
        Long total = em.createQuery(
                        "SELECT COUNT(a) FROM Aposta a " +
                        "WHERE a.participante = :participante AND a.partida = :partida",
                        Long.class)
                .setParameter("participante", participante)
                .setParameter("partida", partida)
                .getSingleResult();
        return total > 0;
    }
}
