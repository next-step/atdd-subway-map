package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    private Long 강남역id;
    private Long 양재역id;
    private Long 판교역id;
    private Long 역삼역id;

    private Map<String, String> 신분당선;
    private Map<String, String> 이호선;
    private Map<String, String> 구분당선;

    private Map<String, String> 양재_판교_구간;
    private Map<String, String> 역삼_판교_구간;
    private Map<String, String> 양재_강남_구간;

    @BeforeEach
    void initParam() {
        강남역id = 지하철_노선_id(지하철역_생성_요청(강남역));
        양재역id = 지하철_노선_id(지하철역_생성_요청(양재역));
        판교역id = 지하철_노선_id(지하철역_생성_요청(판교역));
        역삼역id = 지하철_노선_id(지하철역_생성_요청(역삼역));

        신분당선 = 지하철_노선_파라미터("신분당선", "bg-red-600", 강남역id, 양재역id, 1600);
        이호선 = 지하철_노선_파라미터("2호선", "bg-green-600", 강남역id, 역삼역id, 1600);

        구분당선 = new HashMap<>();
        구분당선.put("name", "구분당선");
        구분당선.put("color", "bg-blue-600");

        양재_판교_구간 = 구간_파라미터(양재역id, 판교역id, 12700);
        역삼_판교_구간 = 구간_파라미터(역삼역id, 판교역id, 999);
        양재_강남_구간 = 구간_파라미터(양재역id, 강남역id, 999);
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
        지하철_노선_생성됨(response, 신분당선);
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
        ExtractableResponse<Response> createResponse1 = 지하철_노선_등록되어_있음(신분당선);
        ExtractableResponse<Response> createResponse2 = 지하철_노선_등록되어_있음(이호선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_조회됨(response, createResponse1, createResponse2);
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
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(uri);

        // then
        지하철_노선_조회됨(response, createResponse);
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
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(uri, 구분당선);

        // then
        ExtractableResponse<Response> showResponse = 지하철_노선_조회_요청(uri);
        지하철_노선_수정됨(response, showResponse, 구분당선);
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
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(uri);

        // then
        지하철_노선_삭제됨(response);
    }

    /**
     * Scenario: 중복이름으로 지하철 노선 생성
     * Given 지하철 노선 생성 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선 중복이름으로 생성")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        지하철_노선_이름_중복됨(response);
    }

    /**
     * Scenario: 구간 등록(정상적인 시나리오)
     * Given 지하철 노선 생성 요청 하고
     * When 구간 등록 요청하면
     * Then 구간 등록이 성공한다.
     */
    @DisplayName("구간 등록")
    @Test
    void addSection() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        Long 신분당선id = 지하철_노선_id(createResponse);
        ExtractableResponse<Response> response = 구간_등록_요청(신분당선id, 양재_판교_구간);

        // then
        구간_등록됨(response, 양재_판교_구간);
    }

    /**
     * Scenario: 구간 등록(비정상적인 시나리오)
     * Given 지하철 노선 생성(상행:강남역, 하행:양재역) 요청 하고
     * When 잘못된 구간 등록(상행:역삼역, 하행:판교역) 요청하면
     * Then 구간 등록이 실패한다.
     */
    @DisplayName("구간 등록 실패 - 새로운 구간의 상행역은 해당 노선의 하행 종점역이어야 함.")
    @Test
    void addSectionFailUpStation() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        Long 신분당선id = 지하철_노선_id(createResponse);
        ExtractableResponse<Response> response = 구간_등록_요청(신분당선id, 역삼_판교_구간);

        // then
        구간_등록_실패됨(response);
    }

    /**
     * Scenario: 구간 등록(비정상적인 시나리오)
     * Given 지하철 노선 생성(상행:강남역, 하행:양재역) 요청 하고
     * When 잘못된 구간 등록(상행:양재역, 하행:강남역) 요청하면
     * Then 구간 등록이 실패한다.
     */
    @DisplayName("구간 등록 실패 - 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.")
    @Test
    void addSectionFailDownStation() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        Long 신분당선id = 지하철_노선_id(createResponse);
        ExtractableResponse<Response> response = 구간_등록_요청(신분당선id, 양재_강남_구간);

        // then
        구간_등록_실패됨(response);
    }

    /**
     * Scenario: 구간 제거(정상적인 시나리오)
     * Given 지하철 노선 생성(상행:강남역, 하행:양재역) 요청 하고
     * Given 신분당선에 구간 등록(상행:양재역, 하행:판교역) 요청 하고
     * When 구간(판교역) 제거 요청하면
     * Then 구간(양재_판교_구간) 제거가 성공한다.
     */
    @DisplayName("구간 제거")
    @Test
    void removeSection() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(신분당선);
        Long 신분당선id = 지하철_노선_id(createResponse);
        구간_등록되어_있음(신분당선id, 양재_판교_구간);

        // when
        ExtractableResponse<Response> response = 구간_제거_요청(신분당선id, 판교역id);

        // then
        구간_제거됨(response);
    }

    /**
     * Scenario: 구간 제거(실패 - 하행 좀점역 이외의 역 제거)
     * Given 지하철 노선 생성(상행:강남역, 하행:양재역) 요청 하고
     * Given 신분당선에 구간 등록(상행:양재역, 하행:판교역) 요청 하고
     * When 구간(양재역) 제거 요청하면
     * Then 구간 제거가 실패한다.
     */
    @DisplayName("구간 제거 실패 - 하행 종점역 이외의 역을 제거")
    @Test
    void removeSectionFailNotLastStation() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(신분당선);
        Long 신분당선id = 지하철_노선_id(createResponse);
        구간_등록되어_있음(신분당선id, 양재_판교_구간);

        // when
        ExtractableResponse<Response> response = 구간_제거_요청(신분당선id, 양재역id);

        // then
        구간_제거_실패됨(response);
    }

    /**
     * Scenario: 구간 제거(실패 - 구간 1개일 때 제거)
     * Given 지하철 노선 생성(상행:강남역, 하행:양재역) 요청 하고
     * When 구간(양재역) 제거 요청하면
     * Then 구간 제거가 실패한다.
     */
    @DisplayName("구간 제거 실패 - 하행 종점역 이외의 역을 제거")
    @Test
    void removeSectionFailUniqueSection() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        Long 신분당선id = 지하철_노선_id(createResponse);
        ExtractableResponse<Response> response = 구간_제거_요청(신분당선id, 양재역id);

        // then
        구간_제거_실패됨(response);
    }
}
