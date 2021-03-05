package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.exception.ResourceNotFoundException;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        // 지하철_노선_생성요청
        ExtractableResponse<Response> response = 지하철_노선_생성요청("1호선", "노랑색");

        // then
        // 지하철_노선_생성됨
        지하철_노선_생성됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음 (지하철_노선_생성요청)
        // 지하철_노선_등록되어_있음 (지하철_노선_생성요청)
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_생성요청("1호선", "노랑색");
        ExtractableResponse<Response> createdResponse2 = 지하철_노선_생성요청("2호선", "빨강색");

        // when
        // 지하철_노선목록_조회요청
        ExtractableResponse<Response> response = 지하철_노선목록_조회요청();

        // then
        // 지하철_노선_목록_응답됨
        // 지하철_노선_목록_포함됨
        지하철_노선목록_응답됨포함됨(response, Arrays.asList(createdResponse1, createdResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음 (지하철_노선_생성요청)
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성요청("1호선", "노랑색");

        // when
        // 지하철_노선_조회_요청
        Long lineId = createdResponse.as(LineResponse.class).getId();
        ExtractableResponse<Response> response = 지하철_노선_조회요청(lineId);

        // then
        // 지하철_노선_응답됨포함됨
        지하철_노선_응답됨포함됨(response, createdResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음 (지하철_노선_생성요청)
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성요청("1호선", "노랑색");

        // when
        // 지하철_노선_수정요청
        Long lineId = createdResponse.as(LineResponse.class).getId();
        String name = "3호선";
        String color = "파랑색";
        ExtractableResponse<Response> response = 지하철_노선_수정요청(lineId, name, color);

        // then
        // 지하철_노선_수정됨
        지하철_노선_수정됨(response, name, color);
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

    private ExtractableResponse<Response> 지하철_노선_생성요청(String name, String color) {
        LineRequest request = new LineRequest(name, color);

        return RestAssured
                .given().log().all().body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> 지하철_노선목록_조회요청() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all().extract();
        return response;
    }

    private void 지하철_노선목록_응답됨포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> it.as(LineResponse.class))
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private ExtractableResponse<Response> 지하철_노선_조회요청(Long lineId) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines/{id}", lineId)
                .then().log().all().extract();
        return response;
    }

    private void 지하철_노선_응답됨포함됨(ExtractableResponse<Response> response, ExtractableResponse<Response> createdResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        Long resultLineId = response.as(LineResponse.class).getId();
        Long expectedLineId = createdResponse.as(LineResponse.class).getId();

        assertThat(resultLineId).isEqualTo(expectedLineId);
    }

    private ExtractableResponse<Response> 지하철_노선_수정요청(Long lineId, String name, String color) {
        LineRequest request = new LineRequest(name, color);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all().body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", lineId)
                .then().log().all().extract();

        return response;
    }

    private void 지하철_노선_수정됨(ExtractableResponse<Response> response, String name, String color) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        String updatedName = response.as(LineResponse.class).getName();
        String updatedColor = response.as(LineResponse.class).getColor();

        assertThat(updatedName).isEqualTo(name);
        assertThat(updatedColor).isEqualTo(color);
    }
}
