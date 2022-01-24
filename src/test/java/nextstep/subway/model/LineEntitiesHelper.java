package nextstep.subway.model;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public final class LineEntitiesHelper {

    private static final String REQUEST_URI = "/lines";

    public static ExtractableResponse<Response> 노선_생성_요청(Line line) {
        return RestAssured.given().log().all()
                .body(newLine(line))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(REQUEST_URI)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when()
                .get(REQUEST_URI)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_단건_조회_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_수정_요청(Line line, String uri) {
        return RestAssured.given().log().all()
                .body(newLine(line))
                .when()
                .contentType(APPLICATION_JSON_VALUE)
                .put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_삭제_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    private static Map<String, String> newLine(Line line) {
        Map<String, String> params = new HashMap<>();
        params.put("name", line.name());
        params.put("color", line.getColor());
        return params;
    }
}
