package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationSteps {
    public static final String DEFAULT_PATH = "/stations";

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", name);

        return RestAssured.given().log().all()
                .body(params1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(DEFAULT_PATH)
                .then().log().all()
                .extract();
    }
}
