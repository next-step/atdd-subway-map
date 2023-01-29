package subway.station;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        StationResponse station = requestSaveStation("강남역");

        // then
        List<String> findStationNames = getStationNames();
        assertThat(findStationNames).containsAnyOf(station.getName());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        // given
        List<StationResponse> stations = requestSaveStation("강남역", "망포역");
        // when
        List<String> findStationNames = getStationNames();
        // then
        assertThat(findStationNames)
                .hasSize(stations.size())
                .containsAnyOf(stations.stream()
                        .map(StationResponse::getName)
                        .toArray(String[]::new));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationResponse station = requestSaveStation("강남역");
        // when
        requestDeleteStation(station.getId());
        // then
        assertDoseNotContainsStation("강남역");
    }

    private void requestDeleteStation(Long id) {
        RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    public static List<StationResponse> requestSaveStation(String... stationNames) {
        return Arrays.stream(stationNames)
                .map(StationAcceptanceTest::requestSaveStation)
                .collect(Collectors.toList());
    }

    public static StationResponse requestSaveStation(String stationName) {
        // when
        return RestAssured.given().log().all()
                .body(new StationRequest(stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .body()
                .as(StationResponse.class);
    }

    private void assertDoseNotContainsStation(String... stationNames) {
        List<String> findStationNames = getStationNames();
        assertThat(findStationNames).doesNotContain(stationNames);
    }

    private List<String> getStationNames() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }
}
