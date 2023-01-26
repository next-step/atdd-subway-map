package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class StationApi {
    public static final String STATION_NAME_KEY = "name";

    public static List<String> getStationNames() {
        return when()
                    .get("/stations")
                .then()
                    .extract()
                    .jsonPath().getList(STATION_NAME_KEY, String.class);
    }

    public static ExtractableResponse<Response> createStation(MockStation station) {
        Map<String, String> params = new HashMap<>();
        params.put(STATION_NAME_KEY, station.name());

        return given()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations")
                .then()
                    .extract();
    }

    public static void createStations(MockStation... stations) {
        for (MockStation station : stations) {
            createStation(station);
        }
    }
}
