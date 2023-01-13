import Model.Directory;

import java.sql.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Datamart {
    public static Connection connection;

    public static void main(String[] args) throws SQLException, IOException {
        connection = DriverManager.getConnection("jdbc:sqlite:Datamart.db");
        dropTable();
        createTables();
        processFiles();
        connection.close();
    }

    private static void createTables() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS temperaturas_maximas (fecha DATE PRIMARY KEY, hora TIME, lugar TEXT, estacion TEXT, valor FLOAT);");
        statement.execute("CREATE TABLE IF NOT EXISTS temperaturas_minimas (fecha DATE PRIMARY KEY, hora TIME, lugar TEXT, estacion TEXT, valor FLOAT);");
    }

    private static void dropTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS temperaturas_maximas;\n");
        statement.execute("DROP TABLE IF EXISTS temperaturas_minimas;\n");
    }

    private static void processFiles() throws IOException {
        String directory = Directory.DATALAKE_DIRECTORY;
        Files.list(Paths.get(directory))
                .filter(Files::isRegularFile)
                .filter(file -> file.getFileName().toString().endsWith(".events"))
                .forEach(file -> {
                    try {
                        Insert.processFile(file);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                });
    }
}
