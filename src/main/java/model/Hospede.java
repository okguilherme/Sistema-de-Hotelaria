package src.main.java.model;

public class Hospede extends Pessoa {
    public Hospede(String nome, String cpf, String telefone, String email) {
        super(nome, cpf, telefone, email);
    }

    @Override
    public String toString() {
        return "Hospede -> " + super.toString();
    }
}
