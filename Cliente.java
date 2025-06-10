import io.ReservaOutputStream;
import model.Reserva;
import java.io.OutputStream;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 12345);

        Reserva[] reservas = new Reserva[3];
        reservas[0] = new Reserva("11111111111", "H1", 101, "2025-06-01", "2025-06-05", 500.0, "R1");
        reservas[1] = new Reserva("22222222222", "H2", 102, "2025-06-02", "2025-06-06", 600.0, "R2");
        reservas[2] = new Reserva("33333333333", "H3", 103, "2025-06-03", "2025-06-07", 700.0, "R3");

        OutputStream out = socket.getOutputStream();
        ReservaOutputStream reservaOut = new ReservaOutputStream(out, reservas, 3);
        reservaOut.enviar();

        out.close();
        socket.close();
    }
}
