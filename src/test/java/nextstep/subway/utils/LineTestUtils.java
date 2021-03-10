package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.constants.TestConstants.HTTP_HEADER_LOCATION;
import static org.assertj.core.api.Assertions.assertThat;

public class LineTestUtils extends BaseTestUtils{
    private static final String PARAM_NAME = "name";
    private static final String PARAM_COLOR = "color";
    private static final String BASE_URL = "/lines";
    private static final String PARAM_UP_STATION_ID = "upStationId";
    private static final String PARAM_DOWN_STATION_ID = "downStationId";;
    private static final String PARAM_DISTANCE = "distance";

    private LineTestUtils() {}

    public static Map<String, String> 노선_파라미터_설정(String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME, name);
        params.put(PARAM_COLOR, color);
        params.put(PARAM_UP_STATION_ID, String.valueOf(upStationId));
        params.put(PARAM_DOWN_STATION_ID, String.valueOf(downStationId));
        params.put(PARAM_DISTANCE, String.valueOf(distance));
        return params;
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(BASE_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(BASE_URL)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, ExtractableResponse<Response> responseWhenPostLineDX, ExtractableResponse<Response> responseWhenPostLineTwo) {
        List<Long> expectedLineIds = Arrays.asList(responseWhenPostLineDX, responseWhenPostLineTwo).stream()
                .map(resp -> Long.parseLong(resp.header(HTTP_HEADER_LOCATION).split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(lineResponse -> lineResponse.getId())
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> responseWhenPostLineDX) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(responseWhenPostLineDX.header(HTTP_HEADER_LOCATION))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> responseWhenPostLineDX, Map<String, String> paramsForUpdating) {
        return RestAssured
                .given().log().all()
                .body(paramsForUpdating)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(responseWhenPostLineDX.header(HTTP_HEADER_LOCATION))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> responseWhenPostLineDX) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(responseWhenPostLineDX.header(HTTP_HEADER_LOCATION))
                .then().log().all()
                .extract();
    }
}
