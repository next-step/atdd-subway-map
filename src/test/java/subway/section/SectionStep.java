package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.LineRequest;

import java.util.HashMap;
import java.util.Map;

public class SectionStep {
    public static Map<String, String> 지하철역_요청_만들기(String name){
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }

    public static ExtractableResponse<Response> 지하철역_생성(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 라인생성(LineRequest req) {
        Map<String, Object> params = req.getParams();
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 구간생성(SectionRequest req, long id) {
        return RestAssured.given().log().all()
                .body(req)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections", id)
                .then().log().all()
                .extract();
    }
    public static ExtractableResponse<Response> 구간삭제( Long id, Long stationId) {
        return RestAssured.given().log().all()
                .queryParam("stationId", stationId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{id}/sections", id)
                .then().log().all()
                .extract();
    }
}
