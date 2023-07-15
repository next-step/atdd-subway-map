package common.fixture.subway;

import common.utils.CustomRequest;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class SectionFixture {

    public static Response 구간_생성_요청(Long lineId, Long upStationId, Long downStationId, Long distance) {
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("lineId", lineId);
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("upStationId", upStationId);
        bodyParams.put("downStationId", downStationId);
        bodyParams.put("distance", distance);
        return CustomRequest.requestPost("/lines/{lineId}/sections", pathParams, bodyParams);
    }

    public static Response 구간_삭제_요청(Long lineId, Long stationId) {
        Map<String, Long> pathParams = new HashMap<>();
        pathParams.put("lineId", lineId);
        pathParams.put("stationId", stationId);
        return CustomRequest.requestDelete("/lines/{lineId}/sections?stationId={stationId}", pathParams);
    }
}
