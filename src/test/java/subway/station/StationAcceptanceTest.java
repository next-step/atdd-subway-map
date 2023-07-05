package subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import subway.constants.Endpoint;
import subway.support.AcceptanceTest;
import subway.support.DatabaseCleanUp;
import subway.support.RestAssuredClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.UNDEFINED_PORT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철역 관련 기능")
@AcceptanceTest
public class StationAcceptanceTest {

    @LocalServerPort
    private int port;

    private static final String STATION_BASE_URL = Endpoint.STATION_BASE_URL.getUrl();

    private static final String STATION_ID_KEY = "id";

    private static final String STATION_NAME_KEY = "name";

    @Autowired private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
        }
        databaseCleanUp.execute();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        String stationName = "강남역";
        saveStation(stationName);

        // then
        List<String> stationNames = findStationsAll()
                .jsonPath()
                .getList(STATION_NAME_KEY, String.class);

        assertAll(
                () -> assertThat(stationNames.size()).isEqualTo(1),
                () -> assertThat(stationNames).containsAnyOf(stationName)
        );
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void readStations() {
        //give
        String gangnamStationName = "강남역";
        String gwanggyoStationName = "광교역";

        Stream.of(gangnamStationName, gwanggyoStationName)
                .forEach(this::saveStation);

        // when
        ExtractableResponse<Response> findStationsAllResponse = findStationsAll();
        List<String> stationNames = findStationsAllResponse
                .jsonPath()
                .getList(STATION_NAME_KEY, String.class);

        // then
        assertThat(stationNames).containsOnly(gangnamStationName, gwanggyoStationName);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        String stationName = "신도림역";
        Long savedStationId = saveStation(stationName)
                .jsonPath()
                .getLong(STATION_ID_KEY);

        // when
        ExtractableResponse<Response> deleteStationByIdResponse = deleteStationById(savedStationId);

        // then
        List<String> stationNames = RestAssuredClient.get(Endpoint.STATION_BASE_URL.getUrl())
                .jsonPath()
                .getList(STATION_NAME_KEY, String.class);

        assertThat(stationNames).doesNotContain(stationName);
    }

    /**
     * <pre>
     * stationName 이라는 이름을 가진
     * 지하철역을 생성하는 API를 호출하는 함수
     * </pre>
     *
     * @param stationName
     * @return ExtractableResponse
     */
    private ExtractableResponse<Response> saveStation(String stationName) {
        Map<String, String> station = Map.of(STATION_NAME_KEY, stationName);
        ExtractableResponse<Response> saveStationResponse =
                RestAssuredClient.post(STATION_BASE_URL, station);
        assertThat(saveStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return saveStationResponse;
    }

    /**
     * <pre>
     * 모든 지하철역들을 조회하는 API를 호출하는 함수
     * </pre>
     *
     * @return ExtractableResponse
     */
    private ExtractableResponse<Response> findStationsAll() {
        ExtractableResponse<Response> findStationsAllResponse = RestAssuredClient.get(STATION_BASE_URL);
        assertThat(findStationsAllResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        return findStationsAllResponse;
    }

        /**
         * <pre>
         * id를 가진
         * 지하철역을 삭제하는 API를 호출하는 함수
         * </pre>
         *
         * @param id
         * @return ExtractableResponse
         */
    private ExtractableResponse<Response> deleteStationById(Long id) {
        String path = String.format("%s/%d", STATION_BASE_URL, id);
        ExtractableResponse<Response> deleteStationByIdResponse = RestAssuredClient.delete(path);
        assertThat(deleteStationByIdResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        return deleteStationByIdResponse;
    }

}