package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationTestFixtures {
    public static String 역_생성(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract().jsonPath().getString("id");
    }

    public static ExtractableResponse<Response> showStations() {
        return RestAssured
                .given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> removeStation(String id) {
        return RestAssured
                .given().log().all()
                .pathParam("id", id)
                .when()
                .delete("/stations/{id}")
                .then().log().all()
                .extract();
    }
}
