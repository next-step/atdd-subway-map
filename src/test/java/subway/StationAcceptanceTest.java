package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.MockStation.강남역;
import static subway.MockStation.서초역;
import static subway.MockStation.신촌역;

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
        ExtractableResponse<Response> response = StationApi.createStation(강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = StationApi.getStationNames();
        assertThat(stationNames).containsAnyOf(강남역.name());
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
        StationApi.createStations(강남역, 서초역);

        // When
        List<String> stationNames = StationApi.getStationNames();

        // Then
        assertThat(stationNames)
                .hasSize(2)
                .containsAnyOf(강남역.name(), 서초역.name());
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
        StationApi.createStations(강남역, 서초역, 신촌역);

        // When
        Long stationId = StationApi.getStationId(서초역);
        StationApi.deleteStation(stationId);

        // Then
        List<String> stationNames = StationApi.getStationNames();
        assertThat(stationNames)
                .hasSize(2)
                .containsAnyOf(강남역.name(), 신촌역.name());
    }
}