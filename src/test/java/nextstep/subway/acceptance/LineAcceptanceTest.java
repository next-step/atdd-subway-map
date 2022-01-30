package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.steps.StationSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static nextstep.subway.fixture.TLine.*;
import static nextstep.subway.steps.LineSteps.*;
import static nextstep.subway.steps.StationSteps.지하철_역_생성_요청;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setup() {
        지하철_역_생성_요청("역1");
        지하철_역_생성_요청("역2");
        지하철_역_생성_요청("역3");
    }

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        노선_생성_성공(response);
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
        지하철_노선_생성_요청(신분당선);
        지하철_노선_생성_요청(구분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        노선_목록_조회_성공(response, 신분당선, 구분당선);
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
        지하철_노선_생성_요청(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(1L);

        // then
        노선_조회_성공(response, 신분당선);
    }

    /**
     * When 없는 지하철 노선 조회를 요청 하면
     * Then 404 응답을 받는다
     */
    @DisplayName("지하철 노선 조회 - 없는 노선")
    @Test
    void getInvalidLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(999L);

        // then
        노선_조회_실패_없는_노선(response);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정 - 존재 하는 경우 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(신분당선);

        // when
        String uri = createResponse.header(HttpHeaders.LOCATION);
        ExtractableResponse<Response> response = 지하철_노선_변경_요청(uri, 구분당선);

        // then
        노선_변경_성공(response);
    }

    /**
     * When 없는 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선이 생성이 성공한다.
     */
    @DisplayName("지하철 노선 수정 - 없는 경우 NOT FOUND")
    @Test
    void updateNotExistLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_변경_요청("/lines/1", 구분당선);

        // then
        노선_변경_실패_없는_노선(response);
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
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(신분당선);

        // when
        String uri = createResponse.header(HttpHeaders.LOCATION);
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(uri);

        // then
        노선_삭제_성공(response);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void duplicatedStationName() {
        // given
        지하철_노선_생성_요청(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        노선_생성_실패_중복(response);
    }

}
