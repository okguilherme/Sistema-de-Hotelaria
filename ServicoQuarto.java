import java.time.LocalDate;
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

    // Retorna uma cópia da lista de todos os quartos cadastrados
    public List<Quarto> listarTodosQuartos() {
        return new ArrayList<>(quartos);
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

    /**
     * Lista os quartos disponíveis em um intervalo de datas, considerando as reservas.
     * @param dataEntrada data de check-in desejada
     * @param dataSaida data de check-out desejada
     * @param reservas lista de reservas existentes para verificar conflitos
     * @return lista de quartos disponíveis no período informado
     */
    public List<Quarto> listarQuartosDisponiveis(LocalDate dataEntrada, LocalDate dataSaida, List<Reserva> reservas) {
        List<Quarto> disponiveis = new ArrayList<>();

        for (Quarto q : quartos) {
            boolean ocupado = false;

            // Para cada reserva, verifica se o quarto está reservado no período solicitado
            for (Reserva r : reservas) {
                if (r.getQuarto().getNumero() == q.getNumero()) {
                    // Verifica se há sobreposição de datas entre reserva e período solicitado
                    if (!(dataSaida.isBefore(r.getDataEntrada()) || dataEntrada.isAfter(r.getDataSaida()))) {
                        ocupado = true;
                        break; // Sai do loop, o quarto está ocupado
                    }
                }
            }

            // Se o quarto não está ocupado e está marcado como disponível, adiciona na lista
            if (!ocupado && q.isDisponivel()) {
                disponiveis.add(q);
            }
        }

        return disponiveis;
    }
}
