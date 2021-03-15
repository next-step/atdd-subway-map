package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.constants.TestConstants.HTTP_HEADER_LOCATION;
import static org.assertj.core.api.Assertions.assertThat;

public class StationTestUtils extends BaseTestUtils{
    private StationTestUtils() {}

    private static final String PARAM_NAME = "name";
    private static final String BASE_URL = "/stations";

    public static Map<String, String> 역_파라미터_설정(String name) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME, name);
        return params;
    }

    public static ExtractableResponse<Response> 역_생성_요청(Map<String, String> 역) {
        return RestAssured.given().log().all()
                .body(역)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(BASE_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 역_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when()
                .get(BASE_URL)
                .then().log().all()
                .extract();
    }

    public static void 지하철_역_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> responses) {
        List<Long> expectedStationIds = responses.stream()
                .map(it -> Long.parseLong(it.header(HTTP_HEADER_LOCATION).split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultStationIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        assertThat(resultStationIds).containsAll(expectedStationIds);
    }

    public static ExtractableResponse<Response> 역_제거_요청(ExtractableResponse<Response> 역응답) {
        String uri = 역응답.header(HTTP_HEADER_LOCATION);
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }
}
