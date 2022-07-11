package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticMethodUtil {

    public static ExtractableResponse<Response> createStationWithName(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given()
                .log()
                .all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .log()
                .all()
                .extract();
    }

    public static List<Long> extractIdsInListTypeResponse(ExtractableResponse response) {
        return response.jsonPath()
                .getList("id", Long.class);
    }

    public static Long extractIdInResponse(ExtractableResponse response) {
        return response.jsonPath()
                .getLong("id");
    }
}
