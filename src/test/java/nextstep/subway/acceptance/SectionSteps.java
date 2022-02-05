package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class SectionSteps {

    public static ExtractableResponse<Response> addSectionRequest(String lineURI, Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = makeSectionParams(upStationId, downStationId, distance);
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post(covertSectionURI(lineURI))
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> deleteSectionRequest(String lineURI, long stationId) {
        return RestAssured.given().log().all()
            .queryParam("stationId", stationId)
            .when()
            .delete(covertSectionURI(lineURI))
            .then().log().all()
            .extract();
    }

    private static String covertSectionURI(String lineURI) {
        return lineURI + "/sections";
    }

    private static Map<String, String> makeSectionParams(Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));
        return params;
    }
}
