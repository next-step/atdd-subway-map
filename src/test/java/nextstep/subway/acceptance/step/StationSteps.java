package nextstep.subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class StationSteps {
    public static ExtractableResponse<Response> 지하철_역_생성_요청(Map<String, String> station) {
        return RestAssured.given().log().all()
                .body(station)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }
}
