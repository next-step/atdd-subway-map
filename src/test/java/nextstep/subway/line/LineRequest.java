package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineRequest {
    private final static String LINE_PATH = "/lines";

    static Map<String, String> createLineBody(String name, String color) {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("color", color);
        return body;
    }

    static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> body) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(LINE_PATH)
                .then().log().all().extract();
    }

    static ExtractableResponse<Response> 지하철_노선_등록되어_있음(Map<String, String> body) {
        return 지하철_노선_생성_요청(body);
    }

    static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(LINE_PATH)
                .then().log().all().extract();
    }

    static ExtractableResponse<Response> 지하철_노선_조회_요청(Long lineId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(LINE_PATH + "/{id}", lineId)
                .then().log().all().extract();
    }

    static ExtractableResponse<Response> 지하철_노선_수정_요청(Long lineId, Map<String, String> body) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().put(LINE_PATH + "/{id}", lineId)
                .then().log().all().extract();
    }

    static ExtractableResponse<Response> 지하철_노선_제거_요청(Long lineId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(LINE_PATH + "/{id}", lineId)
                .then().log().all().extract();
    }
}
