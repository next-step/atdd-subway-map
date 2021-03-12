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

public class SectionTestUtils extends BaseTestUtils {
    private static final int DEFAULT_DISTANCE = 10;

    private SectionTestUtils() {}

    public static Map<String, String> 구간_파라미터_설정(StationResponse upStationResponse, StationResponse downStationResponse) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", String.valueOf(upStationResponse.getId()));
        params.put("downStationId", String.valueOf(downStationResponse.getId()));
        params.put("distance", String.valueOf(DEFAULT_DISTANCE));
        return params;
    }

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(Map<String, String> body, String url) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제_요청(StationResponse response, String url) {
        return RestAssured.given().log().all()
                .param("stationId", response.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(url)
                .then().log().all()
                .extract();
    }
}