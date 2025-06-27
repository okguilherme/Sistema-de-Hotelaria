package src.main.java.services;

import src.main.java.model.Quarto;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface QuartoServiceRemote extends Remote {
    boolean adicionarQuarto(Quarto quarto) throws RemoteException;

    boolean removerQuarto(int numero) throws RemoteException;

    Quarto buscarQuarto(int numero) throws RemoteException;

    List<Quarto> listarQuartosDisponiveis() throws RemoteException;

    boolean atualizarDisponibilidadeQuarto(int numeroQuarto, boolean disponivel) throws RemoteException;
}