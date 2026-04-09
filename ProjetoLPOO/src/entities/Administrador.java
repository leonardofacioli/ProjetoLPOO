package entities;

public class Administrador extends Usuario {

    public Administrador(String nome) {
        super(nome);
    }

    @Override
    public String obterPapel() {
        return "Administrador do Sistema";
    }

    public void registrarResultado(Partida partida, int golsA, int golsB) {
        partida.registrarResultado(golsA, golsB);
    }
}
