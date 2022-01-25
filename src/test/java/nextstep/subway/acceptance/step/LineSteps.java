package nextstep.subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class LineSteps {
    static final String 이름 = "name";
    static final String 색상 = "color";
    static final String 상행종점 = "upStationId";
    static final String 하행종점 = "downStationId";
    static final String 거리 = "distance";

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color, Long upStationId, Long downStationId, int distance) {
        final Map<String, Object> line = createLineRequest(name, color, upStationId, downStationId, distance);

        return RestAssured
                .given().log().all()
                .body(line)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .log().all().extract();
    }

    public static Map<String, Object> createLineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        return Map.of(
                이름, name,
                색상, color,
                상행종점, upStationId,
                하행종점, downStationId,
                거리, distance
        );
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청() {
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(String uri) {
        return RestAssured
                .given().log().all()
                .when()
                .get(uri)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String uri, Map<String, String> line) {
        return RestAssured
                .given().log().all()
                .body(line)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(uri)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(String uri) {
        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all().extract();
    }
}
