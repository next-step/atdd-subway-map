package subway;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static subway.MockStation.강남역;
import static subway.MockStation.서초역;
import static subway.MockStation.신촌역;
import static subway.StationApi.STATION_ID_KEY;
import static subway.StationApi.STATION_NAME_KEY;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {
    
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStationTest() {
        // when
        ExtractableResponse<Response> responseOfCreateStation = StationApi.createStation(강남역);

        // then
        checkStationCreated(responseOfCreateStation);

        // then
        ExtractableResponse<Response> responseOfShowStations = StationApi.showStations();
        checkStationExistence(responseOfShowStations, 강남역);
    }

    private void checkStationCreated(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void checkStationExistence(ExtractableResponse<Response> response, MockStation... stations) {
        JsonPath jsonPath = response.jsonPath();
        for (MockStation station : stations) {
            assertTrue(jsonPath.getList(STATION_NAME_KEY).contains(station.name()));
        }
    }
    private void checkStationNotExistence(ExtractableResponse<Response> response, MockStation... stations) {
        JsonPath jsonPath = response.jsonPath();
        for (MockStation station : stations) {
            assertFalse(jsonPath.getList(STATION_NAME_KEY).contains(station.name()));
        }
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다")
    @Test
    void showStationsTest() {
        // Given
        StationApi.createStation(강남역);
        StationApi.createStation(서초역);
        
        // When
        ExtractableResponse<Response> responseOfShowStations = StationApi.showStations();

        // Then
        checkStationCount(responseOfShowStations, 2);
        checkStationExistence(responseOfShowStations, 강남역, 서초역);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다")
    @Test
    void deleteStation() {
        // Given
        StationApi.createStation(강남역);
        ExtractableResponse<Response> responseOfCreateStation = StationApi.createStation(서초역);
        StationApi.createStation(신촌역);

        // When
        Long stationId = extractStationId(responseOfCreateStation);
        StationApi.deleteStation(stationId);

        // Then
        ExtractableResponse<Response> responseOfShowStations = StationApi.showStations();
        checkStationCount(responseOfShowStations, 2);
        checkStationNotExistence(responseOfShowStations, 서초역);
        checkStationExistence(responseOfShowStations, 강남역, 신촌역);
    }
    
    private Long extractStationId(ExtractableResponse<Response> response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getLong(STATION_ID_KEY);
    }
    
    private void checkStationCount(ExtractableResponse<Response> response, int expected) {
        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getList("")).hasSize(expected);
    }
}