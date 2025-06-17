package src.main.java.test; 

import src.main.java.io.ReservaArrayOutputStream; 
import src.main.java.io.ReservaArrayInputStream;  
import src.main.java.model.Reserva;               

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays; // Para comparar arrays de Reservas

public class TesteFileStreams {

    private static final String NOME_ARQUIVO = "reservas.dat";

    public static void main(String[] args) {
        System.out.println("--- Iniciando Teste com FileOutputStream e FileInputStream ---");

        Reserva[] reservasParaEscrever = GeradorDeReservas.criarReservasDeTeste(5); 

        System.out.println("\n--- Escrevendo Reservas para o arquivo: " + NOME_ARQUIVO + " ---");
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ReservaArrayOutputStream resOut = new ReservaArrayOutputStream(fos, reservasParaEscrever, reservasParaEscrever.length)) {

            resOut.writeAllReservas(); 

            System.out.println("Reservas escritas para o arquivo.");

        } catch (IOException e) {
            System.err.println("Erro ao escrever Reservas para o arquivo: " + e.getMessage());
            e.printStackTrace();
            return; 
        }

        System.out.println("\n--- Lendo Reservas do arquivo: " + NOME_ARQUIVO + " ---");
        Reserva[] reservasLidas = new Reserva[reservasParaEscrever.length]; // Para armazenar as lidas
        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ReservaArrayInputStream resIn = new ReservaArrayInputStream(fis)) {

            Reserva reservaLida;
            int contador = 0;
            while (contador < reservasParaEscrever.length && (reservaLida = resIn.readReserva()) != null) {
                reservasLidas[contador] = reservaLida; // Armazena a reserva lida
                contador++;
                System.out.println("Reserva lida #" + contador + ": " + reservaLida);
            }
            System.out.println("Total de " + contador + " Reservas lidas do arquivo.");

        } catch (IOException e) {
            System.err.println("Erro ao ler Reservas do arquivo: " + e.getMessage());
            e.printStackTrace();
        }

        // --- Verificação da integridade dos dados ---
        System.out.println("\n--- Verificando integridade dos dados ---");
        boolean todosIguais = true;
        if (reservasParaEscrever.length != reservasLidas.length) {
            todosIguais = false;
            System.err.println("Erro: Quantidade de reservas escritas (" + reservasParaEscrever.length + 
                               ") difere da quantidade de reservas lidas (" + reservasLidas.length + ").");
        } else {
            for (int i = 0; i < reservasParaEscrever.length; i++) {
                if (!reservasParaEscrever[i].equals(reservasLidas[i])) { 
                    todosIguais = false;
                    System.err.println("Diferença encontrada na reserva " + (i + 1) + ":");
                    System.err.println("  Original: " + reservasParaEscrever[i]);
                    System.err.println("  Lida:     " + reservasLidas[i]);
                }
            }
        }
        

        if (todosIguais) {
            System.out.println("Teste BEM SUCEDIDO: Todas as reservas foram serializadas e desserializadas corretamente para/de arquivo.");
        } else {
            System.err.println("Teste FALHOU: Houve diferenças ou problemas na leitura/escrita das reservas.");
        }

        System.out.println("--- Teste com FileOutputStream e FileInputStream Finalizado ---");
    }
}