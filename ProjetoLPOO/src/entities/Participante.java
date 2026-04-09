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

    // Polimorfismo: Sobrecarga (Overloading)
    public void apostar(Partida partida, int golsA, int golsB) {
        apostas.add(new Aposta(partida, golsA, golsB));
    }

    // Polimorfismo: Sobreposição (Override) do método de negócio abstrato
    @Override
    public String obterPapel() {
        return "Apostador (Participante)";
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