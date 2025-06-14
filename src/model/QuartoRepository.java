package src.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class QuartoRepository {
    private static final String FILE_PATH = "data/quartos.json";

    private List<Quarto> quartos;
    private final ObjectMapper mapper = new ObjectMapper();

    public QuartoRepository() throws Exception {
        loadQuartos();
    }

    private void loadQuartos() throws Exception {
        File file = new File(FILE_PATH);
        quartos = mapper.readValue(file, new TypeReference<List<Quarto>>() {
        });
    }

    public void saveQuartos() throws Exception {
        File file = new File(FILE_PATH);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, quartos);
    }

    public Optional<Quarto> buscarPorNumero(int numero) {
        return quartos.stream()
                .filter(q -> q.getNumero() == numero)
                .findFirst();
    }

    public boolean verificarDisponibilidade(int numero) {
        return buscarPorNumero(numero)
                .map(q -> !q.isOcupado())
                .orElse(false);
    }

    public boolean ocuparQuarto(int numero) throws Exception {
        Optional<Quarto> quartoOpt = buscarPorNumero(numero);
        if (quartoOpt.isPresent()) {
            Quarto q = quartoOpt.get();
            if (!q.isOcupado()) {
                q.setOcupado(true);
                saveQuartos();
                return true;
            }
        }
        return false;
    }
}
