package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineResponse;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest extends AcceptanceTest {

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
        ExtractableResponse<Response> response = 지하철_노선_조회요청("/lines/1");

        // then
        응답코드_확인(response, OK);
        assertThat(response.body().jsonPath().getLong("id")).isEqualTo(경강선.body().jsonPath().getLong("id"));
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

    private static Long getLineIdFromHeader(ExtractableResponse<Response> response) {
        return Long.parseLong(response
                .header(HttpHeaders.LOCATION)
                .split("/")[2]) ;
    }

    private void 응답코드_확인(ExtractableResponse<Response> response, HttpStatus created) {
        assertThat(response.statusCode()).isEqualTo(created.value());
    }

    private List<Long> extractLineIds(ExtractableResponse<Response> ...lines) {
        return Arrays.stream(lines)
                .map(LineAcceptanceTest::getLineIdFromHeader)
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
                .when().get("/lines")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_조회요청(String s) {
        return RestAssured
                .given().log().all()
                .when().get(s)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철_노선_생성요청(String name, String color) {
        return RestAssured
                .given().log().all()
                .body(new Line(name, color))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all().extract();
    }
}
