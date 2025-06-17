package src.main.java.services;

import src.main.java.model.Quarto;
import src.main.java.model.Reserva;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServicoReserva {
    private Map<String, Reserva> reservas;
    public ServicoQuarto servicoQuarto;

    public ServicoReserva(ServicoQuarto servicoQuarto) {
        this.reservas = new HashMap<>();
        this.servicoQuarto = servicoQuarto; // Injetar o serviço de quarto
    }

    public Reserva fazerReserva(Reserva reserva) {
        // Verificar se o quarto existe e está disponível
        Quarto quarto = servicoQuarto.buscarQuarto(reserva.getNumeroQuarto());
        if (quarto == null) {
            System.out.println("[ServicoReserva]: Erro: Quarto " + reserva.getNumeroQuarto() + " não encontrado para a reserva " + reserva.getIdReserva() + ".");
            return null;
        }
        // Verificação de disponibilidade
        if (!quarto.isDisponivel()) {
            System.out.println("[ServicoReserva]: Erro: Quarto " + reserva.getNumeroQuarto() + " não está disponível para a reserva " + reserva.getIdReserva() + ".");
            return null;
        }

        // Se a reserva já tem um ID, verifica se já existe uma reserva com esse ID no mapa
        if(reservas.containsKey(reserva.getIdReserva())) {
             System.out.println("[ServicoReserva]: Erro: Reserva com ID " + reserva.getIdReserva() + " já existe no sistema.");
             return null;
        }


        // Registrar a reserva no mapa de reservas do serviço
        reservas.put(reserva.getIdReserva(), reserva);

        // Atualiza a disponibilidade do quarto para false (ocupado)
        servicoQuarto.atualizarDisponibilidadeQuarto(quarto.getNumero(), false);

        System.out.println("[ServicoReserva]: Reserva " + reserva.getIdReserva() + " para o quarto " + quarto.getNumero() + " realizada com sucesso.");
        return reserva;
    }

    public boolean cancelarReserva(String idReserva) {
        Reserva reservaRemovida = reservas.remove(idReserva);

        if (reservaRemovida != null) {
            servicoQuarto.atualizarDisponibilidadeQuarto(reservaRemovida.getNumeroQuarto(), true);
            System.out.println("[ServicoReserva]: Reserva " + idReserva + " cancelada com sucesso.");
            return true;
        }
        System.out.println("[ServicoReserva]: Erro: Reserva com ID " + idReserva + " não encontrada para cancelamento.");
        return false;
    }

    public Reserva buscarReserva(String idReserva) {
        return reservas.get(idReserva);
    }

    public List<Reserva> listarReservasPorHospede(String idHospede) {
        List<Reserva> reservasDoHospede = new ArrayList<>();
        for (Reserva reserva : reservas.values()) {
            if (reserva.getIdHospede().equals(idHospede)) {
                reservasDoHospede.add(reserva);
            }
        }
        return reservasDoHospede;
    }

    public Map<String, Reserva> getMapaReservas() {
        return this.reservas;
    }

    public List<Reserva> listarTodasReservas() {
        return new ArrayList<>(reservas.values());
    }
}