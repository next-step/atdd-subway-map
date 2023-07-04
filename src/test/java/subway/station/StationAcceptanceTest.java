package subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
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
        ExtractableResponse<Response> createStationResponse = StationApi.createStationByName(stationName);

        // then
        assertThat(createStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> retrieveStationsResponse = StationApi.retrieveStations();
        assertThat(retrieveStationsResponse.jsonPath().getList("name", String.class)).containsAnyOf(stationName);
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
        createStationNames.forEach(StationApi::createStationByName);

        // when
        ExtractableResponse<Response> retrieveStationsResponse = StationApi.retrieveStations();
        assertThat(retrieveStationsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        assertThat(retrieveStationsResponse.body().jsonPath().getList("$").size()).isEqualTo(createdStations);

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
        ExtractableResponse<Response> createResponse = StationApi.createStationByName(stationName);
        final String createdLocation = createResponse.header("Location");
        final Integer createdId = createResponse.body().jsonPath().get("id");

        // when
        ExtractableResponse<Response> deletedStation = StationApi.deleteStationByLocation(createdLocation);
        assertThat(deletedStation.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> retrieveStationsResponse = StationApi.retrieveStations();
        assertThat(retrieveStationsResponse.body().jsonPath().getList("id")).doesNotContain(createdId);

    }

}