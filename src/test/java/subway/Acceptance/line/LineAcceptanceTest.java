package subway.Acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.Acceptance.AcceptanceTest;

import java.util.List;

import static subway.Acceptance.line.LineAcceptanceFixture.분당선_생성_요청;
import static subway.Acceptance.line.LineAcceptanceFixture.신분당선_생성_요청;
import static subway.Acceptance.line.LineAcceptanceStep.*;
import static subway.Acceptance.station.StationAcceptanceStep.지하철역_생성_요청;

@DisplayName("노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setStations() {
        지하철역_생성_요청("강남역");
        지하철역_생성_요청("역삼역");
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

}
