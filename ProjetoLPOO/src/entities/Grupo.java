package entities;

import java.util.ArrayList;
import java.util.List;

public class Grupo {
    private String nome;
    private List<Participante> participantes;

    public Grupo(String nome) {
        this.nome = nome;
        this.participantes = new ArrayList<>();
    }

    // Construtor Sobrecarregado
    public Grupo(String nome, Participante p) {
        this.nome = nome;
        this.participantes = new ArrayList<>();
        this.participantes.add(p);
    }

    public void adicionarParticipante(Participante p) {
        if (participantes.size() < 5) participantes.add(p);
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }

    public void mostrarClassificacao() {
        participantes.sort((p1, p2) -> p2.calcularPontuacao() - p1.calcularPontuacao());
        System.out.println("=== Classificação do grupo " + nome + " ===");
        for (Participante p : participantes) {
            System.out.println(p.getNome() + " - " + p.calcularPontuacao() + " pontos");
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setParticipantes(List<Participante> participantes) {
        this.participantes = participantes;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}