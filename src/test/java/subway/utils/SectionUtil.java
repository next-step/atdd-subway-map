package subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class SectionUtil {

    public static ExtractableResponse<Response> addSectionResponse(Long lineId, Long downStationId, Long newStationId, int distance) {
        return RestAssured
                .given().log().all().body(getAddParam(downStationId, newStationId, distance)).contentType(APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections", lineId)
                .then().log().all()
                .extract();
    }

    private static Map<String, Object> getAddParam(Long downStationId, Long newStationId, int distance) {
        Map<String, Object> param = new HashMap<>();
        param.put("upStationId", downStationId);
        param.put("downStationId", newStationId);
        param.put("distance", distance);
        return param;
    }
}
