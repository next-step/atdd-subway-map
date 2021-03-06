package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineSteps.assertCreateLine;
import static nextstep.subway.line.LineSteps.assertDeleteLine;
import static nextstep.subway.line.LineSteps.assertGetLine;
import static nextstep.subway.line.LineSteps.assertGetLines;
import static nextstep.subway.line.LineSteps.assertIncludeLines;
import static nextstep.subway.line.LineSteps.assertUpdateLine;
import static nextstep.subway.line.LineSteps.requestCreateLine2;
import static nextstep.subway.line.LineSteps.requestCreateLineDx;
import static nextstep.subway.line.LineSteps.requestDeleteLine;
import static nextstep.subway.line.LineSteps.requestGetLine;
import static nextstep.subway.line.LineSteps.requestGetLines;
import static nextstep.subway.line.LineSteps.requestUpdateLine;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void createLine() {
        // when
        ExtractableResponse<Response> response = requestCreateLineDx();

        // then
        assertCreateLine(response);
    }

    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = requestCreateLineDx();
        ExtractableResponse<Response> createResponse2 = requestCreateLine2();

        // when
        ExtractableResponse<Response> response = requestGetLines();

        // then
        assertGetLines(response);
        assertIncludeLines(createResponse1, createResponse2, response);
    }

    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = requestCreateLineDx();

        // when
        ExtractableResponse<Response> response = requestGetLine(createResponse);

        // then
        assertGetLine(response);
    }

    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = requestCreateLineDx();

        // when
        ExtractableResponse<Response> response = requestUpdateLine(createResponse);

        // then
        assertUpdateLine(response);
    }

    @Test
    @DisplayName("지하철 노선을 제거한다.")
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = requestCreateLineDx();

        // when
        ExtractableResponse<Response> response = requestDeleteLine(createResponse);

        // then
        assertDeleteLine(response);
    }

}
