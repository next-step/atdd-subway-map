package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class LineApi {

    private LineApi() {}

    public static final String LOCATION = "Location";

    public static final String LINE_ID_KEY = "id";
    public static final String LINE_NAME_KEY = "name";
    public static final String LINE_COLOR_KEY = "color";
    public static final String SECTION_UP_STATION_ID_KEY = "upStationId";
    public static final String SECTION_DOWN_STATION_ID_KEY = "downStationId";
    public static final String SECTION_DISTANCE_KEY = "distance";

    public static ExtractableResponse<Response> createLine(MockLine line, long upStationId, long downStationId, int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put(LINE_NAME_KEY, line.getName());
        params.put(LINE_COLOR_KEY, line.getColor());
        params.put(SECTION_UP_STATION_ID_KEY, upStationId);
        params.put(SECTION_DOWN_STATION_ID_KEY, downStationId);
        params.put(SECTION_DISTANCE_KEY, distance);

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

    public static ExtractableResponse<Response> showLine(Long lineId) {
        return given()
                    .log().all()
                    .pathParam(LINE_ID_KEY, lineId)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/lines/{"+LINE_ID_KEY+"}")
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
                    .put("/lines/{"+LINE_ID_KEY+"}")
                .then()
                    .log().all()
                    .extract();
    }

    public static ExtractableResponse<Response> deleteLine(Long lineId) {
        return given()
                    .log().all()
                    .pathParam(LINE_ID_KEY, lineId)
                .when()
                    .delete("/lines/{"+LINE_ID_KEY+"}")
                .then()
                    .log().all()
                    .extract();
    }
}
