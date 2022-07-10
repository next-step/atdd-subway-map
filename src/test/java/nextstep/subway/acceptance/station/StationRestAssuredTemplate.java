package nextstep.subway.acceptance.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationRestAssuredTemplate {
    public static ExtractableResponse<Response> 지하철역_삭제(String id) {
        return RestAssured.given().log().all()
            .when().delete("/stations/" + id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_생성(String stationName) {
        Map<String, String> station = new HashMap<>();
        station.put("name", stationName);

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(station)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_조회() {
        return RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract();
    }
}
