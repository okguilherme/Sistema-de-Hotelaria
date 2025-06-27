package src.main.java.services;

import src.main.java.model.Quarto;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServicoQuartoImpl extends UnicastRemoteObject implements QuartoServiceRemote {
    // Lista que armazena todos os quartos cadastrados no sistema
    private List<Quarto> quartos = new ArrayList<>();

    public ServicoQuartoImpl() throws RemoteException {
        super(); // Construtor da classe pai (UnicastRemoteObject)
    }

    // Método para adicionar um novo quarto, evitando números duplicados
    @Override
    public boolean adicionarQuarto(Quarto quarto) throws RemoteException {
        // Verifica se já existe um quarto com o mesmo número
        for (Quarto q : quartos) {
            if (q.getNumero() == quarto.getNumero()) {
                System.out.println("Erro: Já existe um quarto com o número " + quarto.getNumero());
                return false;
            }
        }
        quartos.add(quarto);
        System.out.println("Quarto " + quarto.getNumero() + " adicionado.");
        return true;
    }

    // Remove o quarto com o número especificado da lista
    @Override
    public boolean removerQuarto(int numero) throws RemoteException {
        boolean removed = quartos.removeIf(q -> q.getNumero() == numero);
        if (removed) {
            System.out.println("Quarto " + numero + " removido.");
        } else {
            System.out.println("Erro: Quarto " + numero + " não encontrado para remoção.");
        }
        return removed;
    }

    // Busca e retorna o quarto pelo número, ou null se não existir
    @Override
    public Quarto buscarQuarto(int numero) throws RemoteException {
        for (Quarto q : quartos) {
            if (q.getNumero() == numero) {
                return q;
            }
        }
        System.out.println("Quarto " + numero + " não encontrado.");
        return null;
    }

    // Retorna uma lista com os quartos disponíveis
    @Override
    public List<Quarto> listarQuartosDisponiveis() throws RemoteException {
        List<Quarto> disponiveis = new ArrayList<>();
        for (Quarto q : quartos) {
            if (q.isDisponivel()) {
                disponiveis.add(q);
            }
        }
        return disponiveis;
    }

    // Atualiza o status de disponibilidade do quarto pelo número
    @Override
    public boolean atualizarDisponibilidadeQuarto(int numeroQuarto, boolean disponivel) throws RemoteException {
        Quarto q = buscarQuarto(numeroQuarto);
        if (q != null) {
            q.setDisponivel(disponivel);
            System.out.println("Disponibilidade do Quarto " + numeroQuarto + " atualizada para " + disponivel + ".");
            return true;
        }
        System.out.println("Erro: Quarto " + numeroQuarto + " não encontrado para atualizar disponibilidade.");
        return false;
    }
}