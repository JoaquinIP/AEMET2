package Model;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileClass {
    public static void createFileIfNotExists(Path file) throws IOException {
        if (!Files.exists(file)) {
            Files.createFile(file);
        }
    }

    public static void addEventToFile(Path file, JsonObject evento) throws IOException {
        String event = evento.toString() + System.lineSeparator();
        Files.write(file, event.getBytes(), StandardOpenOption.APPEND);
    }
}