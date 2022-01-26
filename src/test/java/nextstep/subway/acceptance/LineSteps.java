package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineCreateRequest;
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

    public static Map<String, String> getParams(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return params;
    }

//    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
//        return RestAssured.given().log().all()
//                .body(params)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .when()
//                .post(DEFAULT_PATH)
//                .then().log().all()
//                .extract();
//    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(
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

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(String uri) {
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

    public static ExtractableResponse<Response> 지하철_노선_구간_추가_요청(
            String url,
            String upStationId,
            String downStationId,
            String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(url + SECTIONS_PATH)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_조회_검증(LineCreateResponse lineCreateResponse, ExtractableResponse<Response> response, List<Station> expectedStations) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse lineResponse = response.body().as(LineResponse.class);
        List<Station> stations = lineResponse.getStations();
        assertThat(stations).containsAll(expectedStations);
        assertThat(lineResponse.getName()).isEqualTo(lineCreateResponse.getName());
        assertThat(lineResponse.getColor()).isEqualTo(lineCreateResponse.getColor());
        assertThat(lineResponse.getCreatedDate()).isNotNull();
        assertThat(lineResponse.getModifiedDate()).isNotNull();
    }

    public static void 지하철_노선_생성_응답_검증(String color, String name, ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        LineResponse lineResponse = createResponse.body().as(LineResponse.class);
        assertAll(
                () -> assertNotNull(lineResponse.getId()),
                () -> assertEquals(lineResponse.getName(), name),
                () -> assertEquals(lineResponse.getColor(), color),
                () -> assertNotNull(lineResponse.getCreatedDate()),
                () -> assertNotNull(lineResponse.getModifiedDate()));
    }

    public static void 실패_응답_검증(int statusCode) {
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_구간_삭제_요청(String downStationId, String s) {
        return RestAssured.given().log().all()
                .param("stationId", downStationId)
                .when()
                .delete(s + "/sections")
                .then().log().all()
                .extract();
    }

    public static void 성공_응답_검증(int statusCode) {
        assertThat(statusCode).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_목록_응답_검증(LineCreateResponse firstLine, LineCreateResponse secondLine, ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<LineResponse> lineResponses = response.body().jsonPath().getList(".", LineResponse.class);

        LineResponse firstLineResponse = lineCreateResponseConvertToLineResponse(firstLine);
        LineResponse secondLineResponse = lineCreateResponseConvertToLineResponse(secondLine);
        assertThat(lineResponses).contains(firstLineResponse, secondLineResponse);
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String 구분당선, String s, String uri) {
        Map<String, String> updateParams = getParams("구분당선", "bg-blue-600");
        return RestAssured.given().log().all()
                .body(updateParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }
}
