//package com.hotelaria.service;

//import com.hotelaria.model.Reserva;
//import com.hotelaria.model.Quarto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServicoReserva {
    // HashMap para armazenar as reservas, onde a chave é o ID da reserva.
    private Map<String, Reserva> reservas;
    private ServicoQuarto servicoQuarto;

    public ServicoReserva(ServicoQuarto servicoQuarto) {
        this.reservas = new HashMap<>();
        this.servicoQuarto = servicoQuarto; // Injetar o serviço de quarto
    }

    // Método para fazer reserva
    public Reserva fazerReserva(Reserva reserva) {
        // Verificar se o quarto existe e está disponível
        Quarto quarto = servicoQuarto.buscarQuarto(reserva.getNumeroQuarto());
        if (quarto == null) {
            System.out.println("Erro: Quarto " + reserva.getNumeroQuarto() + " não encontrado.");
            return null;
        }
        if (!quarto.isDisponivel()) {
            System.out.println("Erro: Quarto " + reserva.getNumeroQuarto() + " não está disponível.");
            return null;
        }

        // Se está disponivel, gera um ID único para a reserva
        String idGerado = UUID.randomUUID().toString();
        reserva.setIdReserva(idGerado);

        // Registrar a reserva
        reservas.put(idGerado, reserva);

        // Atualiza a disponibilidade do quarto para false
        servicoQuarto.atualizarDisponibilidadeQuarto(quarto.getNumero(), false);

        System.out.println("Reserva " + idGerado + " para o quarto " + quarto.getNumero() + " realizada com sucesso.");
        return reserva;
    }

    // Cancela uma reserva pelo ID
    public boolean cancelarReserva(String idReserva) {
        // retorna o objeto Reserva removido ou null se não existir.
        Reserva reservaRemovida = reservas.remove(idReserva);

        if (reservaRemovida != null) {
            // Atualizar a disponibilidade do quarto para true novamente
            servicoQuarto.atualizarDisponibilidadeQuarto(reservaRemovida.getNumeroQuarto(), true);
            System.out.println("Reserva " + idReserva + " cancelada com sucesso.");
            return true;
        }
        System.out.println("Erro: Reserva com ID " + idReserva + " não encontrada para cancelamento.");
        return false;
    }

    // Busca uma reserva pelo ID
    public Reserva buscarReserva(String idReserva) {
        return reservas.get(idReserva);
    }

    // Lista todas as reservas de um hóspede específico.
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

    // Retorna uma lista de todas as reservas no sistema.
    public List<Reserva> listarTodasReservas() {
        return new ArrayList<>(reservas.values());
    }
}