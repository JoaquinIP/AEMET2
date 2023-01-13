package Model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Event {
    public static JsonObject createEvent(JsonElement estacion) {
        JsonObject evento = new JsonObject();
        //-16 < longitud < -15 y 27.5 < latitud < 28.4
        evento.addProperty("fint", estacion.getAsJsonObject().get("fint").getAsString());
        evento.addProperty("idema", estacion.getAsJsonObject().get("idema").getAsString());
        evento.addProperty("ubi", estacion.getAsJsonObject().get("ubi").getAsString());
        if (estacion.getAsJsonObject().has("tamax")) {
            evento.addProperty("tamax", estacion.getAsJsonObject().get("tamax").getAsDouble());
        }
        if (estacion.getAsJsonObject().has("tamin")) {
            evento.addProperty("tamin", estacion.getAsJsonObject().get("tamin").getAsDouble());
        }
        return evento;
    }
}