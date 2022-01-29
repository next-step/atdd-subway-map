package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static nextstep.subway.utils.LineUtils.*;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        final Map<String, String> ONE_LINE = 지하철_노선_데이터_생성("1호선", "blue darken-4");

        // when
        final ExtractableResponse<Response> response = 지하철_노선_생성요청(ONE_LINE);

        // then
        생성_요청한_지하철_노선_생성됨(response);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        final Map<String, String> LINE = 지하철_노선_데이터_생성("1호선", "blue darken-4");
        final Map<String, String> NEW_LINE = 지하철_노선_데이터_생성("7호선", "green darken-3");

        지하철_노선_생성요청(Arrays.asList(LINE, NEW_LINE));

        // when
        final ExtractableResponse<Response> responseList = 지하철_모든_노선_목록요청();

        // then
        생성요청한_지하철_노선들이_포함된_응답을_받음(responseList, Arrays.asList("1호선", "7호선"));
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        final String ONE_LINE_NAME = "1호선";

        final Map<String, String> ONE_LINE = 지하철_노선_데이터_생성(ONE_LINE_NAME, "blue darken-4");
        final ExtractableResponse<Response> createResponse = 지하철_노선_생성요청(ONE_LINE);

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> getResponse = 지하철_노선_목록요청(uri);

        // then
        생성요청한_지하철_노선이_포함된_응답을_받음(getResponse, ONE_LINE_NAME);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        final Map<String, String> ONE_LINE = 지하철_노선_데이터_생성("1호선", "blue darken-4");
        final ExtractableResponse<Response> response = 지하철_노선_생성요청(ONE_LINE);

        // when
        final String uri = response.header("Location");
        final Map<String, String> editedLine = 지하철_노선_데이터_생성("7호선", "green darken-3");

        final ExtractableResponse<Response> editResponse = 지하철_노선_수정요청(editedLine, uri);

        // then
        지하철노선_수정요청이_성공함(editResponse, "7호선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        final Map<String, String> ONE_LINE = 지하철_노선_데이터_생성("1호선", "blue darken-4");
        final ExtractableResponse<Response> response = 지하철_노선_생성요청(ONE_LINE);

        // when
        final String uri = response.header("Location");
        final ExtractableResponse<Response> deleteResponse = 지하철_노선_삭제요청(uri);

        // then
        삭제요청한_지하철_노선이_존재하지_않음(deleteResponse);
    }

    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void duplicateCheck() {
        // given
        final Map<String, String> ONE_LINE = 지하철_노선_데이터_생성("1호선", "blue darken-4");
        지하철_노선_생성요청(ONE_LINE);

        // when
        final ExtractableResponse<Response> duplicateResponse = 지하철_노선_생성요청(ONE_LINE);

        // then
        중복이름으로_지하철_노선_생성_실패함(duplicateResponse);
    }
}
