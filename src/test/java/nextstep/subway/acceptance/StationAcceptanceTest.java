package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.fixture.StationFixture;
import nextstep.subway.acceptance.step.StationStep;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // given
        var params = StationFixture.강남역;

        // when
        var response = StationStep.역_생성_요청(params);

        // then
        역_생성_완료(response);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * Given 새로운 지하철역 생성을 요청 하고
     * When 지하철역 목록 조회를 요청 하면
     * Then 두 지하철역이 포함된 지하철역 목록을 응답받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void getStations() {
        /// given
        var params1 = StationFixture.강남역;
        var createResponse1 = StationStep.역_생성_요청(params1);

        var params2 = StationFixture.역삼역;
        var createResponse2 = StationStep.역_생성_요청(params2);

        // when
        var response = StationStep.역_목록_조회_요청();

        역_목록_조회_완료(response, params1, params2);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 생성한 지하철역 삭제를 요청 하면
     * Then 생성한 지하철역 삭제가 성공한다.
     */
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {
        // given
        var params = StationFixture.강남역;
        var createResponse = StationStep.역_생성_요청(params);

        // when
        String uri = createResponse.header("Location");
        var response = StationStep.역_삭제_요청(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철역 생성")
    @Test
    void createStationWithDuplicateName() {
        // given
        var params = StationFixture.강남역;
        var createResponse = StationStep.역_생성_요청(params);

        // when
        var response = StationStep.역_생성_요청(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    private void 역_생성_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 역_목록_조회_완료(ExtractableResponse<Response> response, Map<String, String>... paramsArgs) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).contains(Arrays.stream(paramsArgs).map(m -> m.get("name")).toArray(String[]::new));
    }
}
