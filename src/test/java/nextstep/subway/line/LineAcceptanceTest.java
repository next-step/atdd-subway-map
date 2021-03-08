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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        assertThat(response.statusCode())
                .isEqualTo(CREATED.value());
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
        assertThat(linesResponse.statusCode())
                .isEqualTo(OK.value());
        // 지하철_노선_목록_포함됨
        List<Long> actualIds = linesResponse.jsonPath()
                .getList(".", LineResponse.class)
                .stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        List<Long> expectedIds = Stream.of(line1Response, line2Response)
                .map(response -> response.as(LineResponse.class))
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        assertThat(actualIds).containsAll(expectedIds);
    }


    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        LineResponse expectedLine = 지하철_노선_생성_요청("1호선", "blue")
                .as(LineResponse.class);
        지하철_노선_생성_요청("2호선", "lightgreen");

        // when
        // 지하철_노선_조회_요청
        Long lineId = expectedLine.getId();
        LineResponse actualLine = 지하철_노선_조회_요청(lineId)
                .as(LineResponse.class);

        // then
        // 지하철_노선_응답됨
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actualLine.getId())
                .isEqualTo(expectedLine.getId());
        softly.assertThat(actualLine.getName())
                .isEqualTo(expectedLine.getName());
        softly.assertThat(actualLine.getColor())
                .isEqualTo(expectedLine.getColor());
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        LineResponse lineResponse = 지하철_노선_생성_요청("1호선", "blue")
                .as(LineResponse.class);

        // when
        // 지하철_노선_수정_요청
        String expectedName = "9호선";
        String expectedColor = "yellow";
        LineResponse actualLine = 지하철_노선_수정_요청(
                lineResponse.getId(),
                createParam(expectedName, expectedColor)
        ).as(LineResponse.class);

        // then
        // 지하철_노선_수정됨
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actualLine.getName())
                .isEqualTo(expectedName);
        softly.assertThat(actualLine.getColor())
                .isEqualTo(expectedColor);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        LineResponse lineResponse = 지하철_노선_생성_요청("1호선", "blue")
                .as(LineResponse.class);

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse.getId());

        // then
        // 지하철_노선_삭제됨
        assertThat(response.statusCode())
                .isEqualTo(NO_CONTENT.value());
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

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PATH)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(Long lineId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PATH + "/{id}", lineId)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(Long lineId, Map<String, String> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(PATH + "/{id}", lineId)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_제거_요청(Long lineId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(PATH + "/{id}", lineId)
                .then().log().all().extract();
    }
}
