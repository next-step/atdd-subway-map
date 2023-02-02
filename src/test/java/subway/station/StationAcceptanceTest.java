package subway.station;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import subway.util.DatabaseCleanup;

@ActiveProfiles("acceptance")
@DisplayName("지하철역 관련 기능")
@TestConstructor(autowireMode = AutowireMode.ALL)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

    @LocalServerPort
    private int port;

    private final DatabaseCleanup databaseCleanup;
    private final StationRestAssured stationRestAssured;

    public StationAcceptanceTest(final DatabaseCleanup databaseCleanup) {
        this.databaseCleanup = databaseCleanup;
        this.stationRestAssured = new StationRestAssured();
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
        this.databaseCleanup.truncateTable();
    }

    /**
     * When 지하철역을 생성하면 Then 지하철역이 생성된다 Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        String stationName = "강남역";

        stationRestAssured.createStation(stationName);

        // then
        List<String> stationNames = stationRestAssured.findStations()
                .jsonPath()
                .getList("name", String.class);

        assertThat(stationNames).containsAnyOf(stationName);
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void findStations() {
        // given
        String[] expectedStationNames = {"강남역", "양재역"};

        stationRestAssured.createStation(expectedStationNames);

        // when
        ExtractableResponse<Response> response = stationRestAssured.findStations();

        // Then
        List<String> stationNames = response
                .jsonPath()
                .getList("name", String.class);

        assertThat(stationNames).hasSize(expectedStationNames.length).contains(expectedStationNames);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        long deleteStationId = stationRestAssured.createStation("강남역").jsonPath().getLong("id");
        stationRestAssured.createStation("양재역").jsonPath().getLong("id");

        // when
        stationRestAssured.deleteStation(deleteStationId);

        // Then
        List<Long> stationIds = stationRestAssured.findStations()
                .jsonPath()
                .getList("id", Long.class);

        assertThat(stationIds).doesNotContain(deleteStationId);
    }
}
