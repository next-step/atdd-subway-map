package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;

import java.util.Map;

public class TestSetupUtils {

    public static long 지하철_노선_생성(String name, String color, String upStationName, String downStationName, int distance) {
        long upStationId = 지하철_역_생성(upStationName);
        long downStationId = 지하철_역_생성(downStationName);
        return 지하철_노선_생성(name, color, upStationId, downStationId, distance);
    }

    public static long 지하철_노선_생성(String name, String color, long upStationId, long downStationId, int distance) {
        return RestAssured.given().log().all()
                .body(Map.of("name", name,
                        "color", color,
                        "upStationId", upStationId,
                        "downStationId", downStationId,
                        "distance", distance))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract()
                .jsonPath()
                .getLong("id");
    }

    public static long 지하철_역_생성(String name) {
        return RestAssured.given().log().all()
                .body(Map.of("name", name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract()
                .jsonPath()
                .getLong("id");
    }
}
