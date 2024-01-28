package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.controller.dto.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    private static final String STATION_NAME_GANGNAM = "강남역";
    private static final String STATION_NAME_SEOLLEUNG = "선릉역";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        Map<String, String> gangNamStationParams = createParams(STATION_NAME_GANGNAM);

        // when
        ExtractableResponse<Response> createResponse = createStation(gangNamStationParams);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> findResponse = findStation();
        List<String> stationsNames = findResponse.jsonPath().getList("name", String.class);
        assertThat(stationsNames).hasSize(1)
                .containsExactly(STATION_NAME_GANGNAM);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void selectStation() {
        // given
        Map<String, String> gangNamStationParams = createParams(STATION_NAME_GANGNAM);
        ExtractableResponse<Response> gangnamStationCreateResponse = createStation(gangNamStationParams);
        assertThat(gangnamStationCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // given
        Map<String, String> seollEungStationParams = createParams(STATION_NAME_SEOLLEUNG);
        ExtractableResponse<Response> seollEungStationCreateResponse = createStation(seollEungStationParams);
        assertThat(seollEungStationCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> findResponse = findStation();
        List<String> stationsNames = findResponse.jsonPath().getList("name", String.class);

        // then
        assertThat(stationsNames).hasSize(2)
                .containsExactly("강남역", STATION_NAME_SEOLLEUNG);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void removeStation() {
        // given
        Map<String, String> gangNamStationParams = createParams(STATION_NAME_GANGNAM);
        ExtractableResponse<Response> createResponse = createStation(gangNamStationParams);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        StationResponse stationResponse = createResponse.as(StationResponse.class);

        // when
        deleteStation(stationResponse.getId());

        // then
        ExtractableResponse<Response> findResponse = findStation();
        List<String> stationsNames = findResponse.jsonPath().getList("name", String.class);
        assertThat(stationsNames).isEmpty();
    }

    private Map<String, String> createParams(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);
        return params;
    }

    private ExtractableResponse<Response> createStation(Map<String, String> body) {
        return post("/stations", body);
    }

    private ExtractableResponse<Response> findStation() {
        return get("/stations/all");
    }

    private ExtractableResponse<Response> deleteStation(Long id) {
        return delete("/stations/{id}", id);
    }

}
