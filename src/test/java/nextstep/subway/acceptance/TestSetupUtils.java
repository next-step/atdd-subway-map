package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class TestSetupUtils {

    public static ExtractableResponse<Response> 지하철_노선_생성(String name, String color,
                                                          String upStationName, String downStationName) {

        long upStationId = 지하철_역_생성(upStationName)
                .jsonPath()
                .getLong("id");

        long downStationId = 지하철_역_생성(downStationName)
                .jsonPath()
                .getLong("id");

        return RestAssured.given().log().all()
                .body(Map.of("name", name,
                        "color", color,
                        "upStationId", upStationId,
                        "downStationId", downStationId,
                        "distance", 10))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_역_생성(String name) {
        return RestAssured.given().log().all()
                .body(Map.of("name", name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }
}
