import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ReservaInputStream {

    private ObjectMapper objectMapper;

    public ReservaInputStream() {
        this.objectMapper = new ObjectMapper();
    }

    public void salvarReservasComoJson(Map<String, Reserva> reservas, String caminhoArquivo) {
        try {
            // Aqui escrevemos o Map em JSON no arquivo
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(caminhoArquivo), reservas);
            System.out.println("Reservas salvas em: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao salvar JSON: " + e.getMessage());
        }
    }
}
