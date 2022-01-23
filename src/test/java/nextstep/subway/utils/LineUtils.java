package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineUtils {
    public static Map<String, Object> 지하철_노선_데이터_생성(String name, String color) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        return param;
    }

    public static ExtractableResponse<Response> 지하철_노선_생성요청(Map<String, Object> param) {
        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }
}
