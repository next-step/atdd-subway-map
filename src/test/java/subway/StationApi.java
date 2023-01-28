package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class StationApi {
    public static final String STATION_NAME_KEY = "name";
    public static final String STATION_ID_KEY = "id";

    public static ExtractableResponse<Response> createStation(MockStation station) {
        Map<String, String> params = new HashMap<>();
        params.put(STATION_NAME_KEY, station.name());

        return given()
                    .log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations")
                .then()
                    .log().all()
                    .extract();
    }

    public static ExtractableResponse<Response> showStations() {
        return given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/stations")
                .then()
                    .log().all()
                    .extract();
    }

    public static ExtractableResponse<Response> deleteStation(Long stationId) {
        return given()
                    .log().all()
                    .pathParam(STATION_ID_KEY, stationId)
                .when()
                    .delete("/stations/{id}")
                .then()
                    .log().all()
                    .extract();
    }
}
