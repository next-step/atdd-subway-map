package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineSteps.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("2호선", "green");

        // then
        지하철_노선_생성_성공(response);
    }

    @DisplayName("기존에 존재하는 노선 이름으로 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_생성_요청("2호선", "green");

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("2호선", "green");

        // then
        지하철_노선_생성_실패(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        지하철_노선_생성_요청("2호선", "green");
        지하철_노선_생성_요청("3호선", "orange");

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_조회_성공(response);
        지하철_노선_목록_조회_결과_2건(response);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createdLine = 지하철_노선_생성_요청("2호선", "green");
        Long createdLineId = 생성된_지하철_노선_ID_확인(createdLine);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createdLineId);

        // then
        지하철_노선_조회_성공(response);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLineWithNoId() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(1L);

        // then
        지하철_노선_조회_결과_없음(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createdLine = 지하철_노선_생성_요청("2호선", "green");
        Long createdLineId = 생성된_지하철_노선_ID_확인(createdLine);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(createdLineId, "2호선", "orange");

        // then
        지하철_노선_수정_성공(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createdLine = 지하철_노선_생성_요청("2호선", "green");
        Long createdLineId = 생성된_지하철_노선_ID_확인(createdLine);

        // when
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(createdLineId);

        // then
        지하철_노선_삭제_성공(response);
    }
}
