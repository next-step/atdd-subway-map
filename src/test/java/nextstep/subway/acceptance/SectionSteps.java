package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SectionSteps {

    public static ExtractableResponse<Response> 구간_생성_요청(
            Long line_id, Long upStationId, Long downStationId, int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + line_id + "/sections")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 구간_제거_요청(String uri, Long stationId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(uri + "?stationId=" + stationId)
                .then().log().all()
                .extract();
    }

}
