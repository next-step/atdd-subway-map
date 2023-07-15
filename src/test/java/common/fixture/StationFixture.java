package common.fixture;

import common.utils.CustomRequest;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class StationFixture {

    public static final String GN_STATION = "강남역";
    public static final String YS_STATION = "역삼역";

    public static Response 역_검색_요청() {
        return CustomRequest.requestGet("/stations");
    }

    public static Response 역_생성_요청(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);
        return CustomRequest.requestPost("/stations", params);
    }

    public static Response 역_삭제_요청(Long stationId) {
        Map<String, Long> params = new HashMap<>();
        params.put("stationId", stationId);
        return CustomRequest.requestDelete("/stations/{stationId}", params);
    }


}
