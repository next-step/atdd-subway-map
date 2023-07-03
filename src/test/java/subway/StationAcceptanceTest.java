package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

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
        final String stationName = "강남역";
        ExtractableResponse<Response> response = StationApi.callCreateStationApi(stationName);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = StationApi.callRetrieveStationsApi().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(stationName);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void retrieveStations() {
        // given
        final List<String> createStationNames = List.of("강남역", "역삼역");
        final int createdStations = createStationNames.size();
        createStationNames.forEach(StationApi::callCreateStationApi);

        // when
        ExtractableResponse<Response> retrieveStationsResponse = StationApi.callRetrieveStationsApi();
        assertThat(retrieveStationsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        assertThat(createdStations).isEqualTo(retrieveStationsResponse.body().jsonPath().getList("$").size());

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
        final String stationName = "강남역";
        ExtractableResponse<Response> createResponse = StationApi.callCreateStationApi(stationName);
        final String createdLocation = createResponse.header("Location");
        final Integer createdId = createResponse.body().jsonPath().get("id");

        // when
        ExtractableResponse<Response> deletedStation = StationApi.callDeleteStationByLocation(createdLocation);
        assertThat(deletedStation.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> retrieveStationsResponse = StationApi.callRetrieveStationsApi();
        assertThat(retrieveStationsResponse.body().jsonPath().getList("id")).doesNotContain(createdId);

    }

}