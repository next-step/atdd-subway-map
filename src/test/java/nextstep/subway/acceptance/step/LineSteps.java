package nextstep.subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class LineSteps {

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> line) {

        return RestAssured
                .given().log().all()
                .body(line)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청() {
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines/{id}", id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Long id, Map<String, String> line) {
        return RestAssured
                .given().log().all()
                .body(line)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(Long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{id}", id)
                .then().log().all().extract();
    }
}
