package nextstep.subway.acceptance.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SectionSteps {

    public static ExtractableResponse<Response> 지하철_구간_등록(Long lineId, Long upStaionId, Long downStationId, Long distance) {
        return RestAssured.given().log().all()
                .body(지하철_구간_등록_파라미터(upStaionId, downStationId, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    private static Map<String, String> 지하철_구간_등록_파라미터(Long upStaionId, Long downStationId, Long distance) {
        Map<String, String> param = new HashMap<>();
        param.put("upStaionId", String.valueOf(upStaionId));
        param.put("downStationId", String.valueOf(downStationId));
        param.put("distance", String.valueOf(distance));
        return param;
    }

}
