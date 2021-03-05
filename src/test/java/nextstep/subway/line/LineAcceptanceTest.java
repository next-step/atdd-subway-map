package nextstep.subway.line;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        // 지하철_노선_생성_요청
        final ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선_생성());

        // then
        // 지하철_노선_생성됨
        지하철_노선_생성됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        // 지하철_노선_등록되어_있음
        final ExtractableResponse<Response> 신분당선_응답 = 지하철_노선_생성_요청(신분당선_생성());
        final ExtractableResponse<Response> 경춘선_응답 = 지하철_노선_생성_요청(경춘선_생성());

        // when
        // 지하철_노선_목록_조회_요청
        final ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        // 지하철_노선_목록_응답됨
        // 지하철_노선_목록_포함됨
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, 신분당선_응답, 경춘선_응답);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음

        // when
        // 지하철_노선_조회_요청

        // then
        // 지하철_노선_응답됨
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음

        // when
        // 지하철_노선_수정_요청

        // then
        // 지하철_노선_수정됨
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음

        // when
        // 지하철_노선_제거_요청

        // then
        // 지하철_노선_삭제됨
    }

    private void 지하철_노선_목록_포함됨(
        ExtractableResponse<Response> response,
        ExtractableResponse<Response> 신분당선_응답,
        ExtractableResponse<Response> 경춘선_응답) {

        final List<Long> expectedLineIds = Stream.of(신분당선_응답, 경춘선_응답)
            .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
            .collect(toList());
        final List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
            .map(LineResponse::getId)
            .collect(toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> map) {
        return RestAssured
            .given().log().all()
            .body(map)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotNull();
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    private Map<String, String> 신분당선_생성() {
        Map<String, String> map = new HashMap<>();
        map.put("color", "bg-red-600");
        map.put("name", "신분당선");
        return map;
    }

    private Map<String, String> 경춘선_생성() {
        Map<String, String> map = new HashMap<>();
        map.put("color", "bg-green-600");
        map.put("name", "경춘선");
        return map;
    }
}
