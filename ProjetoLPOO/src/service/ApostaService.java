package service;

import dao.ApostaDAO;
import dao.ParticipanteDAO;
import entities.Aposta;
import entities.Participante;
import entities.Partida;

public class ApostaService {

    private final ApostaDAO apostaDAO = new ApostaDAO();
    private final ParticipanteDAO participanteDAO = new ParticipanteDAO();

    public Aposta apostar(Participante participante, Partida partida, int golsA, int golsB) {
        if (participante == null) {
            throw new RegraDeNegocioException("Selecione o participante que está apostando.");
        }
        if (partida == null) {
            throw new RegraDeNegocioException("Selecione uma partida.");
        }
        if (golsA < 0 || golsB < 0) {
            throw new RegraDeNegocioException("O número de gols não pode ser negativo.");
        }
        if (!partida.podeApostar()) {
            throw new RegraDeNegocioException(
                    "Apostas são permitidas apenas até 20 minutos antes do início da partida.");
        }
        if (apostaDAO.jaApostou(participante, partida)) {
            throw new RegraDeNegocioException(
                    participante.getNome() + " já apostou nessa partida.");
        }

        Aposta aposta = new Aposta(partida, golsA, golsB);
        participante.apostar(aposta);

        participanteDAO.atualizar(participante);
        return aposta;
    }
}
