package subway.fixture;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.Section;
import subway.domain.Sections;
import subway.ui.dto.LineResponse;
import subway.ui.dto.StationResponse;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestFixtureSection {

    public static ExtractableResponse<Response> 지하철_노선_구간_추가_요청(final Long lineId, final Long upStationId, final Long downStationId, final Integer distance) {
        final Map<String, Object> params = 지하철_노선_구간_생성_값(upStationId, downStationId, distance);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    private static Map<String, Object> 지하철_노선_구간_생성_값(final Long upStationId, final Long downStationId, final Integer distance) {
        final Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    public static Sections 구간_신규_등록(final Section section) {
        final List<Section> sections = new ArrayList<>(List.of(section));
        return Sections.from(sections);
    }

    public static Sections 구간_복수_등록(final Section...section) {
        final List<Section> sections = new ArrayList<>(Arrays.asList(section));
        return Sections.from(sections);
    }

    public static void 지하철_노선_추가됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_구간_목록_조회_요청(final Long lineId) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_구간_추가_실패됨(final ExtractableResponse<Response> response, final HttpStatus httpStatus, final String message) {
        final JsonPath jsonPathResponse = response.response().body().jsonPath();

        assertAll(
                () -> assertThat(response.response().statusCode()).isEqualTo(httpStatus.value()),
                () -> assertThat(jsonPathResponse.getString("message")).isEqualTo(message)
        );
    }

    public static ExtractableResponse<Response> 지하철_노선_구간_삭제_요청(final Long lineId, final Long deleteStation) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/" + lineId + "/sections?stationId=" + deleteStation)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_구간_삭제됨(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
        );
    }

    public static void 지하철_노선_구간_삭제_실패됨(final ExtractableResponse<Response> response, final HttpStatus httpStatus, final String message) {
        final JsonPath jsonPathResponse = response.response().body().jsonPath();

        assertAll(
                () -> assertThat(response.response().statusCode()).isEqualTo(httpStatus.value()),
                () -> assertThat(jsonPathResponse.getString("message")).isEqualTo(message)
        );
    }

    public static void 지하철_노선_구간_조회됨(final ExtractableResponse<Response> response, final int countOfStations, final Long...stationId) {
        final JsonPath jsonPath = response.response().body().jsonPath();
        final List<Long> stationIds = 지하철_노선_구간_목록_중_역_ID_목록_추출함(response);

        assertAll(
                () -> assertThat(response.response().statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(jsonPath.getList("stations")).hasSize(countOfStations),
                () -> assertThat(stationIds).containsAll(Arrays.asList(stationId))
        );
    }

    private static List<Long> 지하철_노선_구간_목록_중_역_ID_목록_추출함(final ExtractableResponse<Response> lineResponse) {
        final LineResponse lineResponses = lineResponse.jsonPath().getObject("", LineResponse.class);
        return lineResponses.getStationResponses().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }
}
