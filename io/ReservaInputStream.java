package io;

import model.Reserva;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ReservaInputStream {
    private DataInputStream in;

    public ReservaInputStream(InputStream in) {
        this.in = new DataInputStream(in);
    }

    public void lerReservas(Map<String, Reserva> mapa, int quantidade) throws IOException {
        for (int i = 0; i < quantidade; i++) {
            String cpf = in.readUTF();
            String idHospede = in.readUTF();
            int numeroQuarto = in.readInt();
            String dataCheckIn = in.readUTF();
            String dataCheckOut = in.readUTF();
            double valorTotal = in.readDouble();
            String idReserva = in.readUTF();

            Reserva reserva = new Reserva(cpf, idHospede, numeroQuarto, dataCheckIn, dataCheckOut, valorTotal,
                    idReserva);
            mapa.put(idReserva, reserva);
        }
    }
}
