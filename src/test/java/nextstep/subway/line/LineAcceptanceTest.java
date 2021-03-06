package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineRequestSteps.*;
import static nextstep.subway.line.LineVerificationStep.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void createLine() {
        // given & when - 신분당선 지하철 노선 생성 요청
        ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청("신분당선", "bg-red-600");

        // then
        지하철_노선_생성_됨(createLineResponse);
    }

    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    void getLines() {
        // given - 조회될 지하철 노선 생성
        지하철_노선_생성_요청("신분당선", "bg-red-600");
        지하철_노선_생성_요청("2호선", "bg-green-600");

        // when
        ExtractableResponse<Response> readLinesResponse = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_조회_됨(readLinesResponse);
        지하철_노선_목록_조회_결과에_2개_노선_포함_확인(readLinesResponse);
    }

    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void getLine() {
        // given - 조회할 지하철 노선 생성
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("신분당선", "bg-red-600");

        // when - 지하철 노선 조회 요청
        String uri = 생성된_지하철_노선_URI_경로_확인(createResponse);
        ExtractableResponse<Response> readLineResponse = 지하철_노선_조회_요청(uri);

        // then
        지하철_노선_조회_됨(readLineResponse);
    }

    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void updateLine() {
        // given - 수정할 지하철 노선 생성
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("신분당선", "bg-red-600");

        // when 지하철 노선 수정 요청
        Long id = 수정할_지하철_노선_ID_가져오기(createResponse);
        ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청(id, "구분당선", "bg-blue-600");

        // then
        지하철_노선_수정_됨(updateResponse);
    }

    @Test
    @DisplayName("지하철 노선을 제거한다.")
    void deleteLine() {
        // given - 제거할 지하철 노선 생성
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("신분당선", "bg-red-600");

        // when 지하철 노선 제거 요청
        String uri = 생성된_지하철_노선_URI_경로_확인(createResponse);
        ExtractableResponse<Response> deleteResponse = 지하철_노선_제거_요청(uri);

        // then
        지하철_노선_제거_됨(deleteResponse);
    }
}
