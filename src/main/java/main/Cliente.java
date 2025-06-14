package src.main.java.main;

import src.main.java.io.ReservaArrayOutputStream;
import src.main.java.model.Reserva;
import src.main.java.test.GeradorDeReservas;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Cliente {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("--- Cliente Iniciado ---");
        Socket socket = null;
        OutputStream socketOut = null;
        ReservaArrayOutputStream reservaStreamOut = null;

        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectado ao servidor em " + SERVER_ADDRESS + ":" + SERVER_PORT);

            socketOut = socket.getOutputStream();

            int quantidadeReservas = 5;
            Reserva[] reservasParaEnviar = GeradorDeReservas.criarReservasDeTeste(quantidadeReservas);
            System.out.println("Criadas " + quantidadeReservas + " Reservas para enviar.");

            reservaStreamOut = new ReservaArrayOutputStream(socketOut, reservasParaEnviar, quantidadeReservas);
            System.out.println("Iniciando envio das Reservas...");

            reservaStreamOut.writeAllReservas();

            System.out.println("Todas as reservas enviadas para o servidor.");

        } catch (IOException e) {
            System.err.println("Erro na comunicação do Cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (reservaStreamOut != null) {
                    reservaStreamOut.close(); // Isso também vai dar flush e fechar o socketOut
                }
                if (socket != null && !socket.isClosed()) {
                    socket.close(); // Fecha o socket, se ainda não estiver fechado pelo close do stream.
                }
            } catch (IOException e) {
                System.err.println("Erro ao fechar recursos do Cliente: " + e.getMessage());
            }
            System.out.println("--- Cliente Finalizado ---");
        }
    }
}