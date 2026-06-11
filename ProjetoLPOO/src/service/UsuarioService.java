package service;

import dao.UsuarioDAO;
import entities.Administrador;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    public Administrador obterAdministrador(String nome) {
        return usuarioDAO.buscarAdministrador(nome)
                .orElseGet(() -> (Administrador) usuarioDAO.salvar(new Administrador(nome)));
    }
}
