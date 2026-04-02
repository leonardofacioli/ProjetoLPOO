package entities;

import java.time.LocalDateTime;

public class Partida {
	 private Clube timeA;
	    private Clube timeB;
	    private LocalDateTime dataHora;
	    private Integer golsA;
	    private Integer golsB;

	    public Partida(Clube timeA, Clube timeB, LocalDateTime dataHora) {
	        this.timeA = timeA;
	        this.timeB = timeB;
	        this.dataHora = dataHora;
	        this.golsA = null;
	        this.golsB = null;
	    }

	    public Clube getTimeA() { return timeA; }
	    public Clube getTimeB() { return timeB; }
	    public LocalDateTime getDataHora() { return dataHora; }

	    public boolean podeApostar() {
	        return LocalDateTime.now().isBefore(dataHora.minusMinutes(20));
	    }

	    public void registrarResultado(int golsA, int golsB) {
	        this.golsA = golsA;
	        this.golsB = golsB;
	    }

	    public Integer getGolsA() { return golsA; }
	    public Integer getGolsB() { return golsB; }

	    public String resultado() {
	        if (golsA == null || golsB == null) return "Não definido";
	        if (golsA > golsB) return timeA.getNome();
	        else if (golsB > golsA) return timeB.getNome();
	        else return "Empate";
	    }
}
