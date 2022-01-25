package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationTestStep {

    public static ExtractableResponse<Response> 지하철역_생성하기(String stationName) {
        Map<String, String> body = new HashMap<>();
        body.put("name", stationName);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }
    public static ExtractableResponse<Response> 지하철역_목록_조회하기() {
        return RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_삭제하기(Long stationId) {
        return RestAssured.given().log().all()
                .when()
                .delete("/stations/" + stationId)
                .then().log().all()
                .extract();
    }
}
