package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SectionSteps {

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(Long lineId, Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));

        return RestAssured
                .given().log().all()
                .body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
//                .when().post("/lines/" + lineId + "/sections")
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제_요청(Long lineId, Long stationId) {
        return RestAssured
                .given().log().all()
                .queryParam("stationId", stationId)
                .when().delete("/lines/{lineId}/sections",lineId)
                .then().log().all()
                .extract();
    }
}
