package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class LineApi {

    public static final String LINE_ID_KEY = "id";
    public static final String LINE_NAME_KEY = "name";
    public static final String LINE_COLOR_KEY = "color";
    public static final String LINE_UP_STATION_ID_KEY = "upStationId";
    public static final String LINE_DOWN_STATION_ID_KEY = "downStationId";
    public static final String LINE_DISTANCE_KEY = "distance";

    public static ExtractableResponse<Response> createLine(MockLine line) {
        Map<String, Object> params = new HashMap<>();
        params.put(LINE_NAME_KEY, line.getName());
        params.put(LINE_COLOR_KEY, line.getColor());
        params.put(LINE_UP_STATION_ID_KEY, line.getUpStationId());
        params.put(LINE_DOWN_STATION_ID_KEY, line.getDownStationId());
        params.put(LINE_DISTANCE_KEY, line.getDistance());

        return given()
                    .log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines")
                .then()
                    .log().all()
                    .extract();
    }

    public static ExtractableResponse<Response> showLines() {
        return given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/lines")
                .then()
                    .log().all()
                    .extract();
    }

    public static ExtractableResponse<Response> updateLine(Long lineId, String name, String color) {
        Map<String, Object> params = new HashMap<>();
        params.put(LINE_NAME_KEY, name);
        params.put(LINE_COLOR_KEY, color);

        return given()
                    .log().all()
                    .pathParam(LINE_ID_KEY, lineId)
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .put("/lines/{id}")
                .then()
                    .log().all()
                    .extract();
    }

    public static ExtractableResponse<Response> deleteLine(Long lineId) {
        return given()
                    .log().all()
                    .pathParam(LINE_ID_KEY, lineId)
                .when()
                    .delete("/lines/{id}")
                .then()
                    .log().all()
                    .extract();
    }
}
