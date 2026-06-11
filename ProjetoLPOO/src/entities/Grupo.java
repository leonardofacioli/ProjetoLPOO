package entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @ManyToMany
    @JoinTable(name = "grupo_participante",
               joinColumns = @JoinColumn(name = "grupo_id"),
               inverseJoinColumns = @JoinColumn(name = "participante_id"))
    private List<Participante> participantes = new ArrayList<>();

    public Grupo() {
    }

    public Grupo(String nome) {
        this.nome = nome;
    }

    public Grupo(String nome, Participante p) {
        this.nome = nome;
        this.participantes.add(p);
    }

    public Long getId() {
        return id;
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
