package nextstep.subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class LineClient {

    private static final String PATH = "/lines";

    public ExtractableResponse<Response> create(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(PATH)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> findAll() {
        return RestAssured.given().log().all()
                .when().get(PATH)
                .then().log().all().extract();
    }

    public ExtractableResponse<Response> findById(Long id) {
        return RestAssured.given().log().all()
                .when().get(PATH + "/" + id)
                .then().log().all().extract();
    }

    public void putById(Long id, Map<String, String> params) {
        RestAssured.given().log().all()
                .headers("Content-Type", "application/json; charset=UTF-8")
                .body(params)
                .when().put(PATH + "/" + id)
                .then().log().all();
    }

    public void deleteById(long id) {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(PATH + "/" + id)
                .then().log().all().extract();
    }

    public ExtractableResponse<Response> addSection(long lineId, Long downStationId, Long upStationId, int distance) {
        final var params = Map.of(
                "downStationId", String.valueOf(downStationId),
                "upStationId", String.valueOf(upStationId),
                "distance", distance
        );
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> deleteSection(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .param("stationId", stationId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }
}
