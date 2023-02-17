package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.springframework.http.MediaType;

public class LineSteps {

    public static ExtractableResponse<Response> 지하철_노선_생성(Map<String, String> params) {
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회() {
        return RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회(Long id) {
        return RestAssured.given().log().all()
            .when().get("/lines/{id}", id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정(Long id, Map<String, String> params) {
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/lines/{id}", id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제(Long id) {
        return RestAssured.given().log().all()
            .when().delete("/lines/{id}", id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_등록(Long lineId, Map<String, String> params) {
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{lineId}/sections", lineId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제(Long lineId, String stationId) {
        return RestAssured.given().log().all()
            .queryParam("stationId", stationId)
            .when().delete("/lines/{lineId}/sections", lineId)
            .then().log().all()
            .extract();
    }
}
