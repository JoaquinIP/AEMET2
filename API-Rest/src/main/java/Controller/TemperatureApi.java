package Controller;

import Model.Temperature;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TemperatureApi {

    public static Route getMaxTemperatures = (Request request, Response response) -> {
        String from = request.queryParams("from");
        String to = request.queryParams("to");

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:Datamart.db")) {
            String query = "SELECT lugar, fecha, MAX(valor) as valor FROM temperaturas_maximas WHERE fecha >= ? AND fecha <= ? GROUP BY fecha ORDER BY valor DESC";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, from);
                statement.setString(2, to);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Temperature> temperatures = new ArrayList<>();
                    while (resultSet.next()) {
                        temperatures.add(new Temperature(resultSet.getString("lugar"), resultSet.getString("fecha"), resultSet.getFloat("valor")));
                    }
                    response.type("application/json");
                    return new Gson().toJson(temperatures);
                }
            }
        }
    };

    public static Route getMinTemperatures = (Request request, Response response) -> {
        String from = request.queryParams("from");
        String to = request.queryParams("to");

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:Datamart.db")) {
            String query = "SELECT lugar, fecha, MIN(valor) as valor FROM temperaturas_minimas WHERE fecha >= ? AND fecha <= ? GROUP BY fecha ORDER BY valor ASC";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, from);
                statement.setString(2, to);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Temperature> temperatures = new ArrayList<>();
                    while (resultSet.next()) {
                        temperatures.add(new Temperature(resultSet.getString("lugar"), resultSet.getString("fecha"), resultSet.getFloat("valor")));
                    }
                    response.type("application/json");
                    return new Gson().toJson(temperatures);
                }
            }
        }
    };
}