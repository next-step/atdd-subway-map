package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.utils.DatabaseCleanup;
import org.apache.groovy.util.Maps;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given & when
        ExtractableResponse<Response> response = 지하철_노선_등록되어_있음("경강선", "deep-blue");

        // then
        응답_HTTP_CREATED(response);
        지하철_노선_포함됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        지하철_노선_등록되어_있음("경강선", "deep-blue");
        지하철_노선_등록되어_있음("신분당선", "red");

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        응답_HTTP_OK(response);
        지하철_노선_목록_포함됨(response);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        지하철_노선_등록되어_있음("경강선", "deep-blue");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청();

        // then
        응답_HTTP_OK(response);
        지하철_노선_포함됨(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        지하철_노선_등록되어_있음("경강선", "deep-blue");

        // when
        Map<String, String> params = Maps.of("name", "신분당선", "color", "red");
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(params);

        // then
        응답_HTTP_OK(response);

    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        지하철_노선_등록되어_있음("2호선", "green");

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청();

        // then
        응답_HTTP_NO_CONTENT(response);
    }

    private ExtractableResponse<Response> 지하철_노선_등록되어_있음(String name, String color) {
        Map<String, String> params = Maps.of("name", name, "color", color);
        return RestAssured
                .given().log().all()
                .body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/1")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/1")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_제거_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/1")
                .then().log().all().extract();
    }

    private void 응답_HTTP_CREATED(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 응답_HTTP_OK(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 응답_HTTP_NO_CONTENT(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response) {
        List<LineResponse> lineResponses = response.jsonPath().getList(".", LineResponse.class);
        Assertions.assertThat(lineResponses).isNotEmpty();
    }

    private void 지하철_노선_포함됨(ExtractableResponse<Response> response) {
        LineResponse lineResponse = response.jsonPath().getObject(".", LineResponse.class);
        Assertions.assertThat(lineResponse).isNotNull();
    }

}
