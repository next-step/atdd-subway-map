package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineCreateResponse;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Station;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LineSteps {
    public static final String DEFAULT_PATH = "/lines";
    public static final String SECTIONS_PATH = "/sections";

    public static ExtractableResponse<Response> 지하철_노선_생성_요청을_한다(
            String name,
            String color,
            String upStationId,
            String downStationId,
            String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(DEFAULT_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회를_요청한다() {
        return RestAssured.given().log().all()
                .when()
                .get(DEFAULT_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회를_요청한다(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static LineResponse lineCreateResponseConvertToLineResponse(LineCreateResponse lineCreateResponse) {
        return new LineResponse(
                lineCreateResponse.getId(),
                lineCreateResponse.getName(),
                lineCreateResponse.getColor(),
                Collections.EMPTY_LIST,
                lineCreateResponse.getCreatedDate(),
                lineCreateResponse.getModifiedDate());
    }

    public static ExtractableResponse<Response> 지하철_노선_구간_추가를_요청한다(
            ExtractableResponse<Response> response,
            String upStationId,
            String downStationId,
            String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        String url = response.header("Location");

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(url + SECTIONS_PATH)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선을_응답받는다(ExtractableResponse<Response> createResponse, ExtractableResponse<Response> response, List<Station> expectedStations) {
        LineCreateResponse lineCreateResponse = createResponse.body().as(LineCreateResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse lineResponse = response.body().as(LineResponse.class);
        List<Station> stations = lineResponse.getStations();
        assertThat(stations).containsAll(expectedStations);
        assertThat(lineResponse.getName()).isEqualTo(lineCreateResponse.getName());
        assertThat(lineResponse.getColor()).isEqualTo(lineCreateResponse.getColor());
        assertThat(lineResponse.getCreatedDate()).isNotNull();
        assertThat(lineResponse.getModifiedDate()).isNotNull();
    }

    public static void 지하철_노선_생성이_성공한다(String color, String name, ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        LineResponse lineResponse = createResponse.body().as(LineResponse.class);
        assertAll(
                () -> assertNotNull(lineResponse.getId()),
                () -> assertEquals(lineResponse.getName(), name),
                () -> assertEquals(lineResponse.getColor(), color),
                () -> assertNotNull(lineResponse.getCreatedDate()),
                () -> assertNotNull(lineResponse.getModifiedDate()));
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제를_요청한다(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_구간_삭제를_요청한다(ExtractableResponse<Response> response, String downStationId) {
        String uri = response.header("Location");
        return RestAssured.given().log().all()
                .param("stationId", downStationId)
                .when()
                .delete(uri + SECTIONS_PATH)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_목록들을_응답받는다(ExtractableResponse<Response> firstResponse, ExtractableResponse<Response> secondResponse, ExtractableResponse<Response> response) {
        LineCreateResponse firstLine = firstResponse.body().as(LineCreateResponse.class);
        LineCreateResponse secondLine = secondResponse.body().as(LineCreateResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<LineResponse> lineResponses = response.body().jsonPath().getList(".", LineResponse.class);

        LineResponse firstLineResponse = lineCreateResponseConvertToLineResponse(firstLine);
        LineResponse secondLineResponse = lineCreateResponseConvertToLineResponse(secondLine);
        assertThat(lineResponses).contains(firstLineResponse, secondLineResponse);
    }

    public static ExtractableResponse<Response> 지하철_노선_수정을_요청한다(ExtractableResponse<Response> response, String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        String uri = response.header("Location");

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_생성이_실패한다(ExtractableResponse<Response> response) {
        실패_응답_검증(response.statusCode());
    }

    public static void 지하철_노선_구간_추가에_성공한다(ExtractableResponse<Response> response) {
        성공_응답_검증(response.statusCode());
    }

    public static void 지하철_노선에_구간_추가가_실패한다(ExtractableResponse<Response> response) {
        실패_응답_검증(response.statusCode());
    }

    public static void 지하철_노선_수정이_성공한다(ExtractableResponse<Response> response) {
        성공_응답_검증(response.statusCode());
    }

    public static void 지하철_노선_삭제가_성공한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_노선_구간_삭제가_성공한다(ExtractableResponse<Response> response) {
        성공_응답_검증(response.statusCode());
    }

    public static void 지하철_노선에_구간_삭제가_실패한다(ExtractableResponse<Response> response) {
        실패_응답_검증(response.statusCode());
    }

    private static void 실패_응답_검증(int statusCode) {
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static void 성공_응답_검증(int statusCode) {
        assertThat(statusCode).isEqualTo(HttpStatus.OK.value());
    }
}
