package src.main.java.services;

import src.main.java.model.Reserva;
import src.main.java.model.Quarto;
import src.main.java.util.JsonUtil;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ReservaServiceImpl extends UnicastRemoteObject implements ReservaServiceRemote {
    private Map<String, Reserva> reservas;
    private QuartoServiceRemote servicoQuartoRemote;

    public ReservaServiceImpl(QuartoServiceRemote servicoQuartoRemote) throws RemoteException {
        super();
        this.reservas = new HashMap<>();
        this.servicoQuartoRemote = servicoQuartoRemote;

        // Carregar reservas do arquivo JSON e atualizar quartos
        List<Reserva> reservasCarregadas = JsonUtil.carregarReservas();
        for (Reserva r : reservasCarregadas) {
            reservas.put(r.getIdReserva(), r);
            servicoQuartoRemote.atualizarDisponibilidadeQuarto(r.getNumeroQuarto(), false);
        }
    }

    @Override
    public Reserva fazerReserva(Reserva reserva) throws RemoteException {
        Quarto quarto = servicoQuartoRemote.buscarQuarto(reserva.getNumeroQuarto());
        if (quarto == null || !quarto.isDisponivel()) {
            System.out.println("Erro: Quarto " + reserva.getNumeroQuarto() + " não está disponível.");
            return null;
        }

        String idGerado = UUID.randomUUID().toString();
        reserva.setIdReserva(idGerado);
        reservas.put(idGerado, reserva);
        servicoQuartoRemote.atualizarDisponibilidadeQuarto(quarto.getNumero(), false);
        JsonUtil.salvarReservas(new ArrayList<>(reservas.values()));

        System.out.println("Reserva " + idGerado + " para o quarto " + quarto.getNumero() + " realizada com sucesso.");
        return reserva;
    }

    @Override
    public boolean cancelarReserva(String idReserva) throws RemoteException {
        Reserva r = reservas.remove(idReserva);
        if (r != null) {
            servicoQuartoRemote.atualizarDisponibilidadeQuarto(r.getNumeroQuarto(), true);
            JsonUtil.salvarReservas(new ArrayList<>(reservas.values()));
            System.out.println("Reserva " + idReserva + " cancelada com sucesso.");
            return true;
        } else {
            System.out.println("Erro: reserva com ID " + idReserva + " não encontrada.");
            return false;
        }
    }

    @Override
    public Reserva buscarReserva(String idReserva) throws RemoteException {
        return reservas.get(idReserva);
    }

    @Override
    public List<Reserva> listarReservasPorHospede(String cpf) throws RemoteException {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : reservas.values()) {
            if (r.getCPF().equals(cpf)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    @Override
    public Map<String, Reserva> getMapaReservas() throws RemoteException {
        return reservas;
    }

    @Override
    public List<Reserva> listarTodasReservas() throws RemoteException {
        return new ArrayList<>(reservas.values());
    }

}
