package src.main.java.main;

import src.main.java.io.ReservaArrayInputStream;
import src.main.java.io.ReservaArrayOutputStream;
import src.main.java.model.Reserva;
import src.main.java.model.Quarto; 
import src.main.java.test.GeradorDeReservas;

import java.io.DataInputStream; 
import java.io.DataOutputStream; 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList; 
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Cliente {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("--- Cliente Iniciado ---");
        Scanner scanner = new Scanner(System.in);
        int escolha;

        do {
            exibirMenu();
            escolha = lerEscolha(scanner);

            switch (escolha) {
                case 1:
                    enviarReservasDeTeste(scanner);
                    break;
                case 2:
                    fazerNovaReservaManualmente(scanner);
                    break;
                case 3: // Nova opção
                    listarQuartosDisponiveis();
                    break;
                case 0:
                    System.out.println("Saindo do Cliente. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, tente novamente.");
            }
            
            if (escolha != 0) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine(); 
            }

        } while (escolha != 0);

        scanner.close();
        System.out.println("--- Cliente Finalizado ---");
    }

    private static void exibirMenu() {
        System.out.println("\n--- MENU DO CLIENTE ---");
        System.out.println("1. Enviar Reservas de Teste (5 reservas automáticas)");
        System.out.println("2. Fazer Nova Reserva (entrada manual)");
        System.out.println("3. Listar Quartos Disponíveis");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerEscolha(Scanner scanner) {
        try {
            int escolha = scanner.nextInt();
            scanner.nextLine(); 
            return escolha;
        } catch (InputMismatchException e) {
            System.out.println("Cliente: Entrada inválida. Digite um número.");
            scanner.nextLine();
            return -1;
        }
    }

    private static void enviarReservasDeTeste(Scanner scanner) {
        System.out.print("Cliente: Quantas reservas de teste você deseja enviar (padrão: 5)? ");
        int quantidade = lerEscolha(scanner); 
        if (quantidade <= 0) {
            quantidade = 5; 
            System.out.println("Cliente: Quantidade inválida, enviando 5 reservas de teste.");
        }
        enviarComandoEReservas("ENVIAR_RESERVAS", GeradorDeReservas.criarReservasDeTeste(quantidade), quantidade);
    }

    private static void fazerNovaReservaManualmente(Scanner scanner) {
        System.out.println("\n--- Criar Nova Reserva Manualmente ---");
        
        System.out.print("Cliente: CPF do Hóspede: ");
        String cpf = scanner.nextLine();

        System.out.print("Cliente: ID do Hóspede: ");
        String idHospede = scanner.nextLine();

        int numeroQuarto = 0;
        boolean quartoValido = false;
        while (!quartoValido) {
            System.out.print("Cliente: Número do Quarto: ");
            try {
                numeroQuarto = scanner.nextInt();
                quartoValido = true;
            } catch (InputMismatchException e) {
                System.out.println("Cliente: Entrada inválida. Digite um número inteiro para o quarto.");
                scanner.nextLine(); 
            }
        }
        scanner.nextLine(); 

        System.out.print("Cliente: Data Check-in (DD/MM/YYYY): ");
        String dataCheckIn = scanner.nextLine();

        System.out.print("Cliente: Data Check-out (DD/MM/YYYY): ");
        String dataCheckOut = scanner.nextLine();

        double valorTotal = 0.0;
        boolean valorValido = false;
        while (!valorValido) {
            System.out.print("Cliente: Valor Total: ");
            try {
                valorTotal = scanner.nextDouble();
                valorValido = true;
            } catch (InputMismatchException e) {
                System.out.println("Cliente: Entrada inválida. Digite um número para o valor.");
                scanner.nextLine(); // Limpa o buffer
            }
        }
        scanner.nextLine();

        String idReserva = "MANUAL_" + UUID.randomUUID().toString().substring(0, 8); // Gera um ID único

        Reserva novaReserva = new Reserva(cpf, idHospede, numeroQuarto, dataCheckIn, dataCheckOut, valorTotal, idReserva);
        System.out.println("Cliente: Reserva criada: " + novaReserva);

        enviarComandoEReservas("FAZER_RESERVA_MANUAL", new Reserva[]{novaReserva}, 1);
    }

    private static void enviarComandoEReservas(String comando, Reserva[] reservas, int quantidade) {
        Socket socket = null;
        DataOutputStream dataOut = null; 
        DataInputStream dataIn = null; 

        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Cliente: Conectado ao servidor em " + SERVER_ADDRESS + ":" + SERVER_PORT);

            OutputStream socketOut = socket.getOutputStream();
            InputStream socketIn = socket.getInputStream(); 

            dataOut = new DataOutputStream(socketOut); 
            dataIn = new DataInputStream(socketIn);     

            // Primeiro, envia o comando para o servidor
            dataOut.writeUTF(comando);
            System.out.println("Cliente: Enviando comando '" + comando + "' para o servidor.");

            if (comando.equals("ENVIAR_RESERVAS") || comando.equals("FAZER_RESERVA_MANUAL")) {
                dataOut.writeInt(quantidade); // Envia a quantidade de reservas
                dataOut.flush(); // Garante que o comando e a quantidade sejam enviados ANTES das reservas

                // O ReservaArrayOutputStream agora recebe o DataOutputStream
                try (ReservaArrayOutputStream reservaStreamOut = new ReservaArrayOutputStream(dataOut, reservas, quantidade)) {
                    System.out.println("Cliente: Iniciando envio de " + quantidade + " reservas para o servidor...");
                    reservaStreamOut.writeAllReservas();
                    System.out.println("Cliente: Envio de reservas concluído.");
                }

            } else if (comando.equals("LISTAR_QUARTOS")) {
                dataOut.flush(); 
                
                String respostaStatus = dataIn.readUTF();
                
                if (respostaStatus.equals("OK_QUARTOS")) {
                    int numQuartos = dataIn.readInt(); 
                    System.out.println("Cliente: Servidor enviou " + numQuartos + " quartos disponíveis:");
                    if (numQuartos == 0) {
                        System.out.println("  Nenhum quarto disponível no momento.");
                    }
                    for (int i = 0; i < numQuartos; i++) {
                        // Deserialização manual de Quarto vindo do servidor
                        int numero = dataIn.readInt();
                        String tipo = dataIn.readUTF();
                        int capacidade = dataIn.readInt();
                        double preco = dataIn.readDouble();
                        boolean disponivel = dataIn.readBoolean(); // Este campo sempre será true para quartos "disponíveis"
                        System.out.println("  Quarto [numero=" + numero + ", tipo=" + tipo + ", capacidade=" + capacidade + ", precoDiaria=" + preco + ", disponivel=" + disponivel + "]");
                    }
                } else {
                    System.out.println("Cliente: Servidor respondeu com erro ao listar os quartos.");
                }
            }


        } catch (IOException e) {
            System.err.println("Cliente: Erro na comunicação: " + e.getMessage());
        } finally {
            try {
                if (dataOut != null) dataOut.close(); 
                if (dataIn != null) dataIn.close();   
                if (socket != null && !socket.isClosed()) {
                    socket.close(); 
                }
            } catch (IOException e) {
                System.err.println("Cliente: Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    private static void listarQuartosDisponiveis() {
        enviarComandoEReservas("LISTAR_QUARTOS", null, 0); // Comando para listar quartos
    }
}