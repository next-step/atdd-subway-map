package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.utils.LineSteps.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("2호선", "green");

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 생성시 생성 실패")
    @Test
    void validateReduplicationLine() {
        // given
        지하철_노선_등록되어_있음("2호선", "green");

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("2호선", "blue");

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_등록되어_있음("2호선", "green");

        ExtractableResponse<Response> createdResponse2 = 지하철_노선_등록되어_있음("1호선", "blue");

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_조회됨(response, 2);
        지하철_노선_목록_포함됨(response, Arrays.asList(createdResponse1, createdResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_등록되어_있음("2호선", "green");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createdResponse);

        // then
        지하철_노선_조회됨(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_등록되어_있음("2호선", "green");

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청("2호선", "red", createdResponse);

        // then
        지하철_노선_수정됨(response, "2호선", "red");
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
}
