public class Quarto {
    private int numero;
    private String tipo;
    private int capacidade;
    private float precoDiaria;
    private boolean disponivel;

    public Quarto(int numero, String tipo, int capacidade, float precoDiaria, boolean disponivel) {
        this.numero = numero;
        this.tipo = tipo;
        this.capacidade = capacidade;
        this.precoDiaria = precoDiaria;
        this.disponivel = disponivel;
    }

    public int getNumero() {
        return numero;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public float getPrecoDiaria() {
        return precoDiaria;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean getDisponivel() {
        return disponivel;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setPrecoDiaria(float precoDiaria) {
        this.precoDiaria = precoDiaria;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}