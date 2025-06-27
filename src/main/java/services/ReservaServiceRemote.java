package src.main.java.services;

import src.main.java.model.Reserva;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ReservaServiceRemote extends Remote {
    Reserva fazerReserva(Reserva reserva) throws RemoteException;

    boolean cancelarReserva(String idReserva) throws RemoteException;

    Reserva buscarReserva(String idReserva) throws RemoteException;

    List<Reserva> listarReservasPorHospede(String idHospede) throws RemoteException;

    Map<String, Reserva> getMapaReservas() throws RemoteException;

    List<Reserva> listarTodasReservas() throws RemoteException;
}