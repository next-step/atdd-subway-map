package nextstep.subway.acceptance.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SectionSteps {

    public static ExtractableResponse<Response> 구간_등록(Long 노선_ID, Long 구간_상행역_ID, Long 구간_하행역_ID, Long distance) {
        return RestAssured.given().log().all()
                .body(구간_등록_PARAM(구간_상행역_ID, 구간_하행역_ID, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + 노선_ID + "/sections")
                .then().log().all()
                .extract();
    }

    public static Long 구간_상행역_ID(ExtractableResponse<Response> 구간) {
        return Long.valueOf(구간.jsonPath().getString("upStationId"));
    }

    public static Long 구간_하행역_ID(ExtractableResponse<Response> 구간) {
        return Long.valueOf(구간.jsonPath().getString("downStationId"));
    }

    private static Map<String, String> 구간_등록_PARAM(Long upStationId, Long downStationId, Long distance) {
        Map<String, String> param = new HashMap<>();
        param.put("upStationId", String.valueOf(upStationId));
        param.put("downStationId", String.valueOf(downStationId));
        param.put("distance", String.valueOf(distance));
        return param;
    }

}
