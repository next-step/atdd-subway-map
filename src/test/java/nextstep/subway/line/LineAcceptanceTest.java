package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Line;
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

    private static final String PATH = "/lines";

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        final ExtractableResponse<Response> 신분당선 = 지하철_노선_생성요청("신분당선","red");

        // then
        응답코드_확인(신분당선, CREATED);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        final ExtractableResponse<Response> 경강선 = 지하철_노선_생성요청("경강선", "blue");
        final ExtractableResponse<Response> 신분당선 = 지하철_노선_생성요청("신분당선", "red");

        // when
        final ExtractableResponse<Response> response = 지하철_노선_목록조회요청();

        // then
        응답코드_확인(response, OK);
        assertThat(getLineIds(response)).containsAll(extractLineIds(경강선, 신분당선));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        final ExtractableResponse<Response> 경강선 = 지하철_노선_생성요청("경강성", "blue");

        // when
        final ExtractableResponse<Response> response = 지하철_노선_조회요청("/lines/1");

        // then
        응답코드_확인(response, OK);
        assertThat(getLineId(response)).isEqualTo(getLineId(경강선));
    }


    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        지하철_노선_생성요청("경강성", "blue");
        final LineRequest 신분당선 = new LineRequest("신분당선", "red");

        // when
        final ExtractableResponse<Response> response = 지하철_노선_수정요청(신분당선);

        // then
        응답코드_확인(response, OK);
        assertThat(getLineName(response)).isEqualTo(신분당선.getName());
    }


    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        지하철_노선_생성요청("경강성", "blue");

        // when
        final ExtractableResponse<Response> response = 지하철_노선_삭제요청();

        // then
        응답코드_확인(response, NO_CONTENT);
    }

    private String getLineName(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getString("name");
    }

    private long getLineId(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getLong("id");
    }

    private List<Long> extractLineIds(ExtractableResponse<Response> ...lines) {
        return Arrays.stream(lines)
                .map(this::getLineId)
                .collect(Collectors.toList());
    }

    private List<Long> getLineIds(ExtractableResponse<Response> response) {
        return response.body().jsonPath()
                .getList(".", LineResponse.class)
                .stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
    }

    private ExtractableResponse<Response> 지하철_노선_목록조회요청() {

        return RestAssured
                .given().log().all()
                .when().get(PATH)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_조회요청(String s) {
        return RestAssured
                .given().log().all()
                .when().get(PATH)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_생성요청(String name, String color) {
        return RestAssured
                .given().log().all()
                .body(new Line(name, color))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(PATH)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정요청(LineRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(PATH + "/1")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_삭제요청() {
        return RestAssured
                .given().log().all()
                .when().delete(PATH + "/1")
                .then().log().all().extract();
    }

    private void 응답코드_확인(ExtractableResponse<Response> response, HttpStatus created) {
        assertThat(response.statusCode()).isEqualTo(created.value());
    }
}
