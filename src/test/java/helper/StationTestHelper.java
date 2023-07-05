package helper;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationTestHelper {
    public static ExtractableResponse<Response> 지하철역을_생성한다(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        ExtractableResponse<Response> response =
                RestAssured.given()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/stations")
                        .then()
                        .log().all()
                        .extract();
        return response;
    }

    public static ExtractableResponse<Response> 지하철역을_삭제한다(String stationId) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/stations/" + stationId)
                .then()
                .log().all()
                .extract();
    }

    public static List<String> 지하철역_목록을_조회한다() {
        return RestAssured
                .given()
                .when().get("/stations")
                .then()
                .log().all()
                .extract()
                .jsonPath()
                .getList("name", String.class);
    }
}
