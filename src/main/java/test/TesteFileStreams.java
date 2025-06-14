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

        System.out.println("\n--- Escrevendo Reservas para o arquivo: " + NOME_ARQUIVO + " ---");
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ReservaArrayOutputStream resOut = new ReservaArrayOutputStream(fos, reservasParaEscrever, reservasParaEscrever.length)) {

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