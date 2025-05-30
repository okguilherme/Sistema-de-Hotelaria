import java.time.LocalDate;
import java.util.Objects;

public class Reserva {
    private Quarto quarto; // Quarto reservado
    private Hospede hospede; // Hóspede que fez a reserva
    private LocalDate dataEntrada;
    private LocalDate dataSaida;

    // Construtor 
    public Reserva(Quarto quarto, Hospede hospede, LocalDate dataEntrada, LocalDate dataSaida) {
        this.quarto = quarto;
        this.hospede = hospede;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
    }

    // Métodos getters
    public Quarto getQuarto() {
        return quarto;
    }

    public Hospede getHospede() {
        return hospede;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    // Métodos setters
    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }

    public void setHospede(Hospede hospede) {
        this.hospede = hospede;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public void setDataSaida(LocalDate dataSaida) {
        this.dataSaida = dataSaida;
    }

    // Verifica se duas reservas são iguais com base no quarto e na data de entrada
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Reserva reserva = (Reserva) obj;
        return Objects.equals(quarto, reserva.quarto) &&
               Objects.equals(dataEntrada, reserva.dataEntrada);
    }

    // Gera o código hash da reserva com base no quarto e na data de entrada
     public int hashCode() {
        return Objects.hash(quarto, dataEntrada);
    }

    // Representação em string de uma reserva 
    public String toString() {
        return "Reserva{" +
               "quarto=" + quarto +
               ", hospede=" + hospede +
               ", dataEntrada=" + dataEntrada +
               ", dataSaida=" + dataSaida +
               '}';
    }
}
