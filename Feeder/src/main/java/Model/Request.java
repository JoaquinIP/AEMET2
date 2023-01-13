package Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;


public class Request {
    private static final String API_KEY = "Introduce your Api Key";
    private static final String ENDPOINT_URL = "https://opendata.aemet.es/opendata/api/observacion/convencional/todas";

    public static String getResponse() throws IOException {

        return Jsoup.connect(ENDPOINT_URL)
                .validateTLSCertificates(false)
                .timeout(600000)
                .ignoreContentType(true)
                .header("accept", "application/json")
                .header("api_key", API_KEY)
                .method(Connection.Method.GET)
                .maxBodySize(0).execute().body();
    }

    public static JsonArray getData(String url) throws IOException {
        return new JsonParser().parse(Jsoup.connect(url).validateTLSCertificates(false)
                .timeout(60000)
                .ignoreContentType(true)
                .header("accept", "application/json")
                .header("api_key", API_KEY)
                .method(Connection.Method.GET)
                .maxBodySize(0).execute().body()).getAsJsonArray();
    }
}
