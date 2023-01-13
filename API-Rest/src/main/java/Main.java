import Controller.TemperatureApi;
import spark.Spark;

import static spark.Spark.port;

public class Main {
    public static void main(String[] args) {
        port(4567);
        Spark.get("/v1/places/with-max-temperature", TemperatureApi.getMaxTemperatures);

        Spark.get("/v1/places/with-min-temperature", TemperatureApi.getMinTemperatures);

    }
}
