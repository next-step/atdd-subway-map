package subway.response;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class StationAcceptanceTestUtils {

    public static ExtractableResponse<Response> createStationResponse(Map<String, String> param) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().post("/stations")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> showStationResponse() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all().extract();
    }
}
