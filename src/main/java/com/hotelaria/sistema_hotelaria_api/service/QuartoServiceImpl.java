package com.hotelaria.sistema_hotelaria_api.service;

import com.hotelaria.sistema_hotelaria_api.model.Quarto;
import com.hotelaria.sistema_hotelaria_api.util.JsonUtil;
import org.springframework.stereotype.Service; 
import java.util.ArrayList;
import java.util.List;

@Service
public class QuartoServiceImpl { 
    private List<Quarto> quartos = new ArrayList<>();

    public QuartoServiceImpl() {
        // Carregar os quartos do arquivo JSON ao inicializar o serviço
        this.quartos = JsonUtil.carregarQuartos();
        // Adicionar quartos iniciais apenas se o arquivo estiver vazio
        if (this.quartos.isEmpty()) {
            inicializarQuartosPadrao();
        }
    }

    private void inicializarQuartosPadrao() {
        System.out.println("Inicializando dados de quartos...");
        adicionarQuarto(new Quarto(101, "Standard", 2, 150.00, true));
        adicionarQuarto(new Quarto(102, "Standard", 2, 150.00, true));
        adicionarQuarto(new Quarto(201, "Luxo", 3, 250.00, true));
        adicionarQuarto(new Quarto(202, "Luxo", 3, 250.00, true));
        adicionarQuarto(new Quarto(301, "Suíte", 4, 400.00, true));
        System.out.println("Dados de quartos inicializados.");
        JsonUtil.salvarQuartos(this.quartos);
    }

    public boolean adicionarQuarto(Quarto quarto) { 
        for (Quarto q : quartos) {
            if (q.getNumero() == quarto.getNumero()) {
                System.out.println("Erro: Já existe um quarto com o número " + quarto.getNumero());
                return false;
            }
        }
        quartos.add(quarto);
        System.out.println("Quarto " + quarto.getNumero() + " adicionado.");
        JsonUtil.salvarQuartos(quartos); // Salvar após adicionar
        return true;
    }

    public boolean removerQuarto(int numero) { 
        boolean removed = quartos.removeIf(q -> q.getNumero() == numero);
        if (removed) {
            System.out.println("Quarto " + numero + " removido.");
            JsonUtil.salvarQuartos(quartos); // Salvar após remover
        } else {
            System.out.println("Erro: Quarto " + numero + " não encontrado para remoção.");
        }
        return removed;
    }

    public Quarto buscarQuarto(int numero) { 
        for (Quarto q : quartos) {
            if (q.getNumero() == numero) {
                return q;
            }
        }
        System.out.println("Quarto " + numero + " não encontrado.");
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
            System.out.println("Disponibilidade do Quarto " + numeroQuarto + " atualizada para " + disponivel + ".");
            JsonUtil.salvarQuartos(quartos); // Salvar após atualizar
            return true;
        }
        System.out.println("Erro: Quarto " + numeroQuarto + " não encontrado para atualizar disponibilidade.");
        return false;
    }
}