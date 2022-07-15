package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.ResponseParser.getIdFromResponse;

public class StationRestAssuredTestSource {

    public static Long 역을생성함(final String stationName) {
        return getIdFromResponse(createStationRequest(stationName));
    }

    private static ExtractableResponse<Response> createStationRequest(final String stationName) {
        return RestAssured.given().log().all()
                .body(createStationParams(stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private static Map<String, Object> createStationParams(final String stationName) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", stationName);
        return params;
    }

}
