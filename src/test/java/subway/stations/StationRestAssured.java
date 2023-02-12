package subway.stations;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class StationRestAssured {

    private static final String apiPath = "/stations";

    public static ExtractableResponse<Response> createStation(Map<String, String> param) {
        return RestAssured.given()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(apiPath)
                .then().log().all()
                .extract();
    }
}
