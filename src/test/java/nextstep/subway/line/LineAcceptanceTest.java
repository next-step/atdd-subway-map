package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        final ExtractableResponse<Response> 신분당선 = 지하철_노선_등록요청("신분당선","red");

        // then
        지하철_노선_등록됨(신분당선);
    }


    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        final Long blueLineId = 지하철_노선_등록되어_있음("경강선", "blue");
        final Long redLineId = 지하철_노선_등록되어_있음("신분당선", "red");

        // when
        final ExtractableResponse<Response> response = 지하철_노선_목록조회요청();

        // then
        지하철_노선_목록조회됨(blueLineId, redLineId, response);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        final Long lineId = 지하철_노선_등록되어_있음("경강선", "blue");

        // when
        final ExtractableResponse<Response> response = 지하철_노선_조회요청(lineId);

        // then
        지하철_노선_조회됨(lineId, response);
    }


    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        final Long lineId = 지하철_노선_등록되어_있음("경강선", "blue");

        // when
        final ExtractableResponse<Response> response = 지하철_노선_수정요청("신분당선", "red", lineId);

        // then
        지하철_노선_수정됨(lineId, response);
    }


    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        final Long lineId = 지하철_노선_등록되어_있음("경강선", "blue");

        // when
        final ExtractableResponse<Response> response = 지하철_노선_삭제요청(lineId);

        // then
        지하철_노선_삭제됨(response);
    }

    private long getLineId(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getLong("id");
    }

    private List<Long> getLineIds(ExtractableResponse<Response> response) {
        return response.body().jsonPath()
                .getList(".", LineResponse.class)
                .stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
    }

    private Long 지하철_노선_등록되어_있음(String name, String color) {
        return 지하철_노선_등록요청(name, color)
                .jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> 지하철_노선_목록조회요청() {

        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_조회요청(Long lineId) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/{lineId}", lineId)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_등록요청(String name, String color) {
        LineRequest request = new LineRequest(name, color);

        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정요청(String name, String color, Long lineId) {
        LineRequest request = new LineRequest(name, color);

        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/{lineId}", lineId)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_삭제요청(Long lineId) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all().extract();
    }

    private void 지하철_노선_등록됨(ExtractableResponse<Response> 신분당선) {
        응답코드_확인(신분당선, CREATED);
    }

    private void 지하철_노선_목록조회됨(Long blueLineId, Long redLineId, ExtractableResponse<Response> response) {
        응답코드_확인(response, OK);
        assertThat(getLineIds(response)).containsAll(Arrays.asList(blueLineId, redLineId));
    }

    private void 지하철_노선_조회됨(Long lineId, ExtractableResponse<Response> response) {
        응답코드_확인(response, OK);
        assertThat(getLineId(response)).isEqualTo(lineId);
    }

    private void 지하철_노선_수정됨(Long lineId, ExtractableResponse<Response> response) {
        응답코드_확인(response, OK);
        assertThat(getLineId(response)).isEqualTo(lineId);
    }

    private void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        응답코드_확인(response, NO_CONTENT);
    }

    private void 응답코드_확인(ExtractableResponse<Response> response, HttpStatus created) {
        assertThat(response.statusCode()).isEqualTo(created.value());
    }

}
