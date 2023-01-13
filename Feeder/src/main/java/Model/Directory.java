package Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Directory {
    public static final String DATALAKE_DIRECTORY = "datalake";

    public static void createDatalakeDirectory() throws IOException {
        Path directory = Paths.get(DATALAKE_DIRECTORY);
        if (!Files.exists(directory)) {
            Files.createDirectory(directory);
        }
    }
}