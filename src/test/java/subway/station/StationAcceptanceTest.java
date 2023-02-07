package subway.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.common.AcceptanceTest;
import subway.common.Endpoints;
import subway.utils.RestAssuredClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.TestHelper.응답_코드가_일치한다;
import static subway.station.StationFixtures.강남역_생성_요청;
import static subway.station.StationFixtures.서울대입구역_생성_요청;
import static subway.station.StationFixtures.지하철역을_생성한다;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        var response = RestAssuredClient.post(Endpoints.STATIONS, 강남역_생성_요청);
        응답_코드가_일치한다(response.statusCode(), HttpStatus.CREATED);

        // then
        List<String> stationNames = RestAssuredClient.get(Endpoints.STATIONS)
                .jsonPath()
                .getList("name", String.class);

        assertThat(stationNames).containsAnyOf("강남역");
    }

    @DisplayName("등록된 모든 지하철역을 조회한다.")
    @Test
    void findAllStations() {
        // Given 2개의 지하철역을 생성하고
        지하철역을_생성한다(강남역_생성_요청);
        지하철역을_생성한다(서울대입구역_생성_요청);

        // When 지하철역 목록을 조회하면
        List<String> stationNames = RestAssuredClient.get(Endpoints.STATIONS)
                .jsonPath()
                .getList("name", String.class);

        // Then 2개의 지하철역을 응답받는다.
        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).containsExactly("강남역", "서울대입구역");
    }

    @DisplayName("등록된 지하철을 삭제하면 삭제된 역은 조회할 수 없다.")
    @Test
    void deleteAllStations() {
        // Given 지하철역을 생성하고
        Long 강남역_아이디 = 지하철역을_생성한다(강남역_생성_요청);
        Long 서울대입구역_아이디 = 지하철역을_생성한다(서울대입구역_생성_요청);

        // When 그 지하철역을 삭제하면
        RestAssuredClient.delete(Endpoints.endpointWithParam(Endpoints.STATIONS, 강남역_아이디));

        // Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
        List<Long> stationIds = RestAssuredClient.get(Endpoints.STATIONS)
                .jsonPath()
                .getList("id", Long.class);

        assertThat(stationIds).containsExactly(서울대입구역_아이디);
        assertThat(stationIds).doesNotContain(강남역_아이디);
    }
}
