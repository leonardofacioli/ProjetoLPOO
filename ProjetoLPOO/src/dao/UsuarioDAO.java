package dao;

import entities.Administrador;
import entities.Usuario;

import java.util.Optional;

public class UsuarioDAO extends GenericDAO<Usuario> {

    public UsuarioDAO() {
        super(Usuario.class);
    }

    public Optional<Administrador> buscarAdministrador(String nome) {
        return em.createQuery(
                        "SELECT a FROM Administrador a WHERE LOWER(a.nome) = LOWER(:nome)",
                        Administrador.class)
                .setParameter("nome", nome)
                .getResultStream()
                .findFirst();
    }
}
