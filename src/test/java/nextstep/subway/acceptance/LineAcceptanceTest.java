package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.step.LineSteps;
import nextstep.subway.acceptance.step.StationSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    final String 이름 = "name";
    final String 번호 = "id";

    final Map<String, String> 신사역 = Map.of(이름, "신사역");
    final Map<String, String> 광교역 = Map.of(이름, "광교역");

    final Map<String, String> 성수역 = Map.of(이름, "성수역");
    final Map<String, String> 신설동역 = Map.of(이름, "신설동역");

    /**
     * Given 상행, 하행 종점역 생성을 요청 하고
     * When 지하철 노선 생성을 요청하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        final Long upStationId = StationSteps.지하철_역_생성_요청(신사역).jsonPath().getLong(번호);
        final Long downStationId = StationSteps.지하철_역_생성_요청(광교역).jsonPath().getLong(번호);

        // when
        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성_요청("신분당선", "bg-red-600", upStationId, downStationId, 100);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();
    }

    /**
     * When 중복된 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복된 지하철 노선 생성")
    @Test
    void createDuplicateStation() {
        // given
        final Long upStationId = StationSteps.지하철_역_생성_요청(신사역).jsonPath().getLong(번호);
        final Long downStationId = StationSteps.지하철_역_생성_요청(광교역).jsonPath().getLong(번호);

        LineSteps.지하철_노선_생성_요청("신분당선", "bg-red-600", upStationId, downStationId, 100);

        // when
        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성_요청("신분당선", "bg-red-600", upStationId, downStationId, 100);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
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
        //given
        final Long upStationId1 = StationSteps.지하철_역_생성_요청(신사역).jsonPath().getLong(번호);
        final Long downStationId1 = StationSteps.지하철_역_생성_요청(광교역).jsonPath().getLong(번호);
        LineSteps.지하철_노선_생성_요청("신분당선", "bg-red-600", upStationId1, downStationId1, 100);

        final Long upStationId2 = StationSteps.지하철_역_생성_요청(성수역).jsonPath().getLong(번호);
        final Long downStationId2 = StationSteps.지하철_역_생성_요청(신설동역).jsonPath().getLong(번호);
        LineSteps.지하철_노선_생성_요청("2호선", "bg-green-600", upStationId2, downStationId2, 50);

        //when
        ExtractableResponse<Response> searchResponse = LineSteps.지하철_노선_조회_요청();

        // then
        assertThat(searchResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> lineNames = searchResponse.jsonPath().getList(이름);
        assertThat(lineNames).contains("신분당선", "2호선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        //given
        final Long upStationId1 = StationSteps.지하철_역_생성_요청(신사역).jsonPath().getLong(번호);
        final Long downStationId1 = StationSteps.지하철_역_생성_요청(광교역).jsonPath().getLong(번호);
        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성_요청("신분당선", "bg-red-600", upStationId1, downStationId1, 100);

        // when
        final String uri = createResponse.header("Location");
        ExtractableResponse<Response> searchResponse = LineSteps.지하철_노선_조회_요청(uri);

        // then
        assertThat(searchResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        final Long upStationId1 = StationSteps.지하철_역_생성_요청(신사역).jsonPath().getLong(번호);
        final Long downStationId1 = StationSteps.지하철_역_생성_요청(광교역).jsonPath().getLong(번호);
        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성_요청("신분당선", "bg-red-600", upStationId1, downStationId1, 100);

        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "구분당선");
        params.put("color", "bg-blue-600");

        // when
        final String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = LineSteps.지하철_노선_수정_요청(uri, params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //given
        final Long upStationId1 = StationSteps.지하철_역_생성_요청(신사역).jsonPath().getLong(번호);
        final Long downStationId1 = StationSteps.지하철_역_생성_요청(광교역).jsonPath().getLong(번호);
        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성_요청("신분당선", "bg-red-600", upStationId1, downStationId1, 100);

        // when
        final String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = LineSteps.지하철_노선_삭제_요청(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
