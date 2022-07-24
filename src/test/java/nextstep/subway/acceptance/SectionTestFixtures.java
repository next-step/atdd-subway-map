package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SectionTestFixtures {
    public static ExtractableResponse<Response>  구간_생성(String lineId, String upStationId, String downStationId, String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("downStationId", downStationId);
        params.put("upStationId", upStationId);
        params.put("distance", distance);

        return RestAssured
                .given().log().all().pathParam("lineId", lineId)
                .body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{lineId}/sections")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 구간_삭제(String lineId, String downStationId) {
        return RestAssured
                .given().log().all()
                .pathParam("lineId", lineId)
                .param("downStationId", downStationId)
                .when()
                .delete("/lines/{lineId}/sections")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 구간_조회() {
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines/sections")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 특정_구간_조회(String lineId) {
        return RestAssured
                .given().log().all()
                .when().pathParam("id", lineId)
                .get("lines/{id}/sections")
                .then().log().all()
                .extract();
    }
}
