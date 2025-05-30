//import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // ### TESTES ###
        // Instanciar serviços
        ServicoQuarto servicoQuarto = new ServicoQuarto();
        ServicoReserva servicoReserva = new ServicoReserva(servicoQuarto);

        // Criar e adicionar quartos
        servicoQuarto.adicionarQuarto(new Quarto(101, "Simples", 2, 100.0, true));
        servicoQuarto.adicionarQuarto(new Quarto(102, "Duplo", 3, 150.0, true));
        servicoQuarto.adicionarQuarto(new Quarto(103, "Suíte", 4, 300.0, true));
        servicoQuarto.adicionarQuarto(new Quarto(101, "Repetido", 1, 80.0, true)); // Teste erro número duplicado

        // Buscar quarto
        System.out.println("Buscar Quarto 102: " + servicoQuarto.buscarQuarto(102));

        // Listar quartos disponíveis
        System.out.println("\nQuartos disponíveis:");
        for (Quarto q : servicoQuarto.listarQuartosDisponiveis()) {
            System.out.println(q);
        }

        // Fazer reservas
        Reserva r1 = new Reserva("11111111111", "11111111111", 101, "01/06/2025", "03/06/2025", 200.0, "");
        Reserva r2 = new Reserva("22222222222", "22222222222", 102, "02/06/2025", "04/06/2025", 300.0, "");
        Reserva r3 = new Reserva("33333333333", "33333333333", 103, "03/06/2025", "06/06/2025", 900.0, "");

        servicoReserva.fazerReserva(r1);
        servicoReserva.fazerReserva(r2);
        servicoReserva.fazerReserva(r3);

        // Tentar reservar quarto já ocupado
        Reserva r4 = new Reserva("44444444444", "44444444444", 101, "05/06/2025", "06/06/2025", 100.0, "");
        servicoReserva.fazerReserva(r4);

        // Listar todas reservas
        System.out.println("\nTodas as reservas:");
        for (Reserva r : servicoReserva.listarTodasReservas()) {
            System.out.println(r);
        }

        // Cancelar uma reserva
        System.out.println("\nCancelando a reserva do quarto 102...");
        servicoReserva.cancelarReserva(r2.getIdReserva());

        // Listar novamente após o cancelamento
        System.out.println("\nReservas após cancelamento:");
        for (Reserva r : servicoReserva.listarTodasReservas()) {
            System.out.println(r);
        }

        // Verificar se o quarto 102 está disponível novamente
        System.out.println("\nQuartos disponíveis após cancelamento:");
        for (Quarto q : servicoQuarto.listarQuartosDisponiveis()) {
            System.out.println(q);
        }

        // Listar reservas por hóspede
        System.out.println("\nReservas do hóspede 11111111111:");
        for (Reserva r : servicoReserva.listarReservasPorHospede("11111111111")) {
            System.out.println(r);
        }
    }
}


