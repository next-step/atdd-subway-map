package subway.fixture;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.ui.dto.LineResponse;
import subway.domain.Line;
import subway.domain.Section;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.TestFixtureSection.구간_신규_등록;

public class TestFixtureLine {

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(final String name, final String color, final Long upStationId, final Long downStationId, final Integer distance) {
        final Map<String, Object> params = 지하철_생성_값(name, color, upStationId,downStationId, distance);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static Map<String, Object> 지하철_생성_값(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    public static void 지하철_노선_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_목록_중_생성한_노선_조회됨(final ExtractableResponse<Response> lineResponse, final ExtractableResponse<Response> createResponse) {
        final List<Long> 지하철_노선_목록_ID = 지하철_노선_목록_중_ID_목록_추출함(lineResponse);
        final Long 지하철_노선_생성_ID = 응답_헤더_ID_추출함(createResponse);

        assertAll(
                () -> assertThat(lineResponse.response().statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(지하철_노선_목록_ID).contains(지하철_노선_생성_ID)
        );
    }

    private static List<Long> 지하철_노선_목록_중_ID_목록_추출함(ExtractableResponse<Response> lineResponse) {
        return lineResponse.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
    }

    private static long 응답_헤더_ID_추출함(final ExtractableResponse<Response> createResponse) {
        return Long.parseLong(createResponse.header("Location").split("/")[2]);
    }

    public static void 지하철_노선_목록_조회됨(final ExtractableResponse<Response> lineResponse, final int countOfLine) {
        final JsonPath 지하철_목록_응답_경로 = lineResponse.response().body().jsonPath();

        assertAll(
                () -> assertThat(lineResponse.response().statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(지하철_목록_응답_경로.getList("")).hasSize(countOfLine)
        );
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(final ExtractableResponse<Response> response) {
        return RestAssured
                .given().log().all()
                .when().get(response.header("location"))
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_조회됨(final ExtractableResponse<Response> response, final String lineName, final String lineColor, final int countOfStation) {
        final JsonPath jsonPath = response.response().body().jsonPath();

        assertAll(
                () -> assertThat(response.response().statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(jsonPath.getString("name")).isEqualTo(lineName),
                () -> assertThat(jsonPath.getString("color")).isEqualTo(lineColor),
                () -> assertThat(jsonPath.getList("stations")).hasSize(countOfStation)
        );
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(final ExtractableResponse<Response> response, final String name, final String color) {
        final Map<String, Object> params = 파라미터_수정_요청(name, color);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(response.header("location"))
                .then().log().all()
                .extract();
    }

    private static Map<String, Object> 파라미터_수정_요청(final String name, final String color) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return params;
    }

    public static void 지하철_노선_수정됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(final ExtractableResponse<Response> response) {
        return RestAssured
                .given().log().all()
                .when().delete(response.header("location"))
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static Line 노선_등록(final Long id, final String name, final String color, Section section) {
        return new Line(id, name, color, 구간_신규_등록(section));
    }

    public static void 노선_추가_등록(final Line line, final Section section) {
        line.addSection(section);
    }

    public static Long 지하철노선_생성_요청(final String name, final String color, final Long upStationId, final Long downStationId, final Integer distance) {
        final ExtractableResponse<Response> response = 지하철_노선_생성_요청(name, color, upStationId, downStationId, distance);
        지하철_노선_생성됨(response);
        return response.jsonPath().getLong("id");
    }
}
