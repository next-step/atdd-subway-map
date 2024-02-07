package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class SubwayLineApiRequester {
    public static ExtractableResponse<Response> getLinesList() {
        return RestAssured
            .given().log().all()
            .when()
            .get("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> createLines(String name, String color, Long upStationId,
        Long downStationId, Long distance) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId.toString());
        params.put("downStationId", downStationId.toString());
        params.put("distance", distance.toString());

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> getLines(Long id) {
        return RestAssured
            .given().log().all()
            .when()
            .get("/lines/" + id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> updateLines(Long id, String name, String color) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when()
            .put("/lines/" + id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> deleteLines(Long id) {
        return RestAssured
            .given().log().all()
            .when()
            .delete("/lines/" + id)
            .then().log().all()
            .extract();
    }

}
