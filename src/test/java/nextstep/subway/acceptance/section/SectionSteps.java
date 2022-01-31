package nextstep.subway.acceptance.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class SectionSteps {

    private static final String URI = "/lines/%s/sections";

    public static ExtractableResponse<Response> 구간_등록_요청(String lineId, String upStationId, String downStationId, String distance) {
        var params = Map.of(
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", distance
        );

        return RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format(URI, lineId))
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 구간_제거_요청(String lineId, String newDownStationId) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .queryParams("stationId",newDownStationId)
                .delete(String.format(URI, lineId))
                .then()
                .extract();
    }
}
