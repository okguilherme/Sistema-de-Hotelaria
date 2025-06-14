package src.main.java.main;

import src.main.java.io.ReservaArrayInputStream;
import src.main.java.model.Reserva;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.HashMap;
//import java.util.Map;
import java.io.DataInputStream; // Adicione esta linha

public class Servidor {
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("--- Servidor Iniciado ---");
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Servidor escutando na porta " + SERVER_PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Conexão aceita de: " + clientSocket.getInetAddress().getHostAddress());

                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            System.err.println("Erro no Servidor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Erro ao fechar ServerSocket: " + e.getMessage());
            }
            System.out.println("--- Servidor Finalizado ---");
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            InputStream socketIn = null;
            ReservaArrayInputStream reservaStreamIn = null;

            try {
                socketIn = clientSocket.getInputStream();
                reservaStreamIn = new ReservaArrayInputStream(socketIn);
                System.out.println("Iniciando leitura de Reservas do cliente "
                        + clientSocket.getInetAddress().getHostAddress() + "...");

                DataInputStream initialDataIn = new DataInputStream(socketIn);
                int numberOfReservas = initialDataIn.readInt(); // Lê a quantidade de reservas

                System.out.println("Cliente " + clientSocket.getInetAddress().getHostAddress() + " enviará "
                        + numberOfReservas + " reservas.");

                Reserva reservaRecebida;
                int contador = 0;
                // Loop FOR baseado na quantidade de reservas esperadas
                for (int i = 0; i < numberOfReservas; i++) {
                    reservaRecebida = reservaStreamIn.readReserva();
                    if (reservaRecebida != null) {
                        contador++;
                        System.out.println("Servidor recebeu Reserva #" + contador + ": " + reservaRecebida);
                    } else {
                        System.out.println("Erro: Read null reserva antes do esperado.");
                        break; // Sai do loop se ler null antes da quantidade esperada
                    }
                }
                System.out.println("Total de " + contador + " Reservas recebidas do cliente "
                        + clientSocket.getInetAddress().getHostAddress() + ".");

            } catch (IOException e) {
                System.err.println("Erro ao lidar com o cliente " + clientSocket.getInetAddress().getHostAddress()
                        + ": " + e.getMessage());
                e.printStackTrace(); // Para ver a stack trace completa do erro
            } finally {
                try {
                    if (reservaStreamIn != null) {
                        reservaStreamIn.close();
                    }
                    if (clientSocket != null && !clientSocket.isClosed()) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    System.err.println("Erro ao fechar recursos do cliente: " + e.getMessage());
                }
                System.out.println("Conexão com " + clientSocket.getInetAddress().getHostAddress() + " encerrada.");
            }
        }
    }
}