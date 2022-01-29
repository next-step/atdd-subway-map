package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static nextstep.subway.acceptance.LineStepDefinition.*;
import static nextstep.subway.acceptance.StationStepDefinition.지하철역_생성_요청;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    Long 강남역;
    Long 양재역;
    String 신분당선 = "신분당선";
    Map<String, String> createParams;
    ExtractableResponse<Response> createResponse;
    String createUri;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_생성_요청("강남역").body().jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").body().jsonPath().getLong("id");

        createParams = 지하철_노선_파라미터_생성(
                신분당선,
                "bg-red-600",
                강남역,
                양재역,
                10);
        createResponse = 지하철_노선_생성_요청(createParams);
        createUri = createResponse.header("Location");
    }
    /**
     * Given 상행 지하철역 생성하고
     * Given 하행 지하철역 생성하고
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // then
        지하철_노선_생성_완료(createResponse);
    }

    /**
     * Given 상행 지하철역 생성하고
     * Given 하행 지하철역 생성하고
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        Long 신도림역 = 지하철역_생성_요청("신도림역").body().jsonPath().getLong("id");
        Long 문래역 = 지하철역_생성_요청("문래역").body().jsonPath().getLong("id");
        String 호선2 = "2호선";
        Map<String, String> params = 지하철_노선_파라미터_생성(
                호선2,
                "bg-green-600",
                신도림역,
                문래역,
                10);
        지하철_노선_생성_요청(createParams);
        지하철_노선_생성_요청(params);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청("/lines");

        // then
        지하철_노선_목록_조회_완료(response, 신분당선, 호선2);
    }

    /**
     * Given 상행 지하철역 생성하고
     * Given 하행 지하철역 생성하고
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createUri);

        // then
        지하철_노선_조회_완료(response, 신분당선);
    }

    /**
     * Given 상행 지하철역 생성하고
     * Given 하행 지하철역 생성하고
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // when
        String 구분당선 = "구분당선";
        Map<String, String> updateParams = 지하철_노선_파라미터_생성(
                구분당선,
                "bg-blue-600",
                강남역,
                양재역,
                10);
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(createUri, updateParams);

        // then
        지하철_노선_수정_완료(response, updateParams);
    }

    /**
     * Given 상행 지하철역 생성하고
     * Given 하행 지하철역 생성하고
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(createUri);

        // then
        지하철_노선_삭제_완료(response);
    }

    /**
     * Scenario: 중복이름으로 지하철 노선 생성
     * Given 상행 지하철역 생성하고
     * Given 하행 지하철역 생성하고
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void createLineWithDuplicateName() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(createParams);

        // then
        지하철_노선_생성_실패(response);
    }
}
