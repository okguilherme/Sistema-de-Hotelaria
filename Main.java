//import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Quarto quarto = new Quarto(101, "Casal", 2, 250.0, true);
        Quarto quarto2 = new Quarto(101, "Suite", 3, 99, false);
        Quarto quarto3 = new Quarto(2, "Solo", 1, 20, true);

        /*
         * System.out.println(quarto);
         * System.out.println(quarto.hashCode());
         * System.out.println(quarto2);
         * System.out.println(quarto2.hashCode());
         * System.out.println(quarto.equals(quarto2));
         */

        Hospede hospede1 = new Hospede("André", "12345", "99999", "andrefelipe");
        Hospede hospede2 = new Hospede("Guilherme", "12345", "88888", "guilhermeoliveira");
        Hospede hospede3 = new Hospede("Guilherme", "987", "777777", "ehocria");

        /*
         * System.out.println(hospede1);
         * System.out.println(hospede1.hashCode());
         * System.out.println(hospede2);
         * System.out.println(hospede2.hashCode());
         * System.out.println(hospede1.equals(hospede2));
         */

        // Reserva reserva1 = new Reserva(quarto, hospede1, LocalDate.of(2025, 5, 30),
        // LocalDate.of(2025, 5, 30));
        // Reserva reserva2 = new Reserva(quarto, hospede2, LocalDate.of(2025, 5, 30),
        // LocalDate.of(2025, 5, 31));
        // Reserva reserva3 = new Reserva(quarto3, hospede3, LocalDate.of(2025, 5, 30),
        // LocalDate.of(2025, 5, 31));

        /*
         * System.out.println(reserva1);
         * System.out.println(reserva1.hashCode());
         * System.out.println(reserva2);
         * System.out.println(reserva2.hashCode());
         * System.out.println(reserva1.equals(reserva2));
         */

        Set<Quarto> quartos = new HashSet<>();
        quartos.add(quarto);
        quartos.add(quarto2); // não será adicionado
        quartos.add(quarto3);
        System.out.println("Quartos no set: " + quartos.size()); // 1
        quartos.forEach(System.out::println);

        Set<Hospede> hospedes = new HashSet<>();
        hospedes.add(hospede1);
        hospedes.add(hospede2); // não será adicionado
        hospedes.add(hospede3);
        System.out.println("Hospedes no set: " + hospedes.size()); // 1
        hospedes.forEach(System.out::println);

        Set<Reserva> reservas = new HashSet<>();
        // reservas.add(reserva1);
        // reservas.add(reserva2); // não será adicionado
        // reservas.add(reserva3);
        System.out.println("Reservas no set: " + reservas.size()); // 1
        reservas.forEach(System.out::println);
    }
}
