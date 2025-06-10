import io.ReservaInputStream;
import model.Reserva;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor {
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(12345);
        System.out.println("Servidor aguardando conexão...");

        Socket socket = server.accept();
        System.out.println("Cliente conectado!");

        InputStream in = socket.getInputStream();
        ReservaInputStream reservaIn = new ReservaInputStream(in);
        Map<String, Reserva> reservas = new HashMap<>();

        reservaIn.lerReservas(reservas, 3); // GARANTA QUE O CLIENTE ENVIA 3

        for (Reserva r : reservas.values()) {
            System.out.println(r);
        }

        in.close();

        server.close();
    }
}
