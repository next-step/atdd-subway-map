package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;

public class StationAcceptanceFactory {

    private static final String STATION_BASE_URL = "/stations";

    public static ExtractableResponse<Response> createStation(String stationName) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(STATION_BASE_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getAllStations() {
        return RestAssured
                .given().log().all()
                .when().get(STATION_BASE_URL)
                .then().log().all()
                .extract();
    }
}
