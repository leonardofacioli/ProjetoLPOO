package entities;

import interfaces.Pontuavel;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Participante")
public class Participante extends Usuario implements Pontuavel {
    @OneToMany(mappedBy = "participante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aposta> apostas = new ArrayList<>();

    public Participante() {
        super();
    }

    public Participante(String nome) {
        super(nome);
    }

    public List<Aposta> getApostas() {
        return apostas;
    }

    public void apostar(Aposta aposta) {
        aposta.setParticipante(this);
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
