package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;

public class LineAcceptanceFactory {

    private static final String LINE_BASE_URL = "/lines";

    public static ExtractableResponse<Response> createLine(
            String name,
            String color,
            long upStationId,
            long downStationId,
            int distance
    ) {
        HashMap<String, Object> params = createLineHashMap(
                name,
                color,
                upStationId,
                downStationId,
                distance
        );

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(LINE_BASE_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> createFixtureLine() {
        HashMap<String, Object> params = createLineHashMap(
                LineNameConstraints.Line2,
                "bg-green-600",
                1,
                3,
                10
        );

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(LINE_BASE_URL)
                .then().log().all()
                .extract();
    }

    private static HashMap<String, Object> createLineHashMap(String name, String color, long upStationId, long downStationId, int distance) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    public static ExtractableResponse<Response> getAllLine() {
        return RestAssured
                .given().log().all()
                .when().get(LINE_BASE_URL)
                .then().log().all()
                .extract();
    }
}
