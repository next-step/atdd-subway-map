package subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.common.util.validation.ExistenceValidation;
import subway.common.util.validation.ResponseStatusValidation;
import subway.setting.AcceptanceTest;
import subway.station.util.StationExtraction;

import static subway.station.MockStation.강남역;
import static subway.station.MockStation.서초역;
import static subway.station.MockStation.신촌역;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

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
        ResponseStatusValidation.checkCreatedResponse(responseOfCreateStation);

        // then
        ExtractableResponse<Response> responseOfShowStations = StationApi.showStations();
        ExistenceValidation.checkNameExistenceInList(responseOfShowStations, 강남역);
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
        ExistenceValidation.checkCountInList(responseOfShowStations, 2);
        ExistenceValidation.checkNamesExistenceInList(responseOfShowStations, 강남역, 서초역);
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
        ExtractableResponse<Response> responseOfCreate서초역 = StationApi.createStation(서초역);
        StationApi.createStation(신촌역);

        // When
        Long stationId = StationExtraction.getStationId(responseOfCreate서초역);
        StationApi.deleteStation(stationId);

        // Then
        ExtractableResponse<Response> responseOfShowStations = StationApi.showStations();
        ExistenceValidation.checkCountInList(responseOfShowStations, 2);
        ExistenceValidation.checkNamesNotExistenceInList(responseOfShowStations, 서초역);
        ExistenceValidation.checkNamesExistenceInList(responseOfShowStations, 강남역, 신촌역);
    }
}