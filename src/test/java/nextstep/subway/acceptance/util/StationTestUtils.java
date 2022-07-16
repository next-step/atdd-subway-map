package nextstep.subway.acceptance.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import org.springframework.http.MediaType;

public class StationTestUtils {

    public static ExtractableResponse<Response> createStationWithName(String stationName) {
        var requestBody = new HashMap<String, String>();
        requestBody.put("name", stationName);

        return RestAssured
                .given()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .extract();
    }

}
