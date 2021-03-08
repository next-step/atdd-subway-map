package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.apache.groovy.util.Maps;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.utils.HttpAssertions.*;

@DisplayName("지하철 노선 관련 기능")
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

    private void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response) {
        List<LineResponse> lineResponses = response.jsonPath().getList(".", LineResponse.class);
        Assertions.assertThat(lineResponses).isNotEmpty();
    }

    private void 지하철_노선_포함됨(ExtractableResponse<Response> response) {
        LineResponse lineResponse = response.jsonPath().getObject(".", LineResponse.class);
        Assertions.assertThat(lineResponse).isNotNull();
    }

}
