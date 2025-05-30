import java.util.ArrayList;
import java.util.List;

public class ServicoQuarto {
    // Lista que armazena todos os quartos cadastrados no sistema
    private List<Quarto> quartos = new ArrayList<>();

    // Método para adicionar um novo quarto, evitando números duplicados
    public boolean adicionarQuarto(Quarto quarto) {
        // Verifica se já existe um quarto com o mesmo número
        for (Quarto q : quartos) {
            if (q.getNumero() == quarto.getNumero()) {
                System.out.println("Erro: Já existe um quarto com o número " + quarto.getNumero());
                return false;
            }
        }
        quartos.add(quarto); // Adiciona o quarto à lista
        return true;
    }

    // Remove o quarto com o número especificado da lista
    public boolean removerQuarto(int numero) {
        // Remove o quarto se o número bater e retorna true; senão, false
        return quartos.removeIf(q -> q.getNumero() == numero);
    }

    // Busca e retorna o quarto pelo número, ou null se não existir
    public Quarto buscarQuarto(int numero) {
        for (Quarto q : quartos) {
            if (q.getNumero() == numero) {
                return q;
            }
        }
        return null;
    }

    // Retorna uma lista com os quartos disponíveis
    public List<Quarto> listarQuartosDisponiveis() {
        List<Quarto> disponiveis = new ArrayList<>();
        for (Quarto q : quartos) {
            if (q.isDisponivel()) {
                disponiveis.add(q);
            }
        }
        return disponiveis;
    }

    // Atualiza o status de disponibilidade do quarto pelo número
    public boolean atualizarDisponibilidadeQuarto(int numeroQuarto, boolean disponivel) {
        Quarto q = buscarQuarto(numeroQuarto);
        if (q != null) {
            q.setDisponivel(disponivel);
            return true; // Sucesso
        }
        return false; // Quarto não encontrado
    }

}
