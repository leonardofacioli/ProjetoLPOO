package entities;

import jakarta.persistence.*;

@Entity
public class Aposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "partida_id")
    private Partida partida;

    @ManyToOne
    @JoinColumn(name = "participante_id")
    private Participante participante;

    @Column(name = "gols_a")
    private int golsA;

    @Column(name = "gols_b")
    private int golsB;

    public Aposta() {
    }

    public Aposta(Partida partida, int golsA, int golsB) {
        this.partida = partida;
        this.golsA = golsA;
        this.golsB = golsB;
    }

    public Long getId() {
        return id;
    }

    public Partida getPartida() {
        return partida;
    }

    public int getGolsA() {
        return golsA;
    }

    public int getGolsB() {
        return golsB;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public int calcularPontuacao() {
        if (partida.getGolsA() == null || partida.getGolsB() == null) return 0;

        boolean resultadoCorreto = (golsA > golsB && partida.getGolsA() > partida.getGolsB()) ||
                (golsB > golsA && partida.getGolsB() > partida.getGolsA()) ||
                (golsA == golsB && partida.getGolsA().equals(partida.getGolsB()));

        boolean placarExato = (golsA == partida.getGolsA() && golsB == partida.getGolsB());

        if (placarExato) return 10;
        else if (resultadoCorreto) return 5;
        else return 0;
    }
}
