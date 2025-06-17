package src.main.java.model;

import java.util.Objects; 

// import java.time.LocalDate; // Se for usar LocalDate

public class Reserva {
    private String CPF;
    private String idHospede;
    private int numeroQuarto;
    private String dataCheckIn; // Ou LocalDate
    private String dataCheckOut; // Ou LocalDate
    private double valorTotal;
    private String idReserva;

    public Reserva(String CPF, String idHospede, int numeroQuarto, String dataCheckIn, String dataCheckOut,
            double valorTotal, String idReserva) { 
        this.CPF = CPF;
        this.idHospede = idHospede;
        this.numeroQuarto = numeroQuarto;
        this.dataCheckIn = dataCheckIn;
        this.dataCheckOut = dataCheckOut;
        this.valorTotal = valorTotal;
        this.idReserva = idReserva; 
    }

    // Métodos getters 
    public String getIdReserva() {
        return idReserva;
    }

    public String getCPF() {
        return CPF;
    }

    public int getNumeroQuarto() {
        return numeroQuarto;
    }

    public String getDataCheckIn() {
        return dataCheckIn;
    }

    public String getDataCheckOut() {
        return dataCheckOut;
    }

    public String getIdHospede() {
        return idHospede;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    // Métodos setters
    public void setCPF(String cPF) {
        CPF = cPF;
    }

    public void setDataCheckIn(String dataCheckIn) {
        this.dataCheckIn = dataCheckIn;
    }

    public void setNumeroQuarto(int numeroQuarto) {
        this.numeroQuarto = numeroQuarto;
    }

    public void setDataCheckOut(String dataCheckOut) {
        this.dataCheckOut = dataCheckOut;
    }

    public void setIdHospede(String idHospede) {
        this.idHospede = idHospede;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setIdReserva(String idReserva) { // Renomeado setIdReserva para idReserva para consistência
        this.idReserva = idReserva;
    }

    @Override
    public String toString() {
        return "Reserva [CPF=" + CPF + ", idHospede=" + idHospede + ", numeroQuarto=" + numeroQuarto + ", dataCheckIn="
                + dataCheckIn + ", dataCheckOut=" + dataCheckOut + ", valorTotal=" + valorTotal + ", idReserva="
                + idReserva + "]";
    }

    @Override
    public boolean equals(Object o) {
        // Se o objeto é a mesma instância, são iguais
        if (this == o) return true;
        // Se o objeto é nulo ou não é da mesma classe, não são iguais
        if (o == null || getClass() != o.getClass()) return false;
        // Faz o cast para Reserva
        Reserva reserva = (Reserva) o;
        // Compara todos os campos relevantes para determinar a igualdade lógica
        return numeroQuarto == reserva.numeroQuarto && // Compara int
               Double.compare(reserva.valorTotal, valorTotal) == 0 && // Compara double 
               Objects.equals(CPF, reserva.CPF) && // Compara String, lida com nulls
               Objects.equals(idHospede, reserva.idHospede) &&
               Objects.equals(dataCheckIn, reserva.dataCheckIn) &&
               Objects.equals(dataCheckOut, reserva.dataCheckOut) &&
               Objects.equals(idReserva, reserva.idReserva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(CPF, idHospede, numeroQuarto, dataCheckIn, dataCheckOut, valorTotal, idReserva);
    }
}