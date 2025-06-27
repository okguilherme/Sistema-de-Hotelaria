package main.java.client;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClienteAdmin {

    private static final String HOST = "localhost";
    private static final int PORTA = 12345;
    private static final String MULTICAST_IP = "230.0.0.0";
    private static final int MULTICAST_PORTA = 6789;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(HOST, PORTA);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println(in.readLine()); // Mensagem de boas-vindas

            boolean autenticado = false;

            // Autenticação
            while (!autenticado) {
                System.out.print("Login: ");
                String login = scanner.nextLine();

                System.out.print("Senha: ");
                String senha = scanner.nextLine();

                out.println("LOGIN_ADMIN;" + login + ";" + senha);
                String resposta = in.readLine();
                if ("SUCCESS_ADMIN_LOGIN".equals(resposta)) {
                    System.out.println("Login de administrador realizado com sucesso!");
                    autenticado = true;
                } else {
                    System.out.println("Login de administrador falhou.");
                }
            }

            // Menu do administrador
            boolean ativo = true;
            while (ativo) {
                System.out.println("\nMENU ADMINISTRADOR");
                System.out.println("1. Adicionar candidato");
                System.out.println("2. Remover candidato");
                System.out.println("3. Enviar nota informativa");
                System.out.println("4. Sair");
                System.out.print("Escolha: ");
                String opcao = scanner.nextLine();

                switch (opcao) {
                    case "1":
                        System.out.print("ID do candidato: ");
                        String id = scanner.nextLine();
                        System.out.print("Nome: ");
                        String nome = scanner.nextLine();
                        System.out.print("Número de votação: ");
                        int numero = Integer.parseInt(scanner.nextLine());
                        System.out.print("Partido: ");
                        String partido = scanner.nextLine();

                        String candidatoJson = String.format("{\"id\":\"%s\",\"nome\":\"%s\",\"numero\":%d,\"partido\":\"%s\"}", id, nome, numero, partido);
                        out.println("ADD_CANDIDATO");
                        out.println(candidatoJson);
                        System.out.println(in.readLine()); // status
                        System.out.println(in.readLine()); // mensagem
                        break;

                    case "2":
                        System.out.print("ID do candidato a remover: ");
                        String idRemover = scanner.nextLine();
                        out.println("REMOVE_CANDIDATO");
                        out.println(idRemover);
                        System.out.println(in.readLine());
                        System.out.println(in.readLine());
                        break;

                    case "3":
                        enviarNotaMulticast(scanner);
                        break;

                    case "4":
                        System.out.println("Saindo...");
                        ativo = false;
                        break;

                    default:
                        System.out.println("Opção inválida.");
                }
            }

        } catch (IOException e) {
            System.err.println("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }

    private static void enviarNotaMulticast(Scanner scanner) {
        try (DatagramSocket socket = new DatagramSocket()) {
            System.out.print("Digite a nota a ser enviada: ");
            String mensagem = scanner.nextLine();

            String json = String.format("{\"tipo\":\"nota\",\"conteudo\":\"%s\"}", mensagem);
            byte[] buffer = json.getBytes();
            InetAddress grupo = InetAddress.getByName(MULTICAST_IP);

            DatagramPacket pacote = new DatagramPacket(buffer, buffer.length, grupo, MULTICAST_PORTA);
            socket.send(pacote);

            System.out.println("Nota enviada com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao enviar nota via UDP: " + e.getMessage());
        }
    }
}
