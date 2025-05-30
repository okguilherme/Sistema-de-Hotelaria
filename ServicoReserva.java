//package com.hotelaria.service;

//import com.hotelaria.model.Reserva;
//import com.hotelaria.model.Quarto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID; // Para gerar IDs de reserva únicos

public class ServicoReserva {
    // Usaremos um HashMap para armazenar as reservas, onde a chave é o ID da
    // reserva.
    private Map<String, Reserva> reservas;
    private ServicoQuarto servicoQuarto; // Dependência para verificar disponibilidade de quartos

    public ServicoReserva(ServicoQuarto servicoQuarto) {
        this.reservas = new HashMap<>();
        this.servicoQuarto = servicoQuarto; // Injetar o serviço de quarto
    }

    /**
     * Tenta fazer uma nova reserva.
     * 
     * @param reserva O objeto Reserva contendo os detalhes da reserva.
     * @return O objeto Reserva com um ID gerado se a reserva for bem-sucedida, ou
     *         null caso contrário.
     */
    public Reserva fazerReserva(Reserva reserva) {
        // 1. Verificar se o quarto existe e está disponível
        Quarto quarto = servicoQuarto.buscarQuarto(reserva.getNumeroQuarto());
        if (quarto == null) {
            System.out.println("Erro: Quarto " + reserva.getNumeroQuarto() + " não encontrado.");
            return null;
        }
        if (!quarto.isDisponivel()) {
            System.out.println("Erro: Quarto " + reserva.getNumeroQuarto() + " não está disponível.");
            return null;
        }

        // 2. Gerar um ID único para a reserva
        String idGerado = UUID.randomUUID().toString();
        reserva.SetIdReserva(idGerado);

        // 3. (Opcional) Calcular o valor total se não estiver preenchido
        // Aqui você faria o cálculo baseado nas datas e preço da diária do quarto.
        // Por enquanto, assumimos que o valorTotal já vem no objeto reserva ou é
        // calculado de outra forma.

        // 4. Registrar a reserva
        reservas.put(idGerado, reserva);

        // 5. Atualizar a disponibilidade do quarto para false
        servicoQuarto.atualizarDisponibilidadeQuarto(quarto.getNumero(), false);

        System.out.println("Reserva " + idGerado + " para o quarto " + quarto.getNumero() + " realizada com sucesso.");
        return reserva;
    }

    /**
     * Cancela uma reserva existente.
     * 
     * @param idReserva O ID da reserva a ser cancelada.
     * @return true se a reserva foi cancelada com sucesso, false se não foi
     *         encontrada.
     */
    public boolean cancelarReserva(String idReserva) {
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

    /**
     * Busca uma reserva pelo seu ID.
     * 
     * @param idReserva O ID da reserva a ser buscada.
     * @return O objeto Reserva correspondente, ou null se não for encontrado.
     */
    public Reserva buscarReserva(String idReserva) {
        return reservas.get(idReserva);
    }

    /**
     * Lista todas as reservas de um hóspede específico.
     * 
     * @param idHospede O ID do hóspede.
     * @return Uma lista de reservas feitas pelo hóspede.
     */
    public List<Reserva> listarReservasPorHospede(String idHospede) {
        List<Reserva> reservasDoHospede = new ArrayList<>();
        for (Reserva reserva : reservas.values()) {
            if (reserva.getIdHospede().equals(idHospede)) {
                reservasDoHospede.add(reserva);
            }
        }
        return reservasDoHospede;
    }

    /**
     * Retorna uma lista de todas as reservas no sistema.
     * 
     * @return Uma lista de todos os objetos Reserva.
     */
    public List<Reserva> listarTodasReservas() {
        return new ArrayList<>(reservas.values());
    }
}