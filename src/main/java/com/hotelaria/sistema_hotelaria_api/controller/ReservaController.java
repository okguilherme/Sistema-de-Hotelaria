package com.hotelaria.sistema_hotelaria_api.controller;

import com.hotelaria.sistema_hotelaria_api.model.Reserva;
import com.hotelaria.sistema_hotelaria_api.service.ReservaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta classe é um controlador REST
@RequestMapping("/api/reservas") // Define o prefixo de URL para todos os endpoints neste controlador
public class ReservaController {

    private final ReservaServiceImpl reservaService; // Injeta o serviço de reserva

    @Autowired // Realiza a injeção de dependência do ReservaServiceImpl
    public ReservaController(ReservaServiceImpl reservaService) {
        this.reservaService = reservaService;
    }

    // Endpoint para fazer uma nova reserva
    // POST /api/reservas
    @PostMapping
    public ResponseEntity<Reserva> fazerReserva(@RequestBody Reserva reserva) {
        Reserva novaReserva = reservaService.fazerReserva(reserva);
        if (novaReserva != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(novaReserva);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // Endpoint para cancelar uma reserva pelo ID
    // DELETE /api/reservas/{idReserva}
    @DeleteMapping("/{idReserva}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable String idReserva) {
        if (reservaService.cancelarReserva(idReserva)) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.notFound().build(); 
    }

    // Endpoint para buscar uma reserva pelo ID
    // GET /api/reservas/{idReserva}
    @GetMapping("/{idReserva}")
    public ResponseEntity<Reserva> buscarReserva(@PathVariable String idReserva) {
        Reserva reserva = reservaService.buscarReserva(idReserva);
        if (reserva != null) {
            return ResponseEntity.ok(reserva); 
        }
        return ResponseEntity.notFound().build(); 
    }

    // Endpoint para listar reservas por CPF do hóspede
    // GET /api/reservas/hospede/{cpf}
    @GetMapping("/hospede/{cpf}")
    public ResponseEntity<List<Reserva>> listarReservasPorHospede(@PathVariable String cpf) {
        List<Reserva> reservasDoHospede = reservaService.listarReservasPorHospede(cpf);
        if (!reservasDoHospede.isEmpty()) {
            return ResponseEntity.ok(reservasDoHospede); 
        }
        return ResponseEntity.notFound().build(); 
    }

    // Endpoint para listar todas as reservas
    // GET /api/reservas
    @GetMapping
    public ResponseEntity<List<Reserva>> listarTodasReservas() {
        List<Reserva> todasReservas = reservaService.listarTodasReservas();
        return ResponseEntity.ok(todasReservas); 
    }
}