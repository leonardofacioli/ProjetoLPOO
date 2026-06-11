package entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Campeonato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @ManyToMany
    @JoinTable(name = "campeonato_clube",
               joinColumns = @JoinColumn(name = "campeonato_id"),
               inverseJoinColumns = @JoinColumn(name = "clube_id"))
    private List<Clube> clubes = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "campeonato_id")
    private List<Partida> partidas = new ArrayList<>();

    public Campeonato() {
    }

    public Campeonato(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void adicionarClube(Clube clube) {
        if (clubes.size() < 8) clubes.add(clube);
    }

    public void adicionarPartida(Partida partida) {
        partidas.add(partida);
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public List<Clube> getClubes() {
        return clubes;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
