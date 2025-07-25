package com.hotelaria.sistema_hotelaria_api.controller;

import com.hotelaria.sistema_hotelaria_api.model.Quarto;
import com.hotelaria.sistema_hotelaria_api.service.QuartoServiceImpl; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController 
@RequestMapping("/api/quartos") 
public class QuartoController {

    private final QuartoServiceImpl quartoService; // Injeta o serviço de quarto

    @Autowired // Realiza a injeção de dependência do QuartoServiceImpl
    public QuartoController(QuartoServiceImpl quartoService) {
        this.quartoService = quartoService;
    }

    // Endpoint para listar todos os quartos disponíveis
    // GET /api/quartos/disponiveis
    @GetMapping("/disponiveis")
    public ResponseEntity<List<Quarto>> listarQuartosDisponiveis() {
        List<Quarto> quartosDisponiveis = quartoService.listarQuartosDisponiveis();
        return ResponseEntity.ok(quartosDisponiveis); 
    }

    // Endpoint para buscar um quarto pelo número
    // GET /api/quartos/{numero}
    @GetMapping("/{numero}")
    public ResponseEntity<Quarto> buscarQuarto(@PathVariable int numero) {
        Quarto quarto = quartoService.buscarQuarto(numero);
        if (quarto != null) {
            return ResponseEntity.ok(quarto); 
        }
        return ResponseEntity.notFound().build(); 
    }

    // Endpoint para adicionar um novo quarto
    // POST /api/quartos
    @PostMapping
    public ResponseEntity<Quarto> adicionarQuarto(@RequestBody Quarto quarto) {
        if (quartoService.adicionarQuarto(quarto)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(quarto);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    // Endpoint para remover um quarto pelo número
    // DELETE /api/quartos/{numero}
    @DeleteMapping("/{numero}")
    public ResponseEntity<Void> removerQuarto(@PathVariable int numero) {
        if (quartoService.removerQuarto(numero)) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.notFound().build(); 
    }

    // Endpoint para atualizar a disponibilidade de um quarto
    // PUT /api/quartos/{numero}/disponibilidade
    @PutMapping("/{numero}/disponibilidade")
    public ResponseEntity<Void> atualizarDisponibilidadeQuarto(@PathVariable int numero,
                                                               @RequestParam boolean disponivel) {
        if (quartoService.atualizarDisponibilidadeQuarto(numero, disponivel)) {
            return ResponseEntity.ok().build(); 
        }
        return ResponseEntity.notFound().build();
    }
}