package src.main.java.test; 

import src.main.java.io.ReservaArrayOutputStream; 
import src.main.java.io.ReservaArrayInputStream;  
import src.main.java.model.Reserva;               

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class TesteFileStreams {

    private static final String NOME_ARQUIVO = "reservas.dat";

    public static void main(String[] args) {
        System.out.println("--- Iniciando Teste com FileOutputStream e FileInputStream ---");

        Reserva[] reservasParaEscrever = GeradorDeReservas.criarReservasDeTeste(5); // 5 Reservas

        // --- PARTE 1: Escrever as Reservas para um arquivo (FileOutputStream) ---
        System.out.println("\n--- Escrevendo Reservas para o arquivo: " + NOME_ARQUIVO + " ---");
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ReservaArrayOutputStream resOut = new ReservaArrayOutputStream(fos, reservasParaEscrever, reservasParaEscrever.length)) {

            // Para forçar o ReservaArrayOutputStream a escrever todos os bytes,
            // podemos chamar 'write(int b)' repetidamente ou, melhor, se tivéssemos um 'writeAll()'.
            // Como o `write(int b)` gerencia o buffer interno, basta 'puxar' o suficiente.
            // Para garantir que todas as reservas sejam processadas, vamos chamar write() um número de vezes
            // que cubra a possível quantidade de bytes gerados.
            // Uma forma mais elegante seria que o ReservaArrayOutputStream.close() implicitamente
            // garantisse a escrita de tudo, ou um método 'writeAll()' fosse adicionado.

            // Por agora, vamos simular lendo bytes do próprio stream para forçar a escrita.
            // Esta é uma maneira não convencional, mas funcional com o seu design de `write(int b)`.
            // Um OutputStream é geralmente "empurrado" (pushed) com bytes.
            // Se o seu `write(int b)` prepara o próximo objeto, ele precisa ser "puxado"
            // chamando `write(int b)` até que ele não tenha mais bytes a entregar para o DataOutputStream interno.
            
            // Uma solução mais simples: já que o construtor recebe todas as reservas,
            // podemos fazer o flush no final. Mas o 'write(int b)' ainda precisa ser chamado.

            // Vamos usar um loop longo para garantir que todas as reservas são processadas e escritas.
            for (int i = 0; i < 2000; i++) { // Número grande para cobrir todas as reservas
                try {
                    resOut.write(0); // O valor do byte não importa
                } catch (IOException e) {
                    // Quando não houver mais bytes internos para escrever, pode lançar exceção ou parar.
                    break;
                }
            }

            System.out.println("Reservas escritas para o arquivo.");

        } catch (IOException e) {
            System.err.println("Erro ao escrever Reservas para o arquivo: " + e.getMessage());
            e.printStackTrace();
        }

        // --- PARTE 2: Ler as Reservas de um arquivo (FileInputStream) ---
        System.out.println("\n--- Lendo Reservas do arquivo: " + NOME_ARQUIVO + " ---");
        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ReservaArrayInputStream resIn = new ReservaArrayInputStream(fis)) {

            Reserva reservaLida;
            int contador = 0;
            // O loop para ler Reservas é mais direto usando o método readReserva()
            while ((reservaLida = resIn.readReserva()) != null) {
                contador++;
                System.out.println("Reserva lida #" + contador + ": " + reservaLida);
            }
            System.out.println("Total de " + contador + " Reservas lidas do arquivo.");

        } catch (IOException e) {
            System.err.println("Erro ao ler Reservas do arquivo: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("--- Teste com FileOutputStream e FileInputStream Finalizado ---");
    }
}