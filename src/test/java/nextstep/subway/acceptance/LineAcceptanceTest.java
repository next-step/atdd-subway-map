package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.step.StationStep;
import nextstep.subway.fixture.LineFixture;
import nextstep.subway.acceptance.step.LineStep;
import nextstep.subway.fixture.StationFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
        var station1 = StationFixture.신논현역;
        var station2 = StationFixture.강남역;
        StationStep.역_생성_요청(station1);
        StationStep.역_생성_요청(station2);

        var params = LineFixture.신분당선;

        // when
        var response = LineStep.노선_생성_요청(params);

        // then
        노선_생성_완료(response);
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
        var station1 = StationFixture.신논현역;
        var station2 = StationFixture.강남역;
        var station3 = StationFixture.역삼역;
        StationStep.역_생성_요청(station1);
        StationStep.역_생성_요청(station2);
        StationStep.역_생성_요청(station3);

        var params1 = LineFixture.신분당선;
        LineStep.노선_생성_요청(params1);

        var params2 = LineFixture.이호선;
        LineStep.노선_생성_요청(params2);

        // when
        var response = LineStep.노선_목록_조회_요청();

        // then
        노선_목록_조회_완료(response, params1, params2);
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
        var station1 = StationFixture.신논현역;
        var station2 = StationFixture.강남역;
        StationStep.역_생성_요청(station1);
        StationStep.역_생성_요청(station2);

        var params = LineFixture.신분당선;
        var createResponse = LineStep.노선_생성_요청(params);

        // when
        var uri = createResponse.header("Location");
        var response = LineStep.노선_조회_요청(uri);

        // then
        노선_조회_완료(response, params);
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
        var station1 = StationFixture.신논현역;
        var station2 = StationFixture.강남역;
        StationStep.역_생성_요청(station1);
        StationStep.역_생성_요청(station2);

        var params = LineFixture.신분당선;
        var createResponse = LineStep.노선_생성_요청(params);

        // when
        var modifyParams = LineFixture.이호선;

        var uri = createResponse.header("Location");
        var response = LineStep.노선_수정_요청(uri, modifyParams);

        // then
        노선_수정_완료(response, modifyParams);
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
        var station1 = StationFixture.신논현역;
        var station2 = StationFixture.강남역;
        StationStep.역_생성_요청(station1);
        StationStep.역_생성_요청(station2);

        var params = LineFixture.신분당선;
        var createResponse = LineStep.노선_생성_요청(params);

        // when
        var uri = createResponse.header("Location");
        var response = LineStep.노선_삭제_요청(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복 이름으로 지하철 노선 생성")
    @Test
    void createLineWithDuplicateName() {
        // given
        var station1 = StationFixture.신논현역;
        var station2 = StationFixture.강남역;
        StationStep.역_생성_요청(station1);
        StationStep.역_생성_요청(station2);

        var params = LineFixture.신분당선;
        var createResponse = LineStep.노선_생성_요청(params);

        // when
        var response = LineStep.노선_생성_요청(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    private void 노선_생성_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 노선_목록_조회_완료(ExtractableResponse<Response> response, Map<String, Object>... paramsArgs) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        var lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).contains(Arrays.stream(paramsArgs).map(m -> m.get("name")).toArray());
    }

    private void 노선_조회_완료(ExtractableResponse<Response> response, Map<String, Object> params) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        var lineName = response.jsonPath().getString("name");
        assertThat(lineName).isEqualTo(params.get("name"));
    }

    private void 노선_수정_완료(ExtractableResponse<Response> response, Map<String, Object> modifyParams) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        var lineName = response.jsonPath().getString("name");
        assertThat(lineName).isEqualTo(modifyParams.get("name"));
    }
}
