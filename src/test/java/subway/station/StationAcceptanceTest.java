package subway.station;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import subway.common.DatabaseCleanser;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanser databaseCleanser;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        databaseCleanser.execute();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("주어진 이름을 가진 지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        var newStation = StationRestAssuredClient.createStation(
                Map.ofEntries(
                        entry("name", "강남역")
                )
        );
        assertThat(newStation.jsonPath().getLong("id")).isEqualTo(1);
        assertThat(newStation.jsonPath().getString("name")).isEqualTo("강남역");

        // then
        var stations = StationRestAssuredClient.listStation();

        // then
        assertThat(stations.jsonPath().getList("$")).hasSize(1);
        assertAll(
                () -> assertThat(stations.jsonPath().getList("$")).hasSize(1),
                () -> assertThat(stations.jsonPath().getLong("[0].id")).isEqualTo(1),
                () -> assertThat(stations.jsonPath().getString("[0].name")).isEqualTo("강남역")
        );
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
        Fixture.createStations(
                List.of(
                        Map.ofEntries(
                                entry("name", "강남역")
                        ),
                        Map.ofEntries(
                                entry("name", "신논현역")
                        )
                )
        );

        // when
        var stations = StationRestAssuredClient.listStation();

        // then
        assertAll(
                () -> assertThat(stations.jsonPath().getList("$")).hasSize(2),
                () -> assertThat(stations.jsonPath().getLong("[0].id")).isEqualTo(1),
                () -> assertThat(stations.jsonPath().getString("[0].name")).isEqualTo("강남역"),
                () -> assertThat(stations.jsonPath().getLong("[1].id")).isEqualTo(2),
                () -> assertThat(stations.jsonPath().getString("[1].name")).isEqualTo("신논현역")
        );
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
        Fixture.createStation(
                Map.ofEntries(
                        entry("name", "강남역")
                )
        );

        // then
        var stationsBefore = StationRestAssuredClient.listStation();
        assertThat(stationsBefore.jsonPath().getList("$")).hasSize(1);

        // when
        StationRestAssuredClient.deleteStation(1L);

        // then
        var stationsAfter = StationRestAssuredClient.listStation();
        assertThat(stationsAfter.jsonPath().getList("$")).isEmpty();
    }

    private static class Fixture {
        private static void createStations(List<Map<String, Object>> stations) {
            for (var station : stations) {
                StationRestAssuredClient.createStation(station);
            }
        }

        private static void createStation(Map<String, Object> station) {
            StationRestAssuredClient.createStation(station);
        }
    }
}
