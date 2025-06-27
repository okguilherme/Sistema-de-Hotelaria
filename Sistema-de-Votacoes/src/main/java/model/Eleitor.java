package main.java.model;

import java.io.Serializable;
import java.util.Objects;

public class Eleitor implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String login;
    private String senha;
    private boolean jaVotou;

    public Eleitor(String id, String login, String senha) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.jaVotou = false;
    }

    // Getters
    public String getId() { return id; }
    public String getLogin() { return login; }
    public String getSenha() { return senha; }
    public boolean getJaVotou() { return jaVotou; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setLogin(String login) { this.login = login; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setJaVotou(boolean jaVotou) { this.jaVotou = jaVotou; }

    @Override
    public String toString() {
        return "Eleitor{" +
               "id='" + id + '\'' +
               ", login='" + login + '\'' +
               ", senha='[PROTEGIDA]'" +
               ", jaVotou=" + jaVotou +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Eleitor eleitor = (Eleitor) o;
        return Objects.equals(login, eleitor.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
