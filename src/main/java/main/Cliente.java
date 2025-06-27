package src.main.java.main;

import src.main.java.model.Reserva;
import src.main.java.model.Quarto;
import src.main.java.test.GeradorDeReservas;
import src.main.java.services.QuartoServiceRemote;
import src.main.java.services.ReservaServiceRemote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class Cliente {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int RMI_PORT = 1099; // A mesma porta que o servidor está usando

    public static void main(String[] args) {
        System.out.println("--- Cliente RMI Iniciado ---");

        try {
            // 1. Obter uma referência ao RMI Registry no servidor
            Registry registry = LocateRegistry.getRegistry(SERVER_ADDRESS, RMI_PORT);
            System.out.println("Conectado ao RMI Registry em " + SERVER_ADDRESS + ":" + RMI_PORT);

            // 2. Buscar as referências dos objetos remotos pelo nome
            QuartoServiceRemote quartoService = (QuartoServiceRemote) registry.lookup("ServicoQuarto");
            ReservaServiceRemote reservaService = (ReservaServiceRemote) registry.lookup("ServicoReserva");
            System.out.println("Serviços remotos 'ServicoQuarto' e 'ServicoReserva' obtidos.");

            // Listar quartos disponíveis (deve ter os 3 adicionados no servidor)
            System.out.println("\n--- Teste: Listando quartos disponíveis ---");
            List<Quarto> quartosDisponiveis = quartoService.listarQuartosDisponiveis();
            if (quartosDisponiveis.isEmpty()) {
                System.out.println("Nenhum quarto disponível.");
            } else {
                for (Quarto q : quartosDisponiveis) {
                    System.out.println("Quarto disponível: " + q);
                }
            }

            // Teste: Fazer uma reserva
            System.out.println("\n--- Teste: Fazendo uma reserva ---");
            Reserva novaReserva = GeradorDeReservas.criarReservasDeTeste(1)[0]; // Cria uma nova reserva
            novaReserva.setNumeroQuarto(101); // Tenta reservar o quarto 101
            Reserva reservaFeita = reservaService.fazerReserva(novaReserva);

            if (reservaFeita != null) {
                System.out.println("Reserva realizada com sucesso: " + reservaFeita);
                // Buscar a reserva feita
                System.out.println("\n--- Teste: Buscando a reserva recém-feita ---");
                Reserva reservaBuscada = reservaService.buscarReserva(reservaFeita.getIdReserva());
                System.out.println("Reserva buscada: " + reservaBuscada);
            } else {
                System.out.println("Falha ao realizar reserva.");
            }

            // Tentar reservar o mesmo quarto novamente (deve falhar)
            System.out.println("\n--- Teste: Tentando reservar o quarto 101 novamente (deve falhar) ---");
            Reserva outraReserva = GeradorDeReservas.criarReservasDeTeste(1)[0];
            outraReserva.setNumeroQuarto(101);
            reservaService.fazerReserva(outraReserva); // Isso deve imprimir uma mensagem de erro no servidor e retornar
                                                       // null.

            // Cancelar a reserva feita
            if (reservaFeita != null) {
                System.out.println("\n--- Teste: Cancelando a reserva " + reservaFeita.getIdReserva() + " ---");
                boolean cancelado = reservaService.cancelarReserva(reservaFeita.getIdReserva());
                System.out.println("Reserva cancelada: " + cancelado);

                // Teste: Listar quartos disponíveis novamente (quarto 101 deve estar
                // disponível)
                System.out.println("\n--- Teste: Listando quartos disponíveis após cancelamento ---");
                quartosDisponiveis = quartoService.listarQuartosDisponiveis();
                if (quartosDisponiveis.isEmpty()) {
                    System.out.println("Nenhum quarto disponível.");
                } else {
                    for (Quarto q : quartosDisponiveis) {
                        System.out.println("Quarto disponível: " + q);
                    }
                }
            }

        } catch (RemoteException e) {
            System.err.println("Erro de comunicação RMI: " + e.getMessage());
            e.printStackTrace();
        } catch (java.rmi.NotBoundException e) {
            System.err.println("Erro: Nome de serviço não encontrado no Registry: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado no cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("--- Cliente RMI Finalizado ---");
        }
    }
}