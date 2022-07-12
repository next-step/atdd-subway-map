package nextstep.subway.acceptance.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationSteps {

    public static ExtractableResponse<Response> 지하철역_생성(String stationName) {
        Map<String, String> param = new HashMap<>();
        param.put("name", stationName);
        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static Long 지하철역_생성_후_ID_반환(String stationName) {
        Map<String, String> param = new HashMap<>();
        param.put("name", stationName);
        return Long.valueOf(RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().jsonPath().getString("id"));
    }

}