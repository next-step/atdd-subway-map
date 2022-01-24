package nextstep.subway.acceptance.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class CommonLineAcceptance {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        ExtractableResponse<Response> response;
        response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
                .when().post("/lines")
                .then().log().all().extract();
        return response;
    }

    public static Map<String, String> getParamsLineMap(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return params;
    }
}
