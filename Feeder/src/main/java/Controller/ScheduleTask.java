package Controller;

import Model.Directory;
import Model.Event;
import Model.FileClass;
import Model.Request;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

public class ScheduleTask extends TimerTask {
    private static Set<JsonObject> eventosAgregados = new HashSet<>();
    private static final Gson gson = new Gson();
    @Override
    public void run() {

        try {
            loadEvents();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonObject response;
        try {
            response = new JsonParser().parse(Request.getResponse()).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
            if (response == null) {
                System.out.println("error");
            }

        assert response != null;
        String url = response.get("datos").getAsString();

        JsonArray estaciones;
        try {
            estaciones = Request.getData(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Directory.createDatalakeDirectory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

            for (JsonElement estacion : estaciones) {
                if (-16.0 < estacion.getAsJsonObject().get("lon").getAsDouble() && estacion.getAsJsonObject().get("lon").getAsDouble() < -15.0 && 27.5 < estacion.getAsJsonObject().get("lat").getAsDouble() && estacion.getAsJsonObject().get("lat").getAsDouble() < 28.4) {
                    JsonObject evento = Event.createEvent(estacion);
                    if (!eventosAgregados.contains(evento)) {
                        eventosAgregados.add(evento);
                        String fechaEvento = evento.get("fint").getAsString().substring(0, 4) + evento.get("fint").getAsString().substring(5, 7) + evento.get("fint").getAsString().substring(8, 10);
                        Path file = Paths.get(Directory.DATALAKE_DIRECTORY, fechaEvento + ".events");
                        try {
                            FileClass.createFileIfNotExists(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            FileClass.addEventToFile(file, evento);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            saveEvents();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }

        private static void loadEvents () throws IOException {
            Path file = Paths.get(Directory.DATALAKE_DIRECTORY, "events.json");
            if (!Files.exists(file)) {
                System.out.println("error");
            }
            try (BufferedReader reader = Files.newBufferedReader(file)) {
                Type type = new TypeToken<Set<JsonObject>>() {
                }.getType();
                eventosAgregados = gson.fromJson(reader, type);
            }
        }

        private static void saveEvents () throws IOException {
            Path file = Paths.get(Directory.DATALAKE_DIRECTORY, "events.json");
            try (BufferedWriter writer = Files.newBufferedWriter(file)) {
                gson.toJson(eventosAgregados, writer);
            }
        }
}