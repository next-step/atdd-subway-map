package nextstep.subway.acceptance.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SectionAcceptanceTemplate {

    public static ExtractableResponse<Response> 구간_등록(String lineId, String downStationId, String upStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("downStationId", downStationId);
        params.put("upStationId", upStationId);
        params.put("distance", String.valueOf(distance));

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines/" + lineId + "/sections")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 구간_제거(String lineId, String downStationId) {
        return RestAssured.given().log().all()
            .queryParam("stationId", downStationId)
            .when().delete("/lines/" + lineId + "/sections")
            .then().log().all().extract();
    }

}
