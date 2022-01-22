package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationSteps {
    public static final String DEFAULT_PATH = "/stations";

    public static Map<String, String> getParams(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return params;
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(DEFAULT_PATH)
                .then().log().all()
                .extract();
    }
}
