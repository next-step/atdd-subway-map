package subway.Acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.Acceptance.AcceptanceTest;

import java.util.List;

import static subway.Acceptance.line.LineAcceptanceFixture.*;
import static subway.Acceptance.line.LineAcceptanceStep.*;
import static subway.Acceptance.station.StationAcceptanceStep.지하철역_생성_요청;

@DisplayName("노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setStations() {
        지하철역_생성_요청("강남역");
        지하철역_생성_요청("역삼역");
        지하철역_생성_요청("선릉역");
        지하철역_생성_요청("삼성역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> createResponse = 노선_생성_요청(신분당선_생성_요청);

        // then
        노선_생성됨(createResponse);

        // then
        ExtractableResponse<Response> response = 노선_목록_조회_요청();
        노선_목록_포함됨(response, List.of(createResponse));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void showLines() {
        // given
        ExtractableResponse<Response> createResponse1 = 노선_생성_요청(신분당선_생성_요청);
        ExtractableResponse<Response> createResponse2 = 노선_생성_요청(분당선_생성_요청);

        // when
        ExtractableResponse<Response> response = 노선_목록_조회_요청();

        // then
        노선_목록_포함됨(response, List.of(createResponse1, createResponse2));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void showLine() {
        // given
        ExtractableResponse<Response> createResponse = 노선_생성_요청(신분당선_생성_요청);

        // when
        ExtractableResponse<Response> response = 노선_조회_요청(createResponse);

        // then
        노선_조회됨(response);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void modifyLine() {
        // given
        ExtractableResponse<Response> createResponse = 노선_생성_요청(신분당선_생성_요청);

        // when
        ExtractableResponse<Response> response = 노선_수정_요청(createResponse);

        // then
        노선_수정됨(response);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 노선_생성_요청(신분당선_생성_요청);

        // when
        ExtractableResponse<Response> response = 노선_삭제_요청(createResponse);

        // then
        노선_삭제됨(response);
    }

    /**
     * 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
     * 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
     * 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
     */
    @DisplayName("지하철 노선 구간 등록 성공")
    @Test
    void createLineSection() {
        // given
        ExtractableResponse<Response> createResponse = 노선_생성_요청(신분당선_생성_요청);

        // when
        ExtractableResponse<Response> response = 노선_구간_등록_요청(createResponse, 구간_등록_요청_성공);

        // then
        노선_구간_생성됨(response);
    }

    @DisplayName("지하철 노선 구간 등록 실패 : 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void createLineSection_fail1() {
        // given
        ExtractableResponse<Response> createResponse = 노선_생성_요청(신분당선_생성_요청);

        // when
        ExtractableResponse<Response> response = 노선_구간_등록_요청(createResponse, 구간_등록_요청_실패1);

        // then
        노선_구간_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 구간 등록 실패 : 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.")
    @Test
    void createLineSection_fail2() {
        // given
        ExtractableResponse<Response> createResponse = 노선_생성_요청(신분당선_생성_요청);

        // when
        ExtractableResponse<Response> response = 노선_구간_등록_요청(createResponse, 구간_등록_요청_실패2);

        // then
        노선_구간_생성_실패됨(response);
    }

    /**
     * 지하철 노선에 구간을 제거하는 기능 구현
     * 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
     * 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
     * 새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.
     */
    @DisplayName("지하철 노선 구간 삭제 성공")
    @Test
    void deleteLineSection() {
        // given
        ExtractableResponse<Response> createResponse = 노선_생성_요청(신분당선_생성_요청);

        // when
        노선_구간_등록_요청(createResponse, 구간_등록_요청_성공);
        ExtractableResponse<Response> response = 노선_구간_삭제_요청(createResponse, 3L);

        // then
        노선_구간_삭제됨(response);
    }

    @DisplayName("지하철 노선 구간 삭제 실패 : 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다.")
    @Test
    void deleteLineSection_fail1() {
        // given
        ExtractableResponse<Response> createResponse = 노선_생성_요청(신분당선_생성_요청);

        // when
        노선_구간_등록_요청(createResponse, 구간_등록_요청_성공);
        ExtractableResponse<Response> response = 노선_구간_삭제_요청(createResponse, 2L);

        // then
        노선_구간_삭제_실패됨(response);
    }

    @DisplayName("지하철 노선 구간 삭제 실패 : 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.")
    @Test
    void deleteLineSection_fail2() {
        // given
        ExtractableResponse<Response> createResponse = 노선_생성_요청(신분당선_생성_요청);

        // when
        ExtractableResponse<Response> response = 노선_구간_삭제_요청(createResponse, 3L);

        // then
        노선_구간_삭제_실패됨(response);
    }
}
