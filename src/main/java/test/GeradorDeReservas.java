package src.main.java.test; // Ajustado

import src.main.java.model.Reserva; // Importação ajustada
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GeradorDeReservas {

    public static Reserva[] criarReservasDeTeste(int quantidade) {
        List<Reserva> reservasList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int i = 1; i <= quantidade; i++) {
            String cpf = String.format("111.222.333-%02d", i);
            String idHospede = "HSP" + i;
            int numeroQuarto = 100 + i;
            String dataCheckIn = LocalDate.now().plusDays(i).format(formatter);
            String dataCheckOut = LocalDate.now().plusDays(i + 5).format(formatter);
            double valorTotal = 250.00 * i;
            String idReserva = "RES" + System.currentTimeMillis() % 10000 + i;

            reservasList.add(new Reserva(cpf, idHospede, numeroQuarto, dataCheckIn, dataCheckOut, valorTotal, idReserva));
        }
        return reservasList.toArray(new Reserva[0]);
    }
}