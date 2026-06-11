package service;

import dao.CampeonatoDAO;
import entities.Campeonato;
import entities.Clube;

import java.util.List;

public class CampeonatoService {

    public static final int MIN_CLUBES = 2;
    public static final int MAX_CLUBES = 8;

    private final CampeonatoDAO campeonatoDAO = new CampeonatoDAO();
    public Campeonato criar(String nome, List<Clube> clubes) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraDeNegocioException("O nome do campeonato é obrigatório.");
        }
        String nomeLimpo = nome.trim();
        if (campeonatoDAO.buscarPorNome(nomeLimpo).isPresent()) {
            throw new RegraDeNegocioException("Já existe um campeonato chamado \"" + nomeLimpo + "\".");
        }
        if (clubes == null || clubes.size() < MIN_CLUBES) {
            throw new RegraDeNegocioException("Selecione ao menos " + MIN_CLUBES + " clubes.");
        }
        if (clubes.size() > MAX_CLUBES) {
            throw new RegraDeNegocioException("O campeonato pode ter no máximo " + MAX_CLUBES + " clubes.");
        }
        if (clubes.stream().distinct().count() != clubes.size()) {
            throw new RegraDeNegocioException("Há clubes repetidos na seleção.");
        }

        Campeonato campeonato = new Campeonato(nomeLimpo);
        for (Clube clube : clubes) {
            campeonato.adicionarClube(clube);
        }
        return campeonatoDAO.salvar(campeonato);
    }

    public List<Campeonato> listarTodos() {
        return campeonatoDAO.buscarTodos();
    }
}
