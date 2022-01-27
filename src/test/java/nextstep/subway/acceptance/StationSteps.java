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
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(DEFAULT_PATH)
                .then().log().all()
                .extract();
    }

    public static void 지하철_역들이_생성되어_있다(int count) {
        for (int i = 1; i <= count; i++) {
            지하철역_생성_요청(i + "역");
        }
    }
}
