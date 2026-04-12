package entities;

import interfaces.Pontuavel;

import java.util.ArrayList;
import java.util.List;

public class Participante extends Usuario implements Pontuavel {
    private List<Aposta> apostas;

    public Participante(String nome) {
        super(nome);
        apostas = new ArrayList<>();
    }

    public void apostar(Aposta aposta) {
        apostas.add(aposta);
    }

    @Override
    public int calcularPontuacao() {
        int total = 0;
        for (Aposta a : apostas) total += a.calcularPontuacao();
        return total;
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}