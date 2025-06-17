package src.main.java.main;

import src.main.java.io.ReservaArrayInputStream;
import src.main.java.model.Reserva;
import src.main.java.model.Quarto; 
import src.main.java.services.ServicoQuarto; 
import src.main.java.services.ServicoReserva; 

import java.io.DataInputStream;
import java.io.DataOutputStream; 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Servidor {
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("--- Servidor Iniciado ---");
        ServerSocket serverSocket = null;

        // Instanciar os serviços uma única vez
        ServicoQuarto servicoQuarto = new ServicoQuarto();
        // Adicionar alguns quartos ao serviço de quarto para que o ServicoReserva possa usá-los
        servicoQuarto.adicionarQuarto(new Quarto(101, "Standard", 2, 250.0, true));
        servicoQuarto.adicionarQuarto(new Quarto(102, "Luxo", 3, 500.0, true));
        servicoQuarto.adicionarQuarto(new Quarto(103, "Suíte", 4, 750.0, true));
        servicoQuarto.adicionarQuarto(new Quarto(104, "Standard", 2, 250.0, true));
        servicoQuarto.adicionarQuarto(new Quarto(105, "Luxo", 3, 500.0, true));
        System.out.println("Servidor: Quartos iniciais adicionados ao ServicoQuarto.");


        ServicoReserva servicoReserva = new ServicoReserva(servicoQuarto);
        
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Servidor: Escutando na porta " + SERVER_PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\nServidor: Conexão aceita de: " + clientSocket.getInetAddress().getHostAddress());

                new Thread(new ClientHandler(clientSocket, servicoReserva)).start();
            }

        } catch (IOException e) {
            System.err.println("Servidor: Erro fatal no Servidor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Servidor: Erro ao fechar ServerSocket: " + e.getMessage());
            }
            System.out.println("--- Servidor Finalizado ---");
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ServicoReserva servicoReserva;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;

        public ClientHandler(Socket clientSocket, ServicoReserva servicoReserva) {
            this.clientSocket = clientSocket;
            this.servicoReserva = servicoReserva;
            try {
                // Inicializa os streams de dados AQUI, encapsulando os streams do socket
                this.dataIn = new DataInputStream(clientSocket.getInputStream());
                this.dataOut = new DataOutputStream(clientSocket.getOutputStream());
            } catch (IOException e) {
                System.err.println("Servidor: Erro ao inicializar DataStreams para o cliente " + clientSocket.getInetAddress().getHostAddress() + ": " + e.getMessage());
            }
        }

        @Override
        public void run() {
            try {
                // Primeiro, lê o comando que o cliente quer executar
                String comando = dataIn.readUTF();
                System.out.println("Servidor: Cliente " + clientSocket.getInetAddress().getHostAddress() + " enviou comando: " + comando);

                if (comando.equals("ENVIAR_RESERVAS") || comando.equals("FAZER_RESERVA_MANUAL")) {
                    processarReservas();
                } else if (comando.equals("LISTAR_QUARTOS")) {
                    enviarQuartosDisponiveis();
                } else {
                    System.out.println("Servidor: Comando desconhecido recebido: " + comando);
                    dataOut.writeUTF("ERRO: Comando desconhecido.");
                }

            } catch (IOException e) {
                System.err.println("Servidor: Erro na comunicação com o cliente " + clientSocket.getInetAddress().getHostAddress() + ": " + e.getMessage());
            } finally {
                try {
                    if (dataOut != null) dataOut.close();
                    if (dataIn != null) dataIn.close();
                    if (clientSocket != null && !clientSocket.isClosed()) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    System.err.println("Servidor: Erro ao fechar recursos do cliente: " + e.getMessage());
                }
                System.out.println("Servidor: Conexão com " + clientSocket.getInetAddress().getHostAddress() + " encerrada.");
            }
        }

        private void processarReservas() throws IOException {
            int numberOfReservas = dataIn.readInt(); 
            System.out.println("Servidor: Cliente " + clientSocket.getInetAddress().getHostAddress() + " enviará " + numberOfReservas + " reservas.");

            ReservaArrayInputStream reservaStreamIn = new ReservaArrayInputStream(dataIn);
            
            Reserva reservaRecebida;
            int contador = 0;
            for (int i = 0; i < numberOfReservas; i++) {
                reservaRecebida = reservaStreamIn.readReserva(); // Lê a reserva usando o stream encadeado
                if (reservaRecebida != null) {
                    contador++;
                    System.out.println("Servidor: Processando Reserva #" + contador + " (ID: " + reservaRecebida.getIdReserva() + ", Quarto: " + reservaRecebida.getNumeroQuarto() + ") ...");
                    
                    Reserva reservaProcessada = servicoReserva.fazerReserva(reservaRecebida);
                    
                    if (reservaProcessada != null) {
                        System.out.println("Servidor: SUCESSO: Reserva #" + contador + " registrada. (ID: " + reservaProcessada.getIdReserva() + ")");
                    } else {
                        System.out.println("Servidor: FALHA: Não foi possível registrar Reserva #" + contador + " (ID: " + reservaRecebida.getIdReserva() + ").");
                    }
                } else {
                    System.err.println("Servidor: Erro: Recebido objeto nulo inesperadamente ao ler reserva " + (i+1) + " de " + numberOfReservas + ".");
                    break; 
                }
            }
            System.out.println("Servidor: Total de " + contador + " Reservas recebidas e processadas do cliente " + clientSocket.getInetAddress().getHostAddress() + ".");
        }

        private void enviarQuartosDisponiveis() throws IOException {
            List<Quarto> quartosDisponiveis = servicoReserva.servicoQuarto.listarQuartosDisponiveis();
            
            dataOut.writeUTF("OK_QUARTOS");
            dataOut.writeInt(quartosDisponiveis.size()); // Envia a quantidade de quartos

            System.out.println("Servidor: Enviando " + quartosDisponiveis.size() + " quartos disponíveis para o cliente " + clientSocket.getInetAddress().getHostAddress() + ".");

            if (quartosDisponiveis.isEmpty()) {
                System.out.println("Servidor: Nenhum quarto disponível para enviar.");
            }

            for (Quarto quarto : quartosDisponiveis) {
                // Serialização manual de Quarto para o cliente via DataOutputStream
                dataOut.writeInt(quarto.getNumero());
                dataOut.writeUTF(quarto.getTipo());
                dataOut.writeInt(quarto.getCapacidade());
                dataOut.writeDouble(quarto.getPrecoDiaria());
                dataOut.writeBoolean(quarto.isDisponivel());
                System.out.println("Servidor: Enviado Quarto " + quarto.getNumero()); 
            }
            dataOut.flush();
        }
    }
}