package src.main.java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Hotel implements Serializable {
    private String nome;
    private List<Quarto> quartos;

    public Hotel(String nome) {
        this.nome = nome;
        this.quartos = new ArrayList<>();
    }

    public String getNome() { return nome; }

    public List<Quarto> getQuartos() { return quartos; }

    public void adicionarQuarto(Quarto quarto) {
        quartos.add(quarto);
    }

    public void removerQuarto(Quarto quarto) {
        quartos.remove(quarto);
    }

    @Override
    public String toString() {
        return "Hotel [nome=" + nome + ", total de quartos=" + quartos.size() + "]";
    }
}
