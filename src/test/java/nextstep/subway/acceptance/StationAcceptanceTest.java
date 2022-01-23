package nextstep.subway.acceptance;

import nextstep.subway.acceptance.rest.BaseCrudStep;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    private final String STATION_PATH = "/stations";

    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // given
        var params = giveMeStationRequest("강남역");

        // when
        var response = BaseCrudStep.createResponse(STATION_PATH, params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
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
        var params1 = giveMeStationRequest("강남역");
        var createResponse1 = BaseCrudStep.createResponse(STATION_PATH, params1);

        var params2 = giveMeStationRequest("역삼역");
        var createResponse2 = BaseCrudStep.createResponse(STATION_PATH, params2);

        // when
        var response = BaseCrudStep.readResponse(STATION_PATH);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).contains(params1.get("name"), params2.get("name"));
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
        var params = giveMeStationRequest("강남역");
        var createResponse = BaseCrudStep.createResponse(STATION_PATH, params);

        // when
        String uri = createResponse.header("Location");
        var response = BaseCrudStep.deleteResponse(uri);

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
        var params = giveMeStationRequest("강남역");
        var createResponse = BaseCrudStep.createResponse(STATION_PATH, params);

        // when
        var response = BaseCrudStep.createResponse(STATION_PATH, params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    private Map<String, String> giveMeStationRequest(String name) {
        return Map.of("name", name);
    }
}
