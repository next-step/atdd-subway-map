package common.fixture.subway;

import common.utils.CustomRequest;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineFixture {

    public static final String SBD_LINE_NAME = "신분당선";
    public static final String BD_LINE_NAME = "분당선";
    public static final String RED_LINE_COLOR = "bg-red-600";
    public static final String GREEN_LINE_COLOR = "bg-green-600";
    public static final Long DISTANCE_10 = 10L;

    public static Response 노선_목록_조회_요청() {
        return CustomRequest.requestGet("/lines");
    }

    public static Response 노선_단일_조회_요청(Long lineId) {
        Map<String, Long> pathParams = new HashMap<>();
        pathParams.put("lineId", lineId);
        return CustomRequest.requestGet("/lines/{lineId}", pathParams);
    }

    public static Response 노선_생성_요청(String name, String color, Long upStationId, Long downStationId, Long distance) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("name", name);
        bodyParams.put("color", color);
        bodyParams.put("upStationId", upStationId);
        bodyParams.put("downStationId", downStationId);
        bodyParams.put("distance", distance);
        return CustomRequest.requestPost("/lines", bodyParams);
    }

    public static Response 노선_수정_요청(Long lineId, String name, String color) {
        Map<String, Long> pathParams = new HashMap<>();
        pathParams.put("lineId", lineId);
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("name", name);
        bodyParams.put("color", color);
        return CustomRequest.requestPut("/lines/{lineId}", pathParams, bodyParams);
    }

    public static Response 노선_삭제_요청(Long lineId) {
        Map<String, Long> pathParams = new HashMap<>();
        pathParams.put("lineId", lineId);
        return CustomRequest.requestDelete("/lines/{lineId}", pathParams);
    }

    public static List<String> 노선_이름_목록_반환(Response response) {
        return response.jsonPath().getList("name", String.class);
    }

    public static Long 노선_생성_ID_반환(Response response) {
        return response.jsonPath().getLong("id");
    }

}
