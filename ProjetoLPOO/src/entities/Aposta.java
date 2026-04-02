package entities;

public class Aposta {
	 private Partida partida;
	    private int golsA;
	    private int golsB;

	    public Aposta(Partida partida, int golsA, int golsB) {
	        this.partida = partida;
	        this.golsA = golsA;
	        this.golsB = golsB;
	    }

	    public Partida getPartida() { return partida; }
	    public int getGolsA() { return golsA; }
	    public int getGolsB() { return golsB; }

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

