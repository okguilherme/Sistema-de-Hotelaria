package src.main;

import src.model.QuartoRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor iniciado na porta 12345...");

            QuartoRepository repo = new QuartoRepository();

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress());

                new Thread(() -> {
                    try (
                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {
                        while (true) {
                            String msg = in.readLine();
                            if (msg == null || msg.equalsIgnoreCase("sair")) {
                                break;
                            }

                            if (msg.startsWith("verificar")) {
                                int numero = Integer.parseInt(msg.split(" ")[1]);
                                boolean disponivel = repo.verificarDisponibilidade(numero);
                                out.println(disponivel ? "disponivel" : "ocupado");
                            } else if (msg.startsWith("reservar")) {
                                int numero = Integer.parseInt(msg.split(" ")[1]);
                                boolean reservado = repo.ocuparQuarto(numero);
                                out.println(reservado ? "reserva_ok" : "reserva_falhou");
                            } else {
                                out.println("comando_invalido");
                            }
                        }
                        socket.close();
                        System.out.println("Cliente desconectado.");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
