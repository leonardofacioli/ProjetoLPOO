package service;

import dao.GrupoDAO;
import dao.ParticipanteDAO;
import entities.Grupo;
import entities.Participante;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GrupoService {

    public static final int MAX_GRUPOS = 5;
    public static final int MAX_PARTICIPANTES = 5;

    private final GrupoDAO grupoDAO = new GrupoDAO();
    private final ParticipanteDAO participanteDAO = new ParticipanteDAO();

    public Grupo criar(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraDeNegocioException("O nome do grupo é obrigatório.");
        }
        String nomeLimpo = nome.trim();
        if (grupoDAO.buscarPorNome(nomeLimpo).isPresent()) {
            throw new RegraDeNegocioException("Já existe um grupo chamado \"" + nomeLimpo + "\".");
        }
        if (grupoDAO.contar() >= MAX_GRUPOS) {
            throw new RegraDeNegocioException("Limite de " + MAX_GRUPOS + " grupos atingido.");
        }
        return grupoDAO.salvar(new Grupo(nomeLimpo));
    }


    public Participante adicionarParticipante(Grupo grupo, String nome) {
        if (grupo == null) {
            throw new RegraDeNegocioException("Selecione um grupo.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraDeNegocioException("O nome do participante é obrigatório.");
        }
        String nomeLimpo = nome.trim();
        if (grupo.getParticipantes().size() >= MAX_PARTICIPANTES) {
            throw new RegraDeNegocioException(
                    "O grupo já atingiu o limite de " + MAX_PARTICIPANTES + " participantes.");
        }
        boolean nomeRepetido = grupo.getParticipantes().stream()
                .anyMatch(p -> p.getNome().equalsIgnoreCase(nomeLimpo));
        if (nomeRepetido) {
            throw new RegraDeNegocioException(
                    "Já existe um participante chamado \"" + nomeLimpo + "\" nesse grupo.");
        }

        Participante participante = new Participante(nomeLimpo);
        participanteDAO.salvar(participante);
        grupo.adicionarParticipante(participante);
        grupoDAO.atualizar(grupo);
        return participante;
    }

    public List<Participante> classificacao(Grupo grupo) {
        List<Participante> ranking = new ArrayList<>(grupo.getParticipantes());
        ranking.sort(Comparator.comparingInt(Participante::calcularPontuacao).reversed());
        return ranking;
    }

    public List<Grupo> listarTodos() {
        return grupoDAO.buscarTodos();
    }
}
