package nextstep.subway.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ApiCall {

    public static ExtractableResponse<Response> 지하철_구간_생성(Long id, Long upStationId, Long downStationId, Integer distance) {
        Map<String, Object> params = new HashMap();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{id}/sections", id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제(Long id, Long stationId) {
        return RestAssured
            .given().log().all()
            .queryParam("stationId", stationId)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/lines/{id}/sections", id)
            .then().log().all()
            .extract();
    }

    public static Long 지하철역_생성(String name) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .body(Collections.singletonMap("name", name))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response.jsonPath().getLong("id");
    }

    public static List<String> 지하철역_목록_조회() {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response.jsonPath().getList("name");
    }

    public static void 지하철역_삭제(Long id) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when().delete("/stations/{id}", id)
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_노선_삭제(Long stationLineId) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/lines/{id}", stationLineId)
            .then().log().all()
            .extract();

        validateHttpStatus(response, HttpStatus.NO_CONTENT);
    }

    public static Long 지하철_노선_생성(String lineName,
        String color,
        Long upStationId,
        Long downStationId,
        Integer distance) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .body(StationLineRequest.builder()
                .name(lineName)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response.jsonPath().getLong("id");
    }

    public static void 지하철_노선_업데이트(Long stationLineId, StationLineRequest stationLineRequest) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .body(stationLineRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/lines/{id}", stationLineId)
            .then().log().all()
            .extract();

        validateHttpStatus(response, HttpStatus.OK);
    }

    public static List<String> 지하철_노선_목록_조회() {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();

        validateHttpStatus(response, HttpStatus.OK);
        return response.jsonPath().getList("name");
    }

    public static ExtractableResponse<Response> 지하철_노선_조회(Long stationLineId) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when().get("/lines/{id}", stationLineId)
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }

}
