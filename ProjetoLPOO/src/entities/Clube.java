package entities;

public class Clube {
    private String nome;

    public Clube() {
        this.nome = "Clube Indefinido";
    }

    public Clube(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

	@Override
	public String toString() {
		return this.nome;
	}
}
