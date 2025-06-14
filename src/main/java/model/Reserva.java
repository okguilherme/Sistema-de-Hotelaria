package src.main.java.model;

public class Reserva {
    private String CPF;
    private String idHospede;
    private int numeroQuarto;
    private String dataCheckIn;
    private String dataCheckOut;
    private double valorTotal;
    private String idReserva;

    public Reserva(String CPF, String idHospede, int numeroQuarto, String dataCheckIn, String dataCheckOut,
            double valorTotal, String setIdReserva) {
        this.CPF = CPF;
        this.idHospede = idHospede;
        this.numeroQuarto = numeroQuarto;
        this.dataCheckIn = dataCheckIn;
        this.dataCheckOut = dataCheckOut;
        this.valorTotal = valorTotal;
        this.idReserva = setIdReserva;
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

    public void setIdReserva(String setIdReserva) {
        this.idReserva = setIdReserva;
    }

    @Override
    public String toString() {
        return "Reserva [CPF=" + CPF + ", idHospede=" + idHospede + ", numeroQuarto=" + numeroQuarto + ", dataCheckIn="
                + dataCheckIn + ", dataCheckOut=" + dataCheckOut + ", valorTotal=" + valorTotal + ", idReserva="
                + idReserva + "]";
    }
}