package subway.service;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationAcceptanceService {

    public static ExtractableResponse<Response> createStation(String stationName) {

        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured
                .given().body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> showStations() {
        return RestAssured
                .when().get("/stations")
                .then().extract();

    }

    public static ExtractableResponse<Response> deleteStation(Long stationId) {
        return RestAssured
                .when().delete("/stations/{id}", stationId)
                .then().extract();
    }

}
