package src.main.java.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import src.main.java.model.Reserva;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private static final String DATA_FILE = "data/reservas.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void salvarReservas(List<Reserva> reservas) {
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            gson.toJson(reservas, writer);
            System.out.println("Reservas salvas em: " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("Erro ao salvar reservas: " + e.getMessage());
        }
    }

    public static List<Reserva> carregarReservas() {
        try (FileReader reader = new FileReader(DATA_FILE)) {
            Type listType = new TypeToken<List<Reserva>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
