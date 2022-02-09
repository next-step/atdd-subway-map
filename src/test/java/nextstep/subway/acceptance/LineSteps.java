package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class LineSteps {

    public static ExtractableResponse<Response> createLineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = makeLineParams(name, color, upStationId, downStationId, distance);

        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> getLineRequest(String uri) {
        return RestAssured.given().log().all()
            .when()
            .get(uri)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> getLinesRequest() {
        return RestAssured.given().log().all()
            .when()
            .get("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> updateLineRequest(String uri, String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = makeLineParams(name, color, upStationId, downStationId, distance);

        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when()
            .put(uri)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> deleteLineRequest(String uri) {
        return RestAssured.given().log().all()
            .when()
            .delete(uri)
            .then().log().all()
            .extract();
    }

    private static Map<String, String> makeLineParams(String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId.toString());
        params.put("downStationId",downStationId.toString());
        params.put("distance", String.valueOf(distance));

        return params;
    }
}
