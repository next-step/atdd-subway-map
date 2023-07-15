package common.fixture.subway;

import common.utils.CustomRequest;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StationFixture {

    public static final String GN_STATION = "강남역";
    public static final String YS_STATION = "역삼역";

    public static Response 역_목록_조회_요청() {
        return CustomRequest.requestGet("/stations");
    }

    public static Response 역_생성_요청(String stationName) {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("name", stationName);
        return CustomRequest.requestPost("/stations", bodyParams);
    }

    public static Response 역_삭제_요청(Long stationId) {
        Map<String, Long> pathParams = new HashMap<>();
        pathParams.put("stationId", stationId);
        return CustomRequest.requestDelete("/stations/{stationId}", pathParams);
    }

    public static List<String> 역_이름_목록_반환(Response response) {
        return response.jsonPath().getList("name", String.class);
    }

    public static Long 역_생성_ID_반환(Response response) {
        return response.jsonPath().getLong("id");
    }

}
