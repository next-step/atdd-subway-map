package subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationApiHelper {

    private StationApiHelper() {
    }

    public static ExtractableResponse<Response> callApiToGetStations() {
        return RestAssured.given().log().all()
                          .when().get("/stations")
                          .then().log().all()
                          .extract();
    }
    public static ExtractableResponse<Response> callApiToDeleteStations(Long stationId) {
        return RestAssured.given().log().all().pathParam("stationId", stationId)
                          .when().delete("/stations/{stationId}")
                          .then().log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> callApiToCreateStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/stations")
                          .then().log().all()
                          .extract();
    }
}
