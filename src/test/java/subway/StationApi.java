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
    public static final String STATION_ID_KEY = "id";

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

    public static void deleteStation(Long stationId) {
        given()
                .pathParam(STATION_ID_KEY, stationId)
        .when()
                .delete("/stations/{id}");
    }

    public static Long getStationId(MockStation station) {
        for (Map<String, Object> stationIdAndName : getStationsIdAndName()) {
            if (station.name().equals(stationIdAndName.get(STATION_NAME_KEY))) {
                return Long.valueOf(String.valueOf(stationIdAndName.get(STATION_ID_KEY)));
            }
        }
        return null;
    }

    private static List<Map<String, Object>> getStationsIdAndName() {
        return when()
                    .get("/stations")
                .then()
                    .extract()
                    .jsonPath().get();
    }
}
