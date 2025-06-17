package src.main.java.test;

import src.main.java.io.ReservaArrayOutputStream; 
import src.main.java.model.Reserva;                

import java.io.IOException;

public class TesteSystemOut {

    public static void main(String[] args) {
        System.out.println("--- Iniciando Teste com System.out ---");

        // Criar algumas Reservas de teste
        Reserva[] reservas = GeradorDeReservas.criarReservasDeTeste(3); 

        // Instanciar ReservaArrayOutputStream com System.out como destino
        try (ReservaArrayOutputStream resOut = new ReservaArrayOutputStream(System.out, reservas, reservas.length)) {
            System.out.println("Escrevendo bytes brutos das Reservas para System.out (pode parecer ilegível, é esperado):");
            
            resOut.writeAllReservas();

            System.out.println("\n--- Bytes brutos de Reservas escritos para System.out ---");

        } catch (IOException e) {
            System.err.println("Erro ao testar com System.out: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("--- Teste com System.out Finalizado ---");
    }
}