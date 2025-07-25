package com.hotelaria.sistema_hotelaria_api;

import com.hotelaria.sistema_hotelaria_api.model.Quarto;
import com.hotelaria.sistema_hotelaria_api.service.QuartoServiceImpl;
import org.springframework.boot.CommandLineRunner; 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; 

@SpringBootApplication
public class SistemaHotelariaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaHotelariaApiApplication.class, args);
    }

    // Adiciona um CommandLineRunner para inicializar dados quando a aplicação inicia
    @Bean
    public CommandLineRunner initData(QuartoServiceImpl quartoService) {
        return args -> {
            System.out.println("Inicializando dados de quartos...");

            // Adicionar alguns quartos iniciais, se ainda não existirem
            if (quartoService.buscarQuarto(101) == null) {
                quartoService.adicionarQuarto(new Quarto(101, "Standard", 2, 150.00, true)); 
            }
            if (quartoService.buscarQuarto(102) == null) {
                quartoService.adicionarQuarto(new Quarto(102, "Standard", 2, 150.00, true)); 
            }
            if (quartoService.buscarQuarto(201) == null) {
                quartoService.adicionarQuarto(new Quarto(201, "Luxo", 3, 250.00, true)); 
            }
            if (quartoService.buscarQuarto(202) == null) {
                quartoService.adicionarQuarto(new Quarto(202, "Luxo", 3, 250.00, true)); 
            }
            if (quartoService.buscarQuarto(301) == null) {
                quartoService.adicionarQuarto(new Quarto(301, "Suíte", 4, 400.00, true));
            }

            System.out.println("Dados de quartos inicializados.");
            // As reservas são carregadas automaticamente pelo ReservaServiceImpl no construtor
        };
    }
}