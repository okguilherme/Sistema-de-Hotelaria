package com.hotelaria.sistema_hotelaria_api.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hotelaria.sistema_hotelaria_api.model.Reserva;
import com.hotelaria.sistema_hotelaria_api.model.Quarto; 

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private static final String RESERVAS_DATA_FILE = "data/reservas.json"; 
    private static final String QUARTOS_DATA_FILE = "data/quartos.json"; 
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // --- Métodos para Reservas ---
    public static void salvarReservas(List<Reserva> reservas) {
        try (FileWriter writer = new FileWriter(RESERVAS_DATA_FILE)) {
            gson.toJson(reservas, writer);
            System.out.println("Reservas salvas em: " + RESERVAS_DATA_FILE);
        } catch (IOException e) {
            System.err.println("Erro ao salvar reservas: " + e.getMessage());
        }
    }

    public static List<Reserva> carregarReservas() {
        try (FileReader reader = new FileReader(RESERVAS_DATA_FILE)) {
            Type listType = new TypeToken<List<Reserva>>() {}.getType();
            List<Reserva> loadedReservas = gson.fromJson(reader, listType);
            return loadedReservas != null ? loadedReservas : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Aviso: Arquivo de reservas não encontrado ou vazio. Iniciando com lista vazia.");
            return new ArrayList<>();
        }
    }

    // --- Novos Métodos para Quartos ---
    public static void salvarQuartos(List<Quarto> quartos) {
        try (FileWriter writer = new FileWriter(QUARTOS_DATA_FILE)) {
            gson.toJson(quartos, writer);
            System.out.println("Quartos salvos em: " + QUARTOS_DATA_FILE);
        } catch (IOException e) {
            System.err.println("Erro ao salvar quartos: " + e.getMessage());
        }
    }

    public static List<Quarto> carregarQuartos() {
        try (FileReader reader = new FileReader(QUARTOS_DATA_FILE)) {
            Type listType = new TypeToken<List<Quarto>>() {}.getType();
            List<Quarto> loadedQuartos = gson.fromJson(reader, listType);
            return loadedQuartos != null ? loadedQuartos : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Aviso: Arquivo de quartos não encontrado ou vazio. Iniciando com lista vazia.");
            return new ArrayList<>();
        }
    }
}