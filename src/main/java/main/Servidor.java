package src.main.java.main;

import src.main.java.services.QuartoServiceImpl;
import src.main.java.services.ReservaServiceImpl;
import src.main.java.model.Quarto;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {
    private static final int RMI_PORT = 1099;

    public static void main(String[] args) {
        System.out.println("===================================");
        System.out.println("   Servidor RMI - Iniciado");
        System.out.println("===================================");

        try {
            Registry registry = LocateRegistry.createRegistry(RMI_PORT);
            System.out.println("RMI Registry criado na porta " + RMI_PORT);

            // 1. Criar o serviço de quartos
            QuartoServiceImpl servicoQuarto = new QuartoServiceImpl();
            System.out.println("Instância de QuartoServiceImpl criada");

            // 2. Adicionar os quartos antes de aplicar as reservas
            servicoQuarto.adicionarQuarto(new Quarto(101, "Simples", 1, 150.00, true));
            servicoQuarto.adicionarQuarto(new Quarto(102, "Duplo", 2, 250.00, true));
            servicoQuarto.adicionarQuarto(new Quarto(201, "Suite", 3, 400.00, true));
            System.out.println("Quartos iniciais cadastrados");

            // 3. Criar o serviço de reservas (agora que os quartos já existem)
            ReservaServiceImpl servicoReserva = new ReservaServiceImpl(servicoQuarto);
            System.out.println("Instância de ReservaServiceImpl criada");

            // 4. Registrar no RMI
            registry.rebind("ServicoQuarto", servicoQuarto);
            registry.rebind("ServicoReserva", servicoReserva);
            System.out.println("Serviços registrados no RMI Registry");

            System.out.println("\nServidor pronto para receber requisições");

        } catch (RemoteException e) {
            System.err.println("Erro RMI: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
