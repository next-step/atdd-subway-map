package subway.response;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineAcceptanceTestUtils {
    public static ExtractableResponse<Response> deleteLineResponse(long id) {
        return RestAssured.given().log().all()
                .given().pathParam("id", id)
                .when().delete("/lines/{id}")
                .then().log().all().extract();
    }
    public static ExtractableResponse<Response> getLinesResponse() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> getLineResponse(long id) {
        return RestAssured.given().log().all()
                .given().pathParam("id", id)
                .when().get("/lines/{id}")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> createLineResponse(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> updateLineResponse(Map<String, String> param, Long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .given().pathParam("id", id)
                .when().put("/lines/{id}")
                .then().log().all().extract();
    }

    public static Map<String, Object> createLine(String lineName, String color, long upStationId, long downStationid, int distance) {
        Map<String, Object> lineParams = new HashMap<>();
        lineParams.put("name", lineName);
        lineParams.put("color", color);
        lineParams.put("upStationId", upStationId);
        lineParams.put("downStationId", downStationid);
        lineParams.put("distance", distance);
        return lineParams;
    }
}
