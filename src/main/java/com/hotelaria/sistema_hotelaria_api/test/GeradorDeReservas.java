package com.hotelaria.sistema_hotelaria_api.test;

import com.hotelaria.sistema_hotelaria_api.model.Reserva;
import com.hotelaria.sistema_hotelaria_api.model.Hospede; 
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GeradorDeReservas {

    public static Reserva[] criarReservasDeTeste(int quantidade) {
        List<Reserva> reservasList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 1; i <= quantidade; i++) {
            String cpfHospede = String.format("111222333%02d", i); 
            String nomeHospede = "Hóspede Teste " + i;
            String telefoneHospede = String.format("8899%08d", i); 
            String emailHospede = "hospede" + i + "@teste.com";

            // Cria um objeto Hospede
            Hospede hospede = new Hospede(nomeHospede, cpfHospede, telefoneHospede, emailHospede);

            int numeroQuarto = 100 + i; // Ajuste se seus quartos iniciais não vão até 100+quantidade
            String dataCheckIn = LocalDate.now().plusDays(i).format(formatter);
            String dataCheckOut = LocalDate.now().plusDays(i + 5).format(formatter);
            
            reservasList.add(new Reserva(numeroQuarto, hospede, dataCheckIn, dataCheckOut));
        }
        return reservasList.toArray(new Reserva[0]);
    }
}