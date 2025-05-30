import java.time.LocalDate;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((quarto == null) ? 0 : quarto.hashCode());
        result = prime * result + ((dataEntrada == null) ? 0 : dataEntrada.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Reserva other = (Reserva) obj;
        if (quarto == null) {
            if (other.quarto != null)
                return false;
        } else if (!quarto.equals(other.quarto))
            return false;
        if (dataEntrada == null) {
            if (other.dataEntrada != null)
                return false;
        } else if (!dataEntrada.equals(other.dataEntrada))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Reserva [quarto=" + quarto + ", hospede=" + hospede + ", dataEntrada=" + dataEntrada + ", dataSaida="
                + dataSaida + "]";
    }

    
}
