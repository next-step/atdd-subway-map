package nextstep.subway.acceptance.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.station.StationRestAssuredTemplate.지하철역_생성;

public class LineStationAssuredTemplate {

    private static final String BASE_URI = "/lines";

    public static ExtractableResponse<Response> 노선_생성(String name, String color, String upStationName, String downStationName, int distance) {
        ExtractableResponse<Response> upStation = 지하철역_생성(upStationName);
        ExtractableResponse<Response> downStation = 지하철역_생성(downStationName);

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", Long.parseLong(upStation.jsonPath().getString("id")));
        params.put("downStationId", Long.parseLong(downStation.jsonPath().getString("id")));
        params.put("distance", distance);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post(BASE_URI)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 노선_수정(String id, String name, String color) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post(BASE_URI + "/" + id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 노선_삭제(String id) {
        return RestAssured
            .given().log().all()
            .when().delete(BASE_URI + "/" + id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 노선_목록_조회() {
        return RestAssured
            .given().log().all()
            .when().get(BASE_URI)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 노선_조회(String id) {
        return RestAssured
            .given().log().all()
            .when().get(BASE_URI + "/" + id)
            .then().log().all()
            .extract();
    }
}
