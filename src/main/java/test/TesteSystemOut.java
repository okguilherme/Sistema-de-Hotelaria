package src.main.java.test;

import src.main.java.io.ReservaArrayOutputStream; 
import src.main.java.model.Reserva;                

import java.io.IOException;

public class TesteSystemOut {

    public static void main(String[] args) {
        System.out.println("--- Iniciando Teste com System.out ---");

        // 1. Criar algumas Reservas de teste
        Reserva[] reservas = GeradorDeReservas.criarReservasDeTeste(3); // 3 Reservas para teste

        // 2. Instanciar ReservaArrayOutputStream com System.out como destino
        try (ReservaArrayOutputStream resOut = new ReservaArrayOutputStream(System.out, reservas, reservas.length)) {
            
            // 3. Escrever os bytes das Reservas.
            // Loop para chamar o método write() várias vezes para enviar os bytes.
            // Para streams de objetos, geralmente um loop simples como este não é o mais ideal
            // pois 'write(int b)' libera apenas um byte por vez.
            // Para fins de demonstração, podemos tentar escrever uma quantidade de bytes grande.
            // O ideal seria que o ReservaArrayOutputStream tivesse um método 'write(byte[] b)'
            // ou que ele processasse todo o array de uma vez no construtor.

            // Devido à implementação do 'write(int b)' que processa uma reserva por vez em seu buffer interno,
            // e como não temos um método 'write(byte[] b, int off, int len)' fácil de usar aqui,
            // vamos simular uma escrita 'massiva' chamando write() várias vezes.
            // Isso geralmente não é como um OutputStream é usado com objetos, mas segue a estrutura base.

            System.out.println("Escrevendo bytes brutos para System.out (pode parecer ilegível):");
            // Uma maneira de garantir que todos os bytes são enviados é:
            // forçar a serialização de todas as reservas e tentar escrever seus bytes.
            // Alternativamente, o ReservaArrayOutputStream deveria ter um método 'writeAllReservas()'.
            // Mas, aderindo ao contrato de OutputStream, vamos tentar ler "o suficiente" bytes do stream.

            // Como o write(int b) do ReservaArrayOutputStream preenche um buffer interno e libera,
            // precisamos "puxar" os bytes dele chamando write().
            // Não há uma maneira direta de saber quantos bytes serão gerados por todas as reservas.
            // Uma estimativa grosseira ou um loop longo seria necessário.

            // Melhor forma para teste simples com 'write(int b)':
            // O próprio ReservaArrayOutputStream irá gerar os bytes quando write(int b) for chamado.
            // Nós precisamos "chamar" ele um número suficiente de vezes.
            
            // Estimativa de bytes por Reserva (apenas para ter uma ideia do loop)
            // String UTF: 2 bytes de tamanho + bytes da string (UTF-8 pode variar)
            // int: 4 bytes
            // double: 8 bytes
            // Digamos uns 200 bytes por Reserva (com cabeçalhos) * 3 reservas = 600 bytes.
            // Mas o internal currentReservaBytes é gerenciado pelo próprio stream.

            // O ReservaArrayOutputStream gerencia internamente qual Reserva está processando
            // e libera seus bytes um por um através de `write(int b)`.
            // Para que ele envie todas as reservas, basta chamar `write(int b)` repetidamente.
            // Como System.out não tem limite, podemos chamar até que ele pare de produzir.
            
            // Note: O System.out vai mostrar os bytes brutos, não formatados.
            // A visualização no console pode parecer "lixo" se não for texto puro.
            for (int i = 0; i < 1000; i++) { // Chame um número grande de vezes para garantir que todas as reservas sejam processadas
                try {
                    resOut.write(0); // O valor do byte não importa, pois estamos usando o buffer interno
                } catch (IOException e) {
                    // Pode ser que o stream interno tenha terminado de escrever.
                    break;
                }
            }
            System.out.println("\n--- Bytes brutos de Reservas escritos para System.out ---");

        } catch (IOException e) {
            System.err.println("Erro ao testar com System.out: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("--- Teste com System.out Finalizado ---");
    }
}