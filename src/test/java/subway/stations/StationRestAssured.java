package subway.stations;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class StationRestAssured {

    private static final String API_PATH = "/stations";

    public static ExtractableResponse<Response> createStation(Map<String, String> param) {
        return RestAssured.given()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(API_PATH)
                .then().log().all()
                .extract();
    }
}
