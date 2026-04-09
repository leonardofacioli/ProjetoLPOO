package entities;

public class Clube {
    private String nome;

    // Construtor Padrão (Sem argumentos)
    public Clube() {
        this.nome = "Clube Indefinido";
    }

    // Construtor Normal
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
