package subway.station;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class StationApiClient {
    private static final String STATIONS_URL = "/stations";

    public ExtractableResponse<Response> createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    public ExtractableResponse<Response> getStations() {
        return RestAssured.given().log().all()
            .when().get(STATIONS_URL)
            .then().log().all()
            .extract();
    }

    public ExtractableResponse<Response> deleteStation(long stationId) {
        return RestAssured.given().log().all()
            .when().delete(STATIONS_URL + "/" + stationId)
            .then().log().all()
            .extract();
    }
}
