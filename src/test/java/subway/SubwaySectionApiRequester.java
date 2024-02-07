package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public final class SubwaySectionApiRequester {
    private SubwaySectionApiRequester() {
    }

    public static ExtractableResponse<Response> addSections(Long lineId, Long upStationId, Long downStationId, Long distance) {
        final Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId.toString());
        params.put("downStationId", downStationId.toString());
        params.put("distance", distance.toString());

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when()
            .post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> deleteSection(Long lineId, Long stationId) {
        return RestAssured
            .given().log().all()
            .when()
            .queryParam("stationId", stationId)
            .delete("/lines/"+ lineId + "/sections")
            .then().log().all()
            .extract();
    }

}
