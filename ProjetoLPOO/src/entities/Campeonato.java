package entities;

import java.util.ArrayList;
import java.util.List;

public class Campeonato {
	  private String nome;
	    private List<Clube> clubes;
	    private List<Partida> partidas;

	    public Campeonato(String nome) {
	        this.nome = nome;
	        this.clubes = new ArrayList<>();
	        this.partidas = new ArrayList<>();
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

	    public List<Partida> getPartidas() { return partidas; }
	    public List<Clube> getClubes() { return clubes; }
}
