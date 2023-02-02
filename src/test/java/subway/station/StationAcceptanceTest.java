package subway.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import subway.utils.RestAssuredClient;

import java.util.Arrays;
import java.util.Map;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("주어진 이름을 가진 지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        var station = RestAssuredClient.createStation(Map.ofEntries(entry("name", "강남역")));
        assertThat(station.jsonPath().getLong("id")).isEqualTo(1);
        assertThat(station.jsonPath().getString("name")).isEqualTo("강남역");

        // when
        var stations = RestAssuredClient.listStation();

        // then
        assertThat(stations.jsonPath().getList("$")).hasSize(1);
        // TODO 더 깔끔하게 첫번째 원소의 id 접근하는 방법 없을까?
        assertThat(stations.jsonPath().getList("id", Long.class).get(0)).isEqualTo(1L);
        assertThat(stations.jsonPath().getList("name", String.class).get(0)).isEqualTo("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다")
    @Test
    void listStation() {
        // given
        RestAssuredClient.createStation(Map.ofEntries(entry("name", "강남역")));
        RestAssuredClient.createStation(Map.ofEntries(entry("name", "신논현역")));

        // when
        var stations = RestAssuredClient.listStation();

        // then
        assertThat(stations.jsonPath().getList("$")).hasSize(2);
        assertThat(stations.jsonPath().getList("name", String.class)).containsAll(Arrays.asList("강남역", "신논현역"));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("전달받은 지하철역을 제거한다")
    @Test
    void deleteStation() {
        // given
        RestAssuredClient.createStation(Map.ofEntries(entry("name", "강남역")));
        var stationsBefore = RestAssuredClient.listStation();
        assertThat(stationsBefore.jsonPath().getList("$")).hasSize(1);

        // when
        RestAssuredClient.deleteStation(1L);

        // then
        var stationsAfter = RestAssuredClient.listStation();
        assertThat(stationsAfter.jsonPath().getList("$")).isEmpty();
    }
}
