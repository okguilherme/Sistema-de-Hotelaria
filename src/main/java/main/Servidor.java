package src.main.java.main;

import src.main.java.services.ServicoQuartoImpl;
import src.main.java.services.ServicoReservaImpl;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {
    private static final int RMI_PORT = 1099; // Porta padrão para o RMI

    public static void main(String[] args) {
        System.out.println("--- Servidor RMI Iniciado ---");

        try {
            // 1. Criar e iniciar o RMI Registry
            // Se o registry já estiver rodando, LocateRegistry.getRegistry() retorna a
            // referência.
            // Se não estiver, createRegistry() cria e inicia um novo.
            Registry registry = LocateRegistry.createRegistry(RMI_PORT);
            System.out.println("RMI Registry iniciado na porta " + RMI_PORT + ".");

            // 2. Instanciar as implementações dos serviços
            ServicoQuartoImpl servicoQuarto = new ServicoQuartoImpl();
            System.out.println("Instância de ServicoQuartoImpl criada.");

            // 3. Instanciar ServicoReservaImpl passando a instância de ServicoQuartoImpl
            ServicoReservaImpl servicoReserva = new ServicoReservaImpl(servicoQuarto);
            System.out.println("Instância de ServicoReservaImpl criada.");

            registry.rebind("ServicoQuarto", servicoQuarto);
            registry.rebind("ServicoReserva", servicoReserva);

            System.out.println("Serviços 'ServicoQuarto' e 'ServicoReserva' registrados no RMI Registry.");
            System.out.println("Servidor RMI pronto para receber requisições...");

            // Adicione alguns quartos iniciais para teste
            servicoQuarto.adicionarQuarto(new src.main.java.model.Quarto(101, "Simples", 1, 150.00, true));
            servicoQuarto.adicionarQuarto(new src.main.java.model.Quarto(102, "Duplo", 2, 250.00, true));
            servicoQuarto.adicionarQuarto(new src.main.java.model.Quarto(201, "Suite", 3, 400.00, true));

        } catch (RemoteException e) {
            System.err.println("Erro ao iniciar ou registrar serviços RMI: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}