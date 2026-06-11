package service;

import dao.CampeonatoDAO;
import dao.PartidaDAO;
import entities.Administrador;
import entities.Campeonato;
import entities.Clube;
import entities.Partida;

import java.time.LocalDateTime;
import java.util.List;

public class PartidaService {

    private final PartidaDAO partidaDAO = new PartidaDAO();
    private final CampeonatoDAO campeonatoDAO = new CampeonatoDAO();

    public Partida agendar(Campeonato campeonato, Clube timeA, Clube timeB, LocalDateTime dataHora) {
        if (campeonato == null) {
            throw new RegraDeNegocioException("Selecione um campeonato.");
        }
        if (timeA == null || timeB == null) {
            throw new RegraDeNegocioException("Selecione os dois clubes da partida.");
        }
        if (timeA.equals(timeB)) {
            throw new RegraDeNegocioException("Os clubes da partida devem ser diferentes.");
        }
        if (!campeonato.getClubes().contains(timeA) || !campeonato.getClubes().contains(timeB)) {
            throw new RegraDeNegocioException("Os clubes devem pertencer ao campeonato selecionado.");
        }
        if (dataHora == null) {
            throw new RegraDeNegocioException("O horário da partida é obrigatório.");
        }
        if (!dataHora.isAfter(LocalDateTime.now())) {
            throw new RegraDeNegocioException("O horário da partida deve ser no futuro.");
        }

        Partida partida = new Partida(timeA, timeB, dataHora);
        partidaDAO.salvar(partida);
        campeonato.adicionarPartida(partida);
        campeonatoDAO.atualizar(campeonato);
        return partida;
    }

    public void registrarResultado(Administrador admin, Partida partida, int golsA, int golsB) {
        if (partida == null) {
            throw new RegraDeNegocioException("Selecione uma partida.");
        }
        if (golsA < 0 || golsB < 0) {
            throw new RegraDeNegocioException("O número de gols não pode ser negativo.");
        }
        if (partida.isResultadoRegistrado()) {
            throw new RegraDeNegocioException("O resultado dessa partida já foi registrado.");
        }
        admin.registrarResultado(partida, golsA, golsB);
        partidaDAO.atualizar(partida);
    }

    public List<Partida> listarPendentes() {
        return partidaDAO.buscarPendentes();
    }
}
