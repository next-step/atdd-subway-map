package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineAcceptanceUtil {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color) {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("color", color);
        params.put("name", name);

        // when
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String location, String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("color", color);
        params.put("name", name);

        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .put(location)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_모든_노선_조회_요청() {
        return RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_특정_노선_조회_요청(String location) {
        return RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(location)
                .then().log().all().extract();
    }
}
