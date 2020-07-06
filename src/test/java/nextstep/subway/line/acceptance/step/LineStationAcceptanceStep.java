package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineStationAcceptanceStep {

    public static ExtractableResponse<Response> 지하철_노선에_지하철역을_등록한다(Long lineId, Long stationId, Long preStationId) {
        Map<String, String> params = getLineStationRequestParameterMap(preStationId, stationId);

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();
    }

    private static Map<String, String> getLineStationRequestParameterMap(Long preStationId, Long stationId) {
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", preStationId + "");
        params.put("stationId", stationId + "");
        params.put("distance", "4");
        params.put("duration", "2");
        return params;
    }
}
