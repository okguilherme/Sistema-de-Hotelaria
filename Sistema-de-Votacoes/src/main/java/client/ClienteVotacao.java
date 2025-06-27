package main.java.client;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClienteVotacao {

    private static final String HOST = "localhost";
    private static final int PORTA = 12345;
    private static final String MULTICAST_IP = "230.0.0.0";
    private static final int MULTICAST_PORTA = 6789;

    public static void main(String[] args) {
        new Thread(() -> escutarNotasUDP()).start();

        try (
            Socket socket = new Socket(HOST, PORTA);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println(in.readLine()); // mensagem de boas-vindas

            boolean autenticado = false;
            String idEleitor = null;

            // Autenticação
            while (!autenticado) {
                System.out.print("Login: ");
                String login = scanner.nextLine();

                System.out.print("Senha: ");
                String senha = scanner.nextLine();

                out.println("LOGIN;" + login + ";" + senha);
                String resposta = in.readLine();
                if ("LOGIN_OK".equals(resposta)) {
                    System.out.println("Login realizado com sucesso!");
                    autenticado = true;

                    // Mapeia login para ID (ajuste conforme necessário)
                    if ("user1".equals(login)) idEleitor = "e001";
                    if ("user2".equals(login)) idEleitor = "e002";
                    if ("user3".equals(login)) idEleitor = "e003";
                } else {
                    System.out.println("Login falhou.");
                }
            }

            // Menu principal
            boolean ativo = true;
            while (ativo) {
                System.out.println("\nMENU");
                System.out.println("1. Listar candidatos");
                System.out.println("2. Votar");
                System.out.println("3. Ver status da votação");
                System.out.println("4. Sair");
                System.out.print("Escolha: ");
                String opcao = scanner.nextLine();

                switch (opcao) {
                    case "1":
                        out.println("LISTAR");
                        String tipo = in.readLine();
                        if ("CANDIDATOS_JSON".equals(tipo)) {
                            String json = in.readLine();
                            System.out.println("Candidatos disponíveis (JSON):");
                            System.out.println(json);
                        } else {
                            System.out.println("Erro ao receber candidatos.");
                        }
                        break;

                    case "2":
                        System.out.print("Digite o ID do candidato (ex: c001): ");
                        String idCandidato = scanner.nextLine().trim();

                        if (idEleitor == null) {
                            System.out.println("ERRO: ID do eleitor desconhecido.");
                            break;
                        }

                        String votoJson = "{\"idEleitor\":\"" + idEleitor + "\",\"idCandidato\":\"" + idCandidato + "\"}";
                        out.println("VOTAR_JSON;" + votoJson);
                        String respVoto = in.readLine();
                        System.out.println("Resposta: " + respVoto);
                        break;

                    case "3":
                        out.println("STATUS");
                        System.out.println("Status da votação: " + in.readLine());
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
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private static void escutarNotasUDP() {
        try (MulticastSocket socket = new MulticastSocket(MULTICAST_PORTA)) {
            InetAddress grupo = InetAddress.getByName(MULTICAST_IP);
            NetworkInterface nif = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            socket.joinGroup(new InetSocketAddress(grupo, MULTICAST_PORTA), nif);

            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
                socket.receive(pacote);
                String mensagem = new String(pacote.getData(), 0, pacote.getLength());
                System.out.println("\n[NOTA DO ADMINISTRADOR]: " + mensagem + "\n");
            }
        } catch (IOException e) {
            System.err.println("Erro no receptor de notas do administrador: " + e.getMessage());
        }
    }
}
