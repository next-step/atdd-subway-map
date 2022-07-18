package nextstep.subway.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LineApi {

    public static ExtractableResponse<Response> createLineApi(String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, Object> lineRequest = new HashMap<>();
        lineRequest.put("name", name);
        lineRequest.put("color", color);
        lineRequest.put("upStationId", upStationId);
        lineRequest.put("downStationId", downStationId);
        lineRequest.put("distance", distance);

        ExtractableResponse<Response> createLineResponse = RestAssured
                .given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        assertThat(createLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return createLineResponse;
    }

    public static ExtractableResponse<Response> getAllLinesApi() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }

    public static ExtractableResponse<Response> getLineByIdApi(Long lineId) {
        ExtractableResponse<Response> getLineResponse = RestAssured
                .given().log().all()
                .pathParam("id", lineId)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();

        assertThat(getLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        return getLineResponse;
    }

    public static ExtractableResponse<Response> changeLineByIdApi(Long lineId, String name, String color) {
        Map<String, String> lineChangeRequest = new HashMap<>();
        lineChangeRequest.put("name", name);
        lineChangeRequest.put("color", color);

        ExtractableResponse<Response> changeLineResponse = RestAssured
                .given().log().all()
                .pathParam("id", lineId)
                .body(lineChangeRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/{id}")
                .then().log().all()
                .extract();
        assertThat(changeLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        return changeLineResponse;
    }

    public static ExtractableResponse<Response> deleteLineByIdApi(Long lineId) {
        ExtractableResponse<Response> deleteLineResponse = RestAssured
                .given().log().all()
                .pathParam("id", lineId)
                .when().delete("/lines/{id}")
                .then().log().all()
                .extract();
        assertThat(deleteLineResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        return deleteLineResponse;
    }

    public static ExtractableResponse<Response> addSectionApi(Long lineId, Long upStationId, Long downStationId, int distance) {
        Map<String, Object> sectionRequest = new HashMap<>();
        sectionRequest.put("upStationId", upStationId);
        sectionRequest.put("downStationId", downStationId);
        sectionRequest.put("distance", distance);

        return RestAssured
                .given().log().all()
                .pathParam("id", lineId)
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteSectionApi(Long lineId, Long stationId) {
        return RestAssured
                .given().log().all()
                .pathParam("id", lineId)
                .queryParams("stationId", stationId)
                .when().delete("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }
}
