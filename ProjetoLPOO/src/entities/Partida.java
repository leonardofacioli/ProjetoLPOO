package entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "timeA_id")
    private Clube timeA;

    @ManyToOne
    @JoinColumn(name = "timeB_id")
    private Clube timeB;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "gols_a")
    private Integer golsA;

    @Column(name = "gols_b")
    private Integer golsB;

    public Partida() {
    }

    public Partida(Clube timeA, Clube timeB, LocalDateTime dataHora) {
        this.timeA = timeA;
        this.timeB = timeB;
        this.dataHora = dataHora;
        this.golsA = null;
        this.golsB = null;
    }

    public Long getId() {
        return id;
    }

    public Clube getTimeA() {
        return timeA;
    }

    public Clube getTimeB() {
        return timeB;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public boolean podeApostar() {
        return LocalDateTime.now().isBefore(dataHora.minusMinutes(20)) && golsA == null;
    }

    public void registrarResultado(int golsA, int golsB) {
        if (this.golsA == null) {
            this.golsA = golsA;
            this.golsB = golsB;
        }
    }

    public Integer getGolsA() {
        return golsA;
    }

    public Integer getGolsB() {
        return golsB;
    }

    public boolean isResultadoRegistrado() {
        return golsA != null;
    }

    public String resultado() {
        if (golsA == null || golsB == null) return "Não definido";
        if (golsA > golsB) return timeA.getNome();
        else if (golsB > golsA) return timeB.getNome();
        else return "Empate";
    }

    @Override
    public String toString() {
        return timeA.getNome() + " x " + timeB.getNome() + " (" +
                dataHora.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM HH:mm")) + ")";
    }
}
