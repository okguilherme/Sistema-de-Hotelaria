package src.main.java.main;

import src.main.java.model.Reserva;
import src.main.java.model.Quarto;
import src.main.java.test.GeradorDeReservas;
import src.main.java.services.QuartoServiceRemote;
import src.main.java.services.ReservaServiceRemote;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class Cliente {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int RMI_PORT = 1099;

    public static void main(String[] args) {
        System.out.println("=== Cliente RMI Iniciado ===");

        try {
            Registry registry = LocateRegistry.getRegistry(SERVER_ADDRESS, RMI_PORT);
            QuartoServiceRemote quartoService = (QuartoServiceRemote) registry.lookup("ServicoQuarto");
            ReservaServiceRemote reservaService = (ReservaServiceRemote) registry.lookup("ServicoReserva");

            Scanner scanner = new Scanner(System.in);
            int opcao;

            do {
                System.out.println("\n===== MENU CLIENTE HOTELARIA =====");
                System.out.println("1. Listar quartos disponíveis");
                System.out.println("2. Fazer uma reserva");
                System.out.println("3. Buscar reserva por ID");
                System.out.println("4. Cancelar reserva");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        List<Quarto> quartosDisponiveis = quartoService.listarQuartosDisponiveis();
                        if (quartosDisponiveis.isEmpty()) {
                            System.out.println("Nenhum quarto disponível.");
                        } else {
                            System.out.println("Quartos disponíveis:");
                            for (Quarto q : quartosDisponiveis) {
                                System.out.println(q);
                            }
                        }
                        break;

                    case 2:
                        Reserva novaReserva = GeradorDeReservas.criarReservasDeTeste(1)[0];
                        System.out.print("Digite o número do quarto desejado: ");
                        int numero = scanner.nextInt();
                        novaReserva.setNumeroQuarto(numero);

                        Reserva r = reservaService.fazerReserva(novaReserva);
                        if (r != null) {
                            System.out.println("Reserva realizada com sucesso:");
                            System.out.println(r);
                        } else {
                            System.out.println("Falha ao realizar reserva. Quarto pode estar indisponível.");
                        }
                        break;

                    case 3:
                        System.out.print("Digite o ID da reserva: ");
                        String idBusca = scanner.nextLine();
                        Reserva encontrada = reservaService.buscarReserva(idBusca);
                        if (encontrada != null) {
                            System.out.println("Reserva encontrada:");
                            System.out.println(encontrada);
                        } else {
                            System.out.println("Reserva não encontrada.");
                        }
                        break;

                    case 4:
                        System.out.print("Digite o ID da reserva a ser cancelada: ");
                        String idCancelamento = scanner.nextLine();
                        boolean cancelada = reservaService.cancelarReserva(idCancelamento);
                        if (cancelada) {
                            System.out.println("Reserva cancelada com sucesso.");
                        } else {
                            System.out.println("Falha ao cancelar. ID não encontrado.");
                        }
                        break;

                    case 0:
                        System.out.println("Encerrando cliente...");
                        break;

                    default:
                        System.out.println("Opção inválida.");
                        break;
                }

            } while (opcao != 0);

            scanner.close();

        } catch (Exception e) {
            System.err.println("Erro no cliente: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== Cliente RMI Finalizado ===");
    }
}
