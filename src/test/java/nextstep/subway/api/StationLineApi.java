package nextstep.subway.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class StationLineApi {

    public static ExtractableResponse<Response> 지하철_구간_생성(Long id, Long upStationId, Long downStationId, Integer distance) {
        Map<String, Object> params = new HashMap();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{id}/sections", id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제(Long id, Long stationId) {
        return RestAssured
            .given().log().all()
            .queryParam("stationId", stationId)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/lines/{id}/sections", id)
            .then().log().all()
            .extract();
    }



}
