package entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Administrador")
public class Administrador extends Usuario {

    public Administrador() {
        super();
    }

    public Administrador(String nome) {
        super(nome);
    }

    public void registrarResultado(Partida partida, int golsA, int golsB) {
        partida.registrarResultado(golsA, golsB);
    }
}
