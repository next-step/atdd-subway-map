package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.subway.AcceptanceTest.parseIdFromResponseHeader;
import static org.assertj.core.api.Assertions.assertThat;

public class StationSteps {

    public static ExtractableResponse<Response> 지하철_역_생성_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static Long 지하철_역_생성_요청_ID_반환(Map<String, String> params) {
        ExtractableResponse<Response> stationResponse1 = 지하철_역_생성_요청(params);
        return parseIdFromResponseHeader(stationResponse1);
    }

    public static void 지하철_역_생성_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_역_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 지하철_역_조회() {
        return RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();
    }

    public static void 지하철_생성한_역_목록_조회_성공(Stream<ExtractableResponse<Response>> createResponses, ExtractableResponse<Response> getResponse) {
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> expectedIds = createResponses.map(AcceptanceTest::parseIdFromResponseHeader)
                .collect(Collectors.toList());
        List<Long> resultIds = getResponse.jsonPath().getList(".", StationResponse.class)
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultIds).containsAll(expectedIds);
    }

    public static ExtractableResponse<Response> 지하철_역_삭제(String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철_역_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
