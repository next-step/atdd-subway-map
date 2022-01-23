package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineTestStep {

    public static ExtractableResponse<Response> 지하철_노선_생성한다(String lineColor, String lineName) {
        Map<String, String> body = new HashMap<>();
        body.put("color", lineColor);
        body.put("name", lineName);

        return RestAssured
                .given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }
}
