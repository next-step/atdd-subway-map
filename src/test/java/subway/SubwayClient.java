package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.Station;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayClient {
    public static Station 지하철역을_생성한다(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response.jsonPath().getObject(".", Station.class);
    }

    public static void 지하철역을_삭제한다(Long id) {
        ExtractableResponse<Response> deleteResponse = RestAssured
                .given().log().all()
                .when()
                .delete("/stations/" + id)
                .then().log().all().extract();

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 지하철역을_모두_조회한다() {
        return RestAssured
                .given().log().all()
                .when()
                .get("/stations")
                .then().log().all().extract();
    }

    public static void 지하철노선_한개를_수정한다(Long id, String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + id)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철노선을_하나를_삭제한다(Long id) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .delete("/lines/" + id)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 지하철노선_목록을_조회한다() {
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선을_생성한다(Map<String, Object> params) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }

    public static ExtractableResponse<Response> 지하철노선_한개를_조회한다(long id) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .get("/lines/" + id)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }

    public static ExtractableResponse<Response> 지하철노선에_지하철구간을_등록한다(Long id, Map<String, Object> params) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + id + "/sections")
                .then().log().all()
                .extract();

        return response;
    }

    public static ExtractableResponse<Response> 지하철노선에_지하철구간을_제거한다(Long id, Station station) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .delete("/lines/" + id + "/sections?stationId=" + station.getId())
                .then().log().all()
                .extract();

        return response;
    }

    public static Map<String, Object> createLineParams(String name, String color, Long upStationId, Long downStationId
            , Long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    public static Map<String, Object> createSectionParams(Long upStationId, Long downStationId, Long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }
}
