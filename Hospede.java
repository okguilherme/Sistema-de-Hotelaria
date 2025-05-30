public class Hospede {
    private String nome;
    private String cpf;
    private String telefone;
    private String email;

    // Construtor 
    public Hospede(String nome, String cpf, String telefone, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
    }

    // Métodos getters 
    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    // Métodos setters 
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Considera hóspedes iguais se o CPF for igual
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Hospede hospede = (Hospede) obj;
        return cpf.equals(hospede.cpf);
    }

    // Retorna um código baseado no cpf do hóspede 
    public int hashCode() {
        return cpf.hashCode();
    }

    // Retorna uma representação textual do hóspede
    public String toString() {
        return "Hospede{" + 
               "nome='" + nome + '\'' +
               ", cpf='" + cpf + '\'' +
               ", telefone='" + telefone + '\'' +
               ", email='" + email + '\'' +
               '}';
    }
}
