package src.main.java.services;
import java.util.ArrayList;
import java.util.List;

import src.main.java.model.Quarto;

public class ServicoQuarto {
    private List<Quarto> quartos = new ArrayList<>();

    public boolean adicionarQuarto(Quarto quarto) {
        for (Quarto q : quartos) {
            if (q.getNumero() == quarto.getNumero()) {
                System.out.println("Erro: Já existe um quarto com o número " + quarto.getNumero());
                return false;
            }
        }
        quartos.add(quarto);
        return true;
    }

    public boolean removerQuarto(int numero) {
        return quartos.removeIf(q -> q.getNumero() == numero);
    }

    public Quarto buscarQuarto(int numero) {
        for (Quarto q : quartos) {
            if (q.getNumero() == numero) {
                return q;
            }
        }
        return null;
    }

    public List<Quarto> listarQuartosDisponiveis() {
        List<Quarto> disponiveis = new ArrayList<>();
        for (Quarto q : quartos) {
            if (q.isDisponivel()) {
                disponiveis.add(q);
            }
        }
        return disponiveis;
    }

    public boolean atualizarDisponibilidadeQuarto(int numeroQuarto, boolean disponivel) {
        Quarto q = buscarQuarto(numeroQuarto);
        if (q != null) {
            q.setDisponivel(disponivel);
            return true; 
        }
        return false; 
    }
}