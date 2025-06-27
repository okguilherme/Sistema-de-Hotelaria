package main.java.model;

import java.io.Serializable;
import java.util.Objects;

public class Administrador implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String login;
    private String senha;

    // Construtor
    public Administrador(String id, String login, String senha) {
        this.id = id;
        this.login = login;
        this.senha = senha;
    }

    // --- Getters ---
    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    // --- Setters ---
    public void setId(String id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Administrador{" +
               "id='" + id + '\'' +
               ", login='" + login + '\'' +
               ", senha='[PROTEGIDA]'" + // Não exibir a senha em toString
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Administrador that = (Administrador) o;
        return Objects.equals(login, that.login); // O login deve ser único para administradores
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}