package subway.line;

import static subway.acceptance.ResponseParser.getIdFromResponse;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class LineAcceptanceTestHelper {

    static ExtractableResponse<Response> 노선_생성_요청(HashMap<String, String> params) {
        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/lines")
                          .then().log().all()
                          .extract();
    }

    static HashMap<String, String> 노선_파라미터_생성() {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");
        return params;
    }

    static HashMap<String, String> 노선_파라미터_생성2() {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-green-600");
        params.put("upStationId", "5");
        params.put("downStationId", "20");
        params.put("distance", "50");
        return params;
    }

    public static ExtractableResponse<Response> 지하철역_조회_요청() {
        return RestAssured.given().log().all()
                          .when().get("/stations")
                          .then().log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/stations")
                          .then().log().all()
                          .extract();
    }

    static ExtractableResponse<Response> 노선목록_조회_요청() {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().get("/lines")
                          .then().log().all()
                          .extract();
    }

    static ExtractableResponse<Response> 노선_단건조회_요청(ExtractableResponse<Response> response) {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().get("/lines/" + getIdFromResponse(response))
                          .then().log().all()
                          .extract();
    }

    static ExtractableResponse<Response> 노선_수정_요청(HashMap<String, String> updateParam,
        Long id) {
        return RestAssured.given().log().all()
                          .body(updateParam)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().put("/lines/" + id)
                          .then().log().all()
                          .extract();
    }

    static HashMap<String, String> 노선수정_파라미터_생성() {
        HashMap<String, String> updateParam = new HashMap<>();
        updateParam.put("name", "다른분당선");
        updateParam.put("color", "bg-red-600");
        return updateParam;
    }

    static ExtractableResponse<Response> 노선_삭제_요청(Long id) {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().delete("/lines/" + id)
                          .then().log().all()
                          .extract();
    }
}
