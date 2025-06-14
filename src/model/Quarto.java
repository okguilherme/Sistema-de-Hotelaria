package src.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Quarto {
    @JsonProperty
    private int numero;

    @JsonProperty
    private boolean ocupado;

    public Quarto() {
    }

    public Quarto(int numero, boolean ocupado) {
        this.numero = numero;
        this.ocupado = ocupado;
    }

    public int getNumero() {
        return numero;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }
}
