package model;

public class Quarto {
    private int numero; // Número do quarto
    private String tipo; // Tipo do quarto (ex: simples, duplo, suíte)
    private int capacidade; // Capacidade de pessoas
    private double precoDiaria; // Preço por diária
    private boolean disponivel; // Disponibilidade

    // Construtor
    public Quarto(int numero, String tipo, int capacidade, double precoDiaria, boolean disponivel) {
        this.numero = numero;
        this.tipo = tipo;
        this.capacidade = capacidade;
        this.precoDiaria = precoDiaria;
        this.disponivel = disponivel;
    }

    // Métodos getters
    public int getNumero() {
        return numero;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public double getPrecoDiaria() {
        return precoDiaria;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    // Métodos setters
    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public void setPrecoDiaria(double precoDiaria) {
        this.precoDiaria = precoDiaria;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + numero;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Quarto other = (Quarto) obj;
        if (numero != other.numero)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Quarto [numero=" + numero + ", tipo=" + tipo + ", capacidade=" + capacidade + ", precoDiaria="
                + precoDiaria + ", disponivel=" + disponivel + "]";
    }
}
