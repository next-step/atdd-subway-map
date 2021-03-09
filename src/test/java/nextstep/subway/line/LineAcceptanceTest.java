package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;


@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private final static String PATH = "/lines";


    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("7호선", "green");

        // then
        // 지하철_노선_생성됨
        지하철_노선_생성됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> line1Response = 지하철_노선_생성_요청("1호선", "blue");
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> line2Response = 지하철_노선_생성_요청("2호선", "lightgreen");

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> linesResponse = 지하철_노선_목록_조회_요청();

        // then
        // 지하철_노선_목록_응답됨
        지하철_노선_목록_응답됨(linesResponse);
        // 지하철_노선_목록_포함됨
        지하철_노선_목록_포함됨(linesResponse,
                Arrays.asList(line1Response, line2Response));
    }


    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> expectedResponse = 지하철_노선_생성_요청("1호선", "blue");
        지하철_노선_생성_요청("2호선", "lightgreen");

        // when
        // 지하철_노선_조회_요청
        Long lineId = expectedResponse
                .as(LineResponse.class)
                .getId();
        ExtractableResponse<Response> actualResponse = 지하철_노선_조회_요청(lineId);

        // then
        // 지하철_노선_응답됨
        지하철_노선_응답됨(actualResponse, expectedResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        Long lineId = 지하철_노선_생성_요청("1호선", "blue")
                .as(LineResponse.class)
                .getId();

        // when
        // 지하철_노선_수정_요청
        String name = "9호선";
        String color = "yellow";
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(
                lineId,
                createParam(name, color));

        // then
        // 지하철_노선_수정됨
        지하철_노선_수정됨(response, name, color);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        Long lineId = 지하철_노선_생성_요청("1호선", "blue")
                .as(LineResponse.class)
                .getId();

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineId);

        // then
        // 지하철_노선_제거됨
        지하철_노선_제거됨(response);
    }

    private Map<String, String> createParam(String name, String color) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        return param;
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createParam(name, color))
                .when().post(PATH)
                .then().log().all().extract();
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(CREATED.value());
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PATH)
                .then().log().all().extract();
    }

    private void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(OK.value());
    }

    private void 지하철_노선_목록_포함됨(ExtractableResponse<Response> actualResponse,
                               List<ExtractableResponse<Response>> responses) {
        List<Long> actualIds = actualResponse.jsonPath()
                .getList(".", LineResponse.class)
                .stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        List<Long> expectedIds = responses.stream()
                .map(response -> response.as(LineResponse.class))
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        assertThat(actualIds).containsAll(expectedIds);
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(Long lineId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PATH + "/{id}", lineId)
                .then().log().all().extract();
    }

    private void 지하철_노선_응답됨(ExtractableResponse<Response> actualResponse, ExtractableResponse<Response> expectedResponse) {
        LineResponse actualLine = actualResponse.as(LineResponse.class);
        LineResponse expectedLine = expectedResponse.as(LineResponse.class);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actualLine.getId())
                .isEqualTo(expectedLine.getId());
        softly.assertThat(actualLine.getName())
                .isEqualTo(expectedLine.getName());
        softly.assertThat(actualLine.getColor())
                .isEqualTo(expectedLine.getColor());
        softly.assertAll();
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(Long lineId, Map<String, String> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(PATH + "/{id}", lineId)
                .then().log().all().extract();
    }

    private void 지하철_노선_수정됨(ExtractableResponse<Response> response, String expectedName, String expectedColor) {
        LineResponse line = response.as(LineResponse.class);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(line.getName())
                .isEqualTo(expectedName);
        softly.assertThat(line.getColor())
                .isEqualTo(expectedColor);
        softly.assertAll();
    }

    private ExtractableResponse<Response> 지하철_노선_제거_요청(Long lineId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(PATH + "/{id}", lineId)
                .then().log().all().extract();
    }

    private void 지하철_노선_제거됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(NO_CONTENT.value());
    }
}
