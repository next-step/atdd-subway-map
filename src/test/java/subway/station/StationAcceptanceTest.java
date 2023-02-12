package subway.station;

import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.RestTestUtils;

import java.util.List;
import java.util.Map;

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

        // given
        String station = "강남역";

        // when
        ExtractableResponse<Response> response = create(station);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = RestTestUtils.getListFromResponse(getAllStations(), "name", String.class);
        assertThat(stationNames).containsAnyOf(station);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("생성된 지하철역들을 조회한다.")
    @Test
    void getStationList() {

        // given
        List<String> stations = List.of("강남역","양재역");
        for (String station : stations) {
            create(station);
        }

        // when
        List<String> stationNames = RestTestUtils.getListFromResponse(getAllStations(), "name", String.class);

        // then
        assertThat(stationNames).containsAll(stations);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("생성된 지하철역을 제거한다.")
    @Test
    void deleteStation() {

        // given
        List<String> stations = List.of("강남역","양재역");
        for (String station : stations) {
            create(station);
        }

        // when
        List<Long> stationIds = RestTestUtils.getListFromResponse(getAllStations(), "id", Long.class);
        Long deleteId = stationIds.get(0);
        delete(deleteId);

        // then
        List<Long> result = RestTestUtils.getListFromResponse(getAllStations(), "id", Long.class);
        assertThat(result).doesNotContain(deleteId);
    }

    private ExtractableResponse<Response> create(String stationName) {
        return RestTestUtils.request(Method.POST, "/stations", Map.of("name", stationName));
    }

    private ExtractableResponse<Response> getAllStations() {
        return RestTestUtils.request(Method.GET, "/stations");
    }

    private ExtractableResponse<Response> delete(Long stationId) {
        return RestTestUtils.request(Method.DELETE, "/stations/"+stationId);
    }

}