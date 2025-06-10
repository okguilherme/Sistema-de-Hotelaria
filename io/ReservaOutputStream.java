package io;

import model.Reserva;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ReservaOutputStream {
    private DataOutputStream out;
    private Reserva[] reservas;
    private int quantidade;

    public ReservaOutputStream(OutputStream out, Reserva[] reservas, int quantidade) {
        this.out = new DataOutputStream(out);
        this.reservas = reservas;
        this.quantidade = quantidade;
    }

    public void enviar() throws IOException {
        for (int i = 0; i < quantidade; i++) {
            Reserva r = reservas[i];
            out.writeUTF(r.getCPF());
            out.writeUTF(r.getIdHospede());
            out.writeInt(r.getNumeroQuarto());
            out.writeUTF(r.getDataCheckIn());
            out.writeUTF(r.getDataCheckOut());
            out.writeDouble(r.getValorTotal());
            out.writeUTF(r.getIdReserva());
        }
        out.flush();
    }
}
