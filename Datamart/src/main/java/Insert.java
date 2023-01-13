import Model.Event;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Insert {
    static ArrayList<Event> eventList = new ArrayList<>();
    static List<Event> filteredTamaxEventList = new ArrayList<>();
    static List<Event> filteredTaminEventList = new ArrayList<>();


    public static void processFile(Path file) throws IOException, SQLException {
        String fileContent = new String(Files.readAllBytes(file));
        String[] events = fileContent.split("\n");
        for (String event : events) {
            if (event.isEmpty()) {
                continue;
            }
            String[] values = event.split(",");
            String date = values[0].substring(9, 13) + values[0].substring(14,16) + values[0].substring(17,19);
            String time = values[0].substring(20,28);
            String place = values[2].substring(6);
            String station = values[1].substring(9, 14);
            float temperature_max = Float.parseFloat(values[3].substring(8));
            float temperature_min = Float.parseFloat(values[4].substring(8).replace("}", ""));
            Event event1 = new Event(date, time, place, station, temperature_max, temperature_min);
            eventList.add(event1);
        }

        Map<String, Optional<Event>> groupedEvents2 = eventList.stream()
                .collect(Collectors.groupingBy(
                        Event::getDate,
                        Collectors.minBy(Comparator.comparingDouble(Event::getTamin))));

        filteredTaminEventList = groupedEvents2.values().stream().flatMap(Optional::stream).collect(Collectors.toList());

        Map<String, Optional<Event>> groupedEvents = eventList.stream()
                .collect(Collectors.groupingBy(
                        Event::getDate,
                        Collectors.maxBy(Comparator.comparingDouble(Event::getTamax))));

        filteredTamaxEventList = groupedEvents.values().stream().flatMap(Optional::stream).collect(Collectors.toList());

        for (int i = 0; i < filteredTamaxEventList.size(); i++) {
            String insertMaxTempSQL = "INSERT INTO temperaturas_maximas (fecha, hora, lugar, estacion, valor) VALUES (?, ?, ?, ?, ?) ON CONFLICT (fecha) DO NOTHING";
            String insertMinTempSQL = "INSERT INTO temperaturas_minimas (fecha, hora, lugar, estacion, valor) VALUES (?, ?, ?, ?, ?) ON CONFLICT (fecha) DO NOTHING";
            PreparedStatement insertMaxTempStmt = Datamart.connection.prepareStatement(insertMaxTempSQL);
            PreparedStatement insertMinTempStmt = Datamart.connection.prepareStatement(insertMinTempSQL);
            insertMaxTempStmt.setString(1, filteredTamaxEventList.get(i).getDate());
            insertMaxTempStmt.setString(2, filteredTamaxEventList.get(i).getTime());
            insertMaxTempStmt.setString(3, filteredTamaxEventList.get(i).getPlace());
            insertMaxTempStmt.setString(4, filteredTamaxEventList.get(i).getStation());
            insertMaxTempStmt.setFloat(5, filteredTamaxEventList.get(i).getTamax());
            insertMaxTempStmt.executeUpdate();
            insertMinTempStmt.setString(1, filteredTaminEventList.get(i).getDate());
            insertMinTempStmt.setString(2, filteredTaminEventList.get(i).getTime());
            insertMinTempStmt.setString(3, filteredTaminEventList.get(i).getPlace());
            insertMinTempStmt.setString(4, filteredTaminEventList.get(i).getStation());
            insertMinTempStmt.setFloat(5, filteredTaminEventList.get(i).getTamin());
            insertMinTempStmt.executeUpdate();
        }
    }
}
