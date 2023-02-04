package subway.station;

import static subway.station.StationAssert.역_목록_조회_검증;
import static subway.station.StationAssert.역_삭제_검증;
import static subway.station.StationAssert.역_생성_검증;
import static subway.station.StationRestAssured.역_목록_조회;
import static subway.station.StationRestAssured.역_삭제;
import static subway.station.StationRestAssured.역_생성;

import io.restassured.RestAssured;
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

    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "양재역";

    @LocalServerPort
    private int port;

    private final DatabaseCleanup databaseCleanup;

    public StationAcceptanceTest(final DatabaseCleanup databaseCleanup) {
        this.databaseCleanup = databaseCleanup;
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
        역_생성(STATION_NAME1);

        // then
        var response = 역_목록_조회();

        List<String> stationNames = response
                .jsonPath()
                .getList("name", String.class);

        역_생성_검증(stationNames, STATION_NAME1);
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void findStations() {
        // given
        String[] expectedStationNames = {STATION_NAME1, STATION_NAME2};

        역_생성(expectedStationNames);

        // when
        var response = 역_목록_조회();

        // Then
        List<String> stationNames = response
                .jsonPath()
                .getList("name", String.class);

        역_목록_조회_검증(expectedStationNames, stationNames);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        long deleteStationId = 역_생성(STATION_NAME1).jsonPath().getLong("id");
        역_생성(STATION_NAME2).jsonPath().getLong("id");

        // when
        역_삭제(deleteStationId);

        // Then
        var response = 역_목록_조회();

        List<Long> stationIds = response
                .jsonPath()
                .getList("id", Long.class);

        역_삭제_검증(deleteStationId, stationIds);
    }
}
