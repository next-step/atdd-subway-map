package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        ExtractableResponse<Response> response = 지하철_노선_등록되어_있음("2호선", "초록색");

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_등록되어_있음("2호선", "초록색");

        ExtractableResponse<Response> createdResponse2 = 지하철_노선_등록되어_있음("1호선", "군청색");

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_응답됨(response, HttpStatus.OK);
        지하철_노선_목록_포함됨(createdResponse1, createdResponse2, response);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_등록되어_있음("2호선", "초록색");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createdResponse);

        // then
        지하철_노선_응답됨(response, HttpStatus.OK);
    }
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_등록되어_있음("2호선", "green");

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청("2호선", "red", createdResponse);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_등록되어_있음("2호선", "green");

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createdResponse);

        // then
        지하철_노선_삭제됨(response);
    }

    private ExtractableResponse<Response> 지하철_노선_등록되어_있음(String lineName, String lineColor) {
        LineRequest lineRequest1 = new LineRequest(lineName, lineColor);
        return RestAssured.given().log().all()
                .body(lineRequest1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        지하철_노선_응답됨(response, HttpStatus.CREATED);
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    private void 지하철_노선_응답됨(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    private void 지하철_노선_목록_포함됨(ExtractableResponse<Response> createdResponse1,
                               ExtractableResponse<Response> createdResponse2, ExtractableResponse<Response> response) {
        List<Long> expectedLineIds = Arrays.asList(createdResponse1, createdResponse2)
                .stream()
                .map(r -> Long.parseLong(r.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath()
                .getList(".", LineResponse.class)
                .stream()
                .map(r -> r.getId())
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> createdResponse) {
        String uri = createdResponse.header("Location");
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(String lineName, String lineColor,
                                                       ExtractableResponse<Response> createdResponse) {
        String uri = createdResponse.header("Location");
        return RestAssured.given().log().all()
                .body(new LineRequest("2호선", "red"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    private void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        지하철_노선_응답됨(response, HttpStatus.OK);
        LineResponse lineResponse = response.jsonPath().getObject(".", LineResponse.class);
        assertThat(lineResponse.getColor()).isEqualTo("red");
    }

    private ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> createdResponse) {
        String uri = createdResponse.header("Location");
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    private void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        지하철_노선_응답됨(response, HttpStatus.NO_CONTENT);
    }
}
