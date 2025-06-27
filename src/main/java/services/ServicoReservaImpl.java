package src.main.java.services;

import src.main.java.model.Quarto;
import src.main.java.model.Reserva;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServicoReservaImpl extends UnicastRemoteObject implements ReservaServiceRemote {
    private Map<String, Reserva> reservas;
    private QuartoServiceRemote servicoQuartoRemote;

    // Construtor que recebe a instância remota de ServicoQuarto
    public ServicoReservaImpl(QuartoServiceRemote servicoQuartoRemote) throws RemoteException {
        super();
        this.reservas = new HashMap<>();
        this.servicoQuartoRemote = servicoQuartoRemote;
    }

    @Override
    public Reserva fazerReserva(Reserva reserva) throws RemoteException {
        Quarto quarto = servicoQuartoRemote.buscarQuarto(reserva.getNumeroQuarto());
        if (quarto == null) {
            System.out.println("Erro: Quarto " + reserva.getNumeroQuarto() + " não encontrado.");
            return null;
        }
        if (!quarto.isDisponivel()) {
            System.out.println("Erro: Quarto " + reserva.getNumeroQuarto() + " não está disponível.");
            return null;
        }

        String idGerado = UUID.randomUUID().toString();
        reserva.setIdReserva(idGerado);

        reservas.put(idGerado, reserva);

        servicoQuartoRemote.atualizarDisponibilidadeQuarto(quarto.getNumero(), false);

        System.out.println("Reserva " + idGerado + " para o quarto " + quarto.getNumero() + " realizada com sucesso.");
        return reserva;
    }

    @Override
    public boolean cancelarReserva(String idReserva) throws RemoteException {
        Reserva reservaRemovida = reservas.remove(idReserva);

        if (reservaRemovida != null) {
            servicoQuartoRemote.atualizarDisponibilidadeQuarto(reservaRemovida.getNumeroQuarto(), true);
            System.out.println("Reserva " + idReserva + " cancelada com sucesso.");
            return true;
        }
        System.out.println("Erro: Reserva com ID " + idReserva + " não encontrada para cancelamento.");
        return false;
    }

    @Override
    public Reserva buscarReserva(String idReserva) throws RemoteException {
        return reservas.get(idReserva);
    }

    @Override
    public List<Reserva> listarReservasPorHospede(String idHospede) throws RemoteException {
        List<Reserva> reservasDoHospede = new ArrayList<>();
        for (Reserva reserva : reservas.values()) {
            if (reserva.getIdHospede().equals(idHospede)) {
                reservasDoHospede.add(reserva);
            }
        }
        return reservasDoHospede;
    }

    @Override
    public Map<String, Reserva> getMapaReservas() throws RemoteException {
        return this.reservas;
    }

    @Override
    public List<Reserva> listarTodasReservas() throws RemoteException {
        return new ArrayList<>(reservas.values());
    }
}
