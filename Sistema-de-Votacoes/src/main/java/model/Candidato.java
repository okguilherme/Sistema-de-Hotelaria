package main.java.model;

import java.io.Serializable;
import java.util.Objects;

public class Candidato implements Serializable{
    private static final long serialVersionUID = 1L;

    private String id;
    private String nome;
    private int numeroVotacao;
    private String partido;
    private int totalVotos;

    public Candidato(String id, String nome, int numeroVotacao, String partido) {
        this.id = id;
        this.nome = nome;
        this.numeroVotacao = numeroVotacao;
        this.partido = partido;
        this.totalVotos = 0;
    }

    // Getters
    public String getId() { return id; }
    public String getNome() { return nome; }
    public int getNumeroVotacao() { return numeroVotacao; }
    public String getPartido() { return partido; }
    public int getTotalVotos() { return totalVotos; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setNumeroVotacao(int numeroVotacao) { this.numeroVotacao = numeroVotacao; }
    public void setPartido(String partido) { this.partido = partido; }
    public void setTotalVotos(int totalVotos) { this.totalVotos = totalVotos; }

    public void adicionarVoto() { this.totalVotos++; }

    @Override
    public String toString() {
        return "Candidato{" +
               "id='" + id + '\'' +
               ", nome='" + nome + '\'' +
               ", numeroVotacao=" + numeroVotacao +
               ", partido='" + partido + '\'' +
               ", totalVotos=" + totalVotos +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidato candidato = (Candidato) o;
        return numeroVotacao == candidato.numeroVotacao;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroVotacao);
    }
}
