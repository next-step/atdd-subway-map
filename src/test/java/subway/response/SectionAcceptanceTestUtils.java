package subway.response;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SectionAcceptanceTestUtils {
    public static ExtractableResponse<Response> deleteSectionResponse(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .given().pathParam("id", lineId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationId)
                .when().delete("/lines/{id}/sections")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> createSectionResponse(Long lineId, Map<String, String> params) {
        return RestAssured.given().log().all()
                .given().pathParam("id", lineId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines/{id}/sections")
                .then().log().all().extract();
    }

    public static Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", 6 + "");
        return params;
    }
}
