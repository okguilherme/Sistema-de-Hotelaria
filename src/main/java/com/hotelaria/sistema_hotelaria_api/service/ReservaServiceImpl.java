package com.hotelaria.sistema_hotelaria_api.service;

import com.hotelaria.sistema_hotelaria_api.model.Reserva;
import com.hotelaria.sistema_hotelaria_api.model.Quarto;
import com.hotelaria.sistema_hotelaria_api.util.JsonUtil;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class ReservaServiceImpl {
    private Map<String, Reserva> reservas;
    private final QuartoServiceImpl quartoService;

    @Autowired
    public ReservaServiceImpl(QuartoServiceImpl quartoService) {
        this.reservas = new HashMap<>();
        this.quartoService = quartoService;

        List<Reserva> reservasCarregadas = JsonUtil.carregarReservas();
        if (reservasCarregadas != null) { 
            for (Reserva r : reservasCarregadas) {
                // Ao carregar, verifique se o hóspede não é nulo antes de processar
                if (r.getHospede() != null && r.getIdReserva() != null) { 
                    reservas.put(r.getIdReserva(), r);
                    this.quartoService.atualizarDisponibilidadeQuarto(r.getNumeroQuarto(), false);
                } else {
                    System.err.println("Aviso: Reserva carregada com dados incompletos (hóspede ou ID nulo), ignorando: " + r);
                }
            }
        }
    }

    public Reserva fazerReserva(Reserva reserva) {
        // Nova verificação para garantir que o objeto Hospede e seu CPF não sejam nulos
        if (reserva.getHospede() == null || reserva.getHospede().getCpf() == null || reserva.getHospede().getCpf().isEmpty()) {
            System.out.println("Erro: Dados do hóspede (CPF) ausentes ou inválidos na reserva.");
            return null;
        }

        Quarto quarto = quartoService.buscarQuarto(reserva.getNumeroQuarto());
        if (quarto == null || !quarto.isDisponivel()) {
            System.out.println("Erro: Quarto " + reserva.getNumeroQuarto() + " não está disponível.");
            return null;
        }

        String idGerado = UUID.randomUUID().toString();
        reserva.setIdReserva(idGerado);
        reservas.put(idGerado, reserva);
        quartoService.atualizarDisponibilidadeQuarto(quarto.getNumero(), false);
        JsonUtil.salvarReservas(new ArrayList<>(reservas.values()));

        System.out.println("Reserva " + idGerado + " para o quarto " + quarto.getNumero() + " realizada com sucesso.");
        return reserva;
    }

    public boolean cancelarReserva(String idReserva) {
        Reserva r = reservas.remove(idReserva);
        if (r != null) {
            quartoService.atualizarDisponibilidadeQuarto(r.getNumeroQuarto(), true);
            JsonUtil.salvarReservas(new ArrayList<>(reservas.values()));
            System.out.println("Reserva " + idReserva + " cancelada com sucesso.");
            return true;
        } else {
            System.out.println("Erro: reserva com ID " + idReserva + " não encontrada.");
            return false;
        }
    }

    public Reserva buscarReserva(String idReserva) {
        return reservas.get(idReserva);
    }

    public List<Reserva> listarReservasPorHospede(String cpf) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : reservas.values()) {
            // Acessa o CPF através do objeto Hospede, com verificações de nulo
            if (r.getHospede() != null && r.getHospede().getCpf() != null && r.getHospede().getCpf().equals(cpf)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    public Map<String, Reserva> getMapaReservas() {
        return reservas;
    }

    public List<Reserva> listarTodasReservas() {
        return new ArrayList<>(reservas.values());
    }
}