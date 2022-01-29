package nextstep.subway.acceptance;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.utils.RestAssuredRequest.*;
import static nextstep.subway.utils.RestAssuredRequest.delete;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

public class LineSteps {
    private static final String LINES_PATH = "/lines";
    private static final String LINES_SECTIONS_PATH = "/lines/%d/sections";

    public static Map<String, String> 지하철_노선_파라미터(String name, String color, Long upStationId, Long downStationId, int distance) {
        return new HashMap<String, String>(){{
            put("name", name);
            put("color", color);
            put("upStationId", String.valueOf(upStationId));
            put("downStationId", String.valueOf(downStationId));
            put("distance", String.valueOf(distance));
        }};
    }

    public static Map<String, String> 구간_파라미터(Long upStationId, Long downStationId, int distance) {
        return new HashMap<String, String>(){{
            put("upStationId", String.valueOf(upStationId));
            put("downStationId", String.valueOf(downStationId));
            put("distance", String.valueOf(distance));
        }};
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return post(params, LINES_PATH);
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return get(LINES_PATH);
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(String uri) {
        return get(uri);
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String uri, Map<String, String> params) {
        return put(uri, params);
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(String uri) {
        return delete(uri);
    }

    public static ExtractableResponse<Response> 구간_등록_요청(Long lineId, Map<String, String> params) {
        return post(params, String.format(LINES_SECTIONS_PATH, lineId));
    }

    public static ExtractableResponse<Response> 구간_제거_요청(Long lineId, Long stationId) {
        return delete(String.format(LINES_SECTIONS_PATH, lineId) + "?stationId=" + stationId);
    }

    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(Map<String, String> params) {
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(params);
        지하철_노선_생성됨(response, params);

        return response;
    }

    public static ExtractableResponse<Response> 구간_등록되어_있음(Long lineId, Map<String, String> params) {
        ExtractableResponse<Response> response = 구간_등록_요청(lineId, params);
        구간_등록됨(response, params);
        return response;
    }

    public static void 지하철_노선_생성됨(ExtractableResponse<Response> response, Map<String, String> params) {
        응답_요청_확인(response, HttpStatus.CREATED);
        assertThat(response.header("Location")).isNotBlank();
        assertThat(jsonPath(response).getList("stations.id", String.class)).containsExactly(params.get("upStationId"), params.get("downStationId"));
    }

    public static void 지하철_노선_목록_조회됨(ExtractableResponse<Response> response, ExtractableResponse<Response> ... createResponses) {
        List<String> createdNames = Arrays.stream(createResponses)
                .map(createResponse -> jsonPath(createResponse).getString("name"))
                .collect(Collectors.toList());
        List<String> createdColors = Arrays.stream(createResponses)
                .map(createResponse -> jsonPath(createResponse).getString("color"))
                .collect(Collectors.toList());
        List<List<Object>> createdStations = Arrays.stream(createResponses)
                .map(createResponse -> jsonPath(createResponse).getList("stations"))
                .collect(Collectors.toList());

        응답_요청_확인(response, HttpStatus.OK);
        assertThat(jsonPath(response).getList("name")).isEqualTo(createdNames);
        assertThat(jsonPath(response).getList("color")).isEqualTo(createdColors);
        assertThat(jsonPath(response).get("stations").equals(createdStations)).isTrue();
    }

    public static void 지하철_노선_조회됨(ExtractableResponse<Response> response, ExtractableResponse<Response> createResponse) {
        응답_요청_확인(response, HttpStatus.OK);
        assertThat(jsonPath(response).getString("name")).isEqualTo(jsonPath(createResponse).getString("name"));
        assertThat(jsonPath(response).get("stations").equals(jsonPath(createResponse).get("stations"))).isTrue();
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response, ExtractableResponse<Response> showResponse, Map<String, String> updateParams) {
        응답_요청_확인(response, HttpStatus.OK);
        응답_요청_확인(showResponse, HttpStatus.OK);
        assertThat(jsonPath(showResponse).getString("name")).isEqualTo(updateParams.get("name"));
        assertThat(jsonPath(showResponse).getString("color")).isEqualTo(updateParams.get("color"));
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        응답_요청_확인(response, HttpStatus.NO_CONTENT);
    }

    public static void 지하철_노선_이름_중복됨(ExtractableResponse<Response> response) {
        응답_요청_확인(response, HttpStatus.CONFLICT);
    }

    public static void 구간_등록됨(ExtractableResponse<Response> response, Map<String, String> params) {
        응답_요청_확인(response, HttpStatus.CREATED);
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 구간_등록_실패됨(ExtractableResponse<Response> response) {
        응답_요청_확인(response, HttpStatus.BAD_REQUEST);
    }

    public static void 구간_제거됨(ExtractableResponse<Response> response) {
        응답_요청_확인(response, HttpStatus.NO_CONTENT);
    }

    public static void 구간_제거_실패됨(ExtractableResponse<Response> response) {
        응답_요청_확인(response, HttpStatus.BAD_REQUEST);
    }

    private static void 응답_요청_확인(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    public static Long 지하철_노선_id(ExtractableResponse<Response> createResponse) {
        return jsonPath(createResponse).getLong("id");
    }

    private static JsonPath jsonPath(ExtractableResponse<Response> response) {
        return response.jsonPath();
    }
}
