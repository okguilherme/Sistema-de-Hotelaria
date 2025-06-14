package src.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("127.0.0.1", 12345);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                Scanner teclado = new Scanner(System.in)
        ) {
            System.out.println("Conectado ao servidor.");

            while (true) {
                System.out.println("\nDigite o número do quarto para consultar (ou 'sair' para encerrar): ");
                String entrada = teclado.nextLine();

                if (entrada.equalsIgnoreCase("sair")) {
                    out.println("sair");
                    break;
                }

                out.println("verificar " + entrada);
                String resposta = in.readLine();

                if (resposta.equals("disponivel")) {
                    System.out.println("O quarto está disponível.");
                    System.out.println("Deseja reservar? (s/n)");
                    String opcao = teclado.nextLine();

                    if (opcao.equalsIgnoreCase("s")) {
                        out.println("reservar " + entrada);
                        String respostaReserva = in.readLine();
                        if (respostaReserva.equals("reserva_ok")) {
                            System.out.println("Reserva realizada com sucesso!");
                        } else {
                            System.out.println("Falha na reserva. Talvez o quarto tenha sido reservado por outro.");
                        }
                    }
                } else if (resposta.equals("ocupado")) {
                    System.out.println("O quarto está ocupado.");
                } else {
                    System.out.println("Resposta do servidor: " + resposta);
                }
            }

            System.out.println("Desconectado.");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }        
    }
}

