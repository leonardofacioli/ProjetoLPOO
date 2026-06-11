package service;

import dao.ClubeDAO;
import entities.Clube;

import java.util.List;

public class ClubeService {

    private final ClubeDAO clubeDAO = new ClubeDAO();

    public Clube cadastrar(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraDeNegocioException("O nome do clube é obrigatório.");
        }
        String nomeLimpo = nome.trim();
        if (clubeDAO.buscarPorNome(nomeLimpo).isPresent()) {
            throw new RegraDeNegocioException("Já existe um clube com o nome \"" + nomeLimpo + "\".");
        }
        return clubeDAO.salvar(new Clube(nomeLimpo));
    }

    public void inicializarClubesPadrao() {
        if (clubeDAO.contar() > 0) return;
        String[] nomes = {"Corinthians", "Palmeiras", "São Paulo", "Santos",
                          "Flamengo", "Grêmio", "Internacional", "Atlético-MG"};
        for (String n : nomes) {
            clubeDAO.salvar(new Clube(n));
        }
    }

    public List<Clube> listarTodos() {
        return clubeDAO.buscarTodos();
    }
}
