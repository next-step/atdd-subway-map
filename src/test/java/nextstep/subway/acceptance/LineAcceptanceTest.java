package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.LineUtils;
import nextstep.subway.utils.StationUtils;
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
        final Long upStationId = 지하철역_생성요청_및_생성한_지하철역_가져오기("1호선상행역");
        final Long downStationId = 지하철역_생성요청_및_생성한_지하철역_가져오기("1호선하행역");

        // when
        final Map<String, Object> ONE_LINE = 지하철_노선_데이터_생성("1호선", "blue darken-4", upStationId, downStationId, 1);
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
        final String lineName = "1호선";
        상하행역을_포함한_지하철_노선_생성요청(lineName);
        final String newLineName = "7호선";
        상하행역을_포함한_지하철_노선_생성요청(newLineName);

        // when
        final ExtractableResponse<Response> responseList = 지하철_모든_노선_목록요청();

        // then
        생성요청한_지하철_노선들이_포함된_응답을_받음(responseList, Arrays.asList(lineName, newLineName));
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
        final String oneLineName = "1호선";
        final ExtractableResponse<Response> createResponse = 상하행역을_포함한_지하철_노선_생성요청(oneLineName);

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> getResponse = 지하철_노선_목록요청(uri);

        // then
        생성요청한_지하철_노선이_포함된_응답을_받음(getResponse, oneLineName);
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
        final String line = "1호선";
        final ExtractableResponse<Response> createResponse = 상하행역을_포함한_지하철_노선_생성요청(line);

        // when
        final String uri = createResponse.header("Location");
        final String editedLineName = "7호선";
        final Map<String, Object> editedLineParam = 상하행역을_포함한_지하철_노선_데이터_생성(editedLineName);

        final ExtractableResponse<Response> editResponse = 지하철_노선_수정요청(editedLineParam, uri);

        // then
        지하철노선_수정요청이_성공함(editResponse, editedLineName);
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
        final String line = "1호선";
        final ExtractableResponse<Response> createResponse = 상하행역을_포함한_지하철_노선_생성요청(line);

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> deleteResponse = 지하철_노선_삭제요청(uri);

        // then
        삭제요청한_지하철_노선이_존재하지_않음(deleteResponse);
    }

    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void duplicateCheck() {
        // given
        final String lineName = "1호선";
        final Map<String, Object> lineParam = 상하행역을_포함한_지하철_노선_데이터_생성(lineName);
        LineUtils.지하철_노선_생성요청(lineParam);

        // when
        final String newLineName = "7호선";
        final Map<String, Object> duplicateParam = 상하행역을_포함한_지하철_노선_데이터_생성(newLineName);
        duplicateParam.put("name", lineName);
        final ExtractableResponse<Response> duplicateResponse = 지하철_노선_생성요청(duplicateParam);

        // then
        중복이름으로_지하철_노선_생성_실패함(duplicateResponse);
    }


    private Long 지하철역_생성요청_및_생성한_지하철역_가져오기(String stationName) {
        return Long.valueOf(
                StationUtils.지하철_역_생성_요청(
                                StationUtils.지하철_역_데이터_생성(stationName)
                        )
                        .jsonPath()
                        .get("id")
                        .toString());
    }

    private Map<String, Object> 상하행역을_포함한_지하철_노선_데이터_생성(String lineName) {
        final Long upStationId = this.지하철역_생성요청_및_생성한_지하철역_가져오기(lineName + "상행역");
        final Long downStationId = this.지하철역_생성요청_및_생성한_지하철역_가져오기(lineName + "하행역");
        final Map<String, Object> LINE = LineUtils.지하철_노선_데이터_생성(lineName, "blue darken-4", upStationId, downStationId, 1);
        return LINE;
    }

    private ExtractableResponse<Response> 상하행역을_포함한_지하철_노선_생성요청(String lineName) {
        final Map<String, Object> LINE = 상하행역을_포함한_지하철_노선_데이터_생성(lineName);
        return LineUtils.지하철_노선_생성요청(LINE);
    }
}
