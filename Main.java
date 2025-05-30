import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ServicoQuarto servicoQuarto = new ServicoQuarto();

        boolean sair = false;

        while (!sair) {
            System.out.println("\n=== Menu de Quartos ===");
            System.out.println("1. Adicionar Quarto");
            System.out.println("2. Remover Quarto");
            System.out.println("3. Buscar Quarto");
            System.out.println("4. Listar Todos os Quartos");
            System.out.println("5. Atualizar Disponibilidade");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = sc.nextInt();
            sc.nextLine(); // Consumir o ENTER após o número

            switch (opcao) {
                case 1:
                    System.out.print("Número do quarto: ");
                    int numero = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Tipo do quarto: ");
                    String tipo = sc.nextLine();
                    System.out.print("Capacidade: ");
                    int capacidade = sc.nextInt();
                    System.out.print("Preço por diária: ");
                    double preco = sc.nextDouble();
                    System.out.print("Disponível (true/false): ");
                    boolean disponivel = sc.nextBoolean();

                    Quarto novoQuarto = new Quarto(numero, tipo, capacidade, preco, disponivel);
                    if (servicoQuarto.adicionarQuarto(novoQuarto)) {
                        System.out.println("Quarto adicionado com sucesso!");
                    } else {
                        System.out.println("Erro: Quarto com esse número já existe.");
                    }
                    break;

                case 2:
                    System.out.print("Número do quarto a remover: ");
                    int numRemover = sc.nextInt();
                    if (servicoQuarto.removerQuarto(numRemover)) {
                        System.out.println("Quarto removido com sucesso!");
                    } else {
                        System.out.println("Quarto não encontrado.");
                    }
                    break;

                case 3:
                    System.out.print("Número do quarto a buscar: ");
                    int numBuscar = sc.nextInt();
                    Quarto q = servicoQuarto.buscarQuarto(numBuscar);
                    if (q != null) {
                        System.out.println("Quarto encontrado: " + q);
                    } else {
                        System.out.println("Quarto não encontrado.");
                    }
                    break;

                case 4:
                    System.out.println("Lista de todos os quartos:");
                    for (Quarto quarto : servicoQuarto.listarTodosQuartos()) {
                        System.out.println(quarto);
                    }
                    break;

                case 5:
                    System.out.print("Número do quarto para atualizar disponibilidade: ");
                    int numAtualizar = sc.nextInt();
                    System.out.print("Disponível? (true/false): ");
                    boolean disp = sc.nextBoolean();
                    if (servicoQuarto.atualizarDisponibilidadeQuarto(numAtualizar, disp)) {
                        System.out.println("Disponibilidade atualizada.");
                    } else {
                        System.out.println("Quarto não encontrado.");
                    }
                    break;

                case 0:
                    sair = true;
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida, tente novamente.");
                    break;
            }
        }

        sc.close();
    }
}
