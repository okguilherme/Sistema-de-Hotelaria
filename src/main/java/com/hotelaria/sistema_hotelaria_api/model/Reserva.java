package com.hotelaria.sistema_hotelaria_api.model;

import java.io.Serializable;

public class Reserva implements Serializable {
    private String idReserva; 
    private int numeroQuarto;
    private Hospede hospede; 
    private String dataCheckin;
    private String dataCheckout;
    private double valorTotal; 

    // Construtor completo com o objeto Hospede
    public Reserva(int numeroQuarto, Hospede hospede, String dataCheckin, String dataCheckout) {
        this.numeroQuarto = numeroQuarto;
        this.hospede = hospede;
        this.dataCheckin = dataCheckin;
        this.dataCheckout = dataCheckout;
    }

    public Reserva() {
        // Construtor vazio necessário para que o Gson consiga criar instâncias da classe
    }


    // Métodos Getters
    public String getIdReserva() {
        return idReserva;
    }

    public int getNumeroQuarto() {
        return numeroQuarto;
    }

    public Hospede getHospede() { 
        return hospede;
    }

    public String getDataCheckin() {
        return dataCheckin;
    }

    public String getDataCheckout() {
        return dataCheckout;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    // Métodos Setters
    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public void setNumeroQuarto(int numeroQuarto) {
        this.numeroQuarto = numeroQuarto;
    }

    public void setHospede(Hospede hospede) { 
        this.hospede = hospede;
    }

    public void setDataCheckin(String dataCheckin) {
        this.dataCheckin = dataCheckin;
    }

    public void setDataCheckout(String dataCheckout) {
        this.dataCheckout = dataCheckout;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }


    @Override
    public String toString() {
        return "Reserva [idReserva=" + idReserva + ", numeroQuarto=" + numeroQuarto + ", hospede=" + hospede
                + ", dataCheckin=" + dataCheckin + ", dataCheckout=" + dataCheckout + ", valorTotal=" + valorTotal + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idReserva == null) ? 0 : idReserva.hashCode());
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
        if (idReserva == null) {
            if (other.idReserva != null)
                return false;
        } else if (!idReserva.equals(other.idReserva))
            return false;
        return true;
    }
}