package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

class RequestFixtures {

    public static ExtractableResponse<Response> 지하철역_생성_요청하기(Map<String, String> params) {
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_생성_요청하기(Map<String, String> params) {
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철구간_등록하기(Long lineId,
        Map<String, String> params) {
        String path = String.format("/lines/%d/sections", lineId);
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철구간_삭제하기(Long lineId, Long stationId) {
        String path = String.format("/lines/%d/sections", lineId);
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("stationId", stationId)
            .when().delete(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청하기() {
        return RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청하기(Long id) {
        String path = String.format("/stations/%d", id);
        return RestAssured.given().log().all()
            .when().delete(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회하기() {
        return RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회하기(String id) {
        String path = String.format("/lines/%s", id);
        return RestAssured.given().log().all()
            .when().get(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정하기(Long id,
        Map<String, String> updateParams) {
        String path = String.format("/lines/%d", id);
        return RestAssured.given().log().all()
            .body(updateParams)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제하기(Long id) {
        String path = String.format("/lines/%d", id);
        return RestAssured.given().log().all()
            .when().delete(path)
            .then().log().all()
            .extract();
    }
}
