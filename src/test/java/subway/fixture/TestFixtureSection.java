package subway.fixture;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.Section;
import subway.domain.Sections;

import java.util.*;

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

    public static void 지하철_노선_구간_목록_중_등록_구간_조회됨(final ExtractableResponse<Response> lineResponse, final int countOfSections) {

        final JsonPath jsonPath = lineResponse.response().body().jsonPath();

        assertAll(
                () -> assertThat(lineResponse.response().statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(jsonPath.getList("sections")).hasSize(countOfSections)
        );
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
}
