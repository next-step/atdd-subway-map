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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        // 지하철_노선_생성_요청
        final ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "bg-red-600");

        // then
        // 지하철_노선_생성됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        List<Long> lineResponseIds = new ArrayList();
        lineResponseIds.add(지하철_노선_등록되어_있음("신분당선", "bg-red-600"));
        lineResponseIds.add(지하철_노선_등록되어_있음("1호선", "bg-blue-600"));

        // when
        // 지하철_노선_목록_조회_요청
        final ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        // 지하철_노선_목록_응답됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        // 지하철_노선_목록_포함됨
        List<Long> responseIds = 지하철_노선_목록_포함됨(response);
        assertThat(lineResponseIds).containsAll(responseIds);
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

    private static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color) {
        LineRequest request = new LineRequest(name, color);

        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    private static Long 지하철_노선_등록되어_있음(String name, String color) {

        return 지하철_노선_생성_요청(name, color).as(LineResponse.class).getId();
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {

        return RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();

    }

    private static List<Long> 지하철_노선_목록_포함됨(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", LineResponse.class)
                .stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
    }
}
