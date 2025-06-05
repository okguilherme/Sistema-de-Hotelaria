public class Main {
    public static void main(String[] args) {
        ServicoQuarto servicoQuarto = new ServicoQuarto();
        ServicoReserva servicoReserva = new ServicoReserva(servicoQuarto);

        // Criar quartos
        servicoQuarto.adicionarQuarto(new Quarto(101, "Simples", 2, 100.0, true));
        servicoQuarto.adicionarQuarto(new Quarto(102, "Duplo", 3, 150.0, true));
        servicoQuarto.adicionarQuarto(new Quarto(103, "Suíte", 4, 300.0, true));

        // Criar reservas
        Reserva r1 = new Reserva("11111111111", "11111111111", 101, "01/06/2025", "03/06/2025", 200.0, "");
        Reserva r2 = new Reserva("22222222222", "22222222222", 102, "02/06/2025", "04/06/2025", 300.0, "");
        Reserva r3 = new Reserva("33333333333", "33333333333", 103, "03/06/2025", "06/06/2025", 900.0, "");

        servicoReserva.fazerReserva(r1);
        servicoReserva.fazerReserva(r2);
        servicoReserva.fazerReserva(r3);

        // Salvar reservas em JSON
        ReservaInputStream reservaInputStream = new ReservaInputStream();
        reservaInputStream.salvarReservasComoJson(servicoReserva.getMapaReservas(), "reservas.json");

        // Salvar quartos em JSON
        // QuartoInputStream quartoInputStream = new QuartoInputStream();
        // quartoInputStream.salvarQuartosComoJson(servicoQuarto.getMapaQuartos(),
        // "quartos.json");
    }
}
