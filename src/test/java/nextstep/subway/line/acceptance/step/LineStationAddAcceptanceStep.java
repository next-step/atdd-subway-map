package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LineStationAddAcceptanceStep {
    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(Long lineId, Long preStationId, Long stationId, Integer distance, Integer duration) {
        Map<String, String> params = 지하철_노선에_지하철역_등록_요청값(preStationId, stationId, distance, duration);

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();
    }

    public static Map<String, String> 지하철_노선에_지하철역_등록_요청값(Long preStationId, Long stationId, Integer distance, Integer duration) {
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", (Objects.nonNull(preStationId) ? String.valueOf(preStationId) : null));
        params.put("stationId", String.valueOf(stationId));
        params.put("distance", String.valueOf(distance));
        params.put("duration",String.valueOf(duration));
        return params;
    }
}
