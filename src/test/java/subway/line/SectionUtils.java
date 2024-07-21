package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class SectionUtils {

    public static ExtractableResponse<Response> 지하철구간_생성(Long id, Long upStationId, Long downStationId, Long distance) {
        Map<String, Object> params = createSectionParams(upStationId, downStationId, distance);

        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/" + id + "/sections")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철구간_삭제(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
            .when().delete("/lines/" + lineId + "/sections?stationId=" + stationId)
            .then().log().all()
            .extract();
    }

    private static Map<String, Object> createSectionParams(Long upStationId, Long downStationId, Long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

}
