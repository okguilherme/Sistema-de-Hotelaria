package main.java.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Voto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String idVoto; // ID único do voto
    private String idEleitor; // ID do eleitor que realizou o voto
    private String idCandidato; // ID do candidato que recebeu o voto
    private String timestamp; // Usaremos String para simplificar a serialização, formatando LocalDateTime

    // Construtor
    public Voto(String idEleitor, String idCandidato) {
        this.idVoto = UUID.randomUUID().toString(); // Gera um ID único para o voto
        this.idEleitor = idEleitor;
        this.idCandidato = idCandidato;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    // --- Getters ---
    public String getIdVoto() {
        return idVoto;
    }

    public String getIdEleitor() {
        return idEleitor;
    }

    public String getIdCandidato() {
        return idCandidato;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // --- Setters (geralmente não são necessários para Voto, pois é um registro imutável) ---
    // Deixaremos apenas para completude, mas em um sistema real, Voto seria imutável.
    public void setIdVoto(String idVoto) {
        this.idVoto = idVoto;
    }

    public void setIdEleitor(String idEleitor) {
        this.idEleitor = idEleitor;
    }

    public void setIdCandidato(String idCandidato) {
        this.idCandidato = idCandidato;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Voto{" +
               "idVoto='" + idVoto + '\'' +
               ", idEleitor='" + idEleitor + '\'' +
               ", idCandidato='" + idCandidato + '\'' +
               ", timestamp='" + timestamp + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voto voto = (Voto) o;
        // Um voto é único por seu idVoto.
        return Objects.equals(idVoto, voto.idVoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVoto);
    }
}