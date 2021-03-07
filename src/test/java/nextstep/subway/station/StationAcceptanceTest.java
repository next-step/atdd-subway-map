package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.station.StationSteps.assertCreateStation;
import static nextstep.subway.station.StationSteps.assertCreateStationFail;
import static nextstep.subway.station.StationSteps.assertDeleteStation;
import static nextstep.subway.station.StationSteps.assertGetStations;
import static nextstep.subway.station.StationSteps.assertIncludeStations;
import static nextstep.subway.station.StationSteps.requestCreateStationGangnam;
import static nextstep.subway.station.StationSteps.requestCreateStationYeoksam;
import static nextstep.subway.station.StationSteps.requestDeleteStation;
import static nextstep.subway.station.StationSteps.requestGetStations;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("지하철역을 생성한다.")
    void createStation() {
        // when
        ExtractableResponse<Response> response = requestCreateStationGangnam();

        // then
        assertCreateStation(response);
    }

    @Test
    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    void createStationWithDuplicateName() {
        // given
        requestCreateStationGangnam();

        // when
        ExtractableResponse<Response> response = requestCreateStationGangnam();

        // then
        assertCreateStationFail(response);
    }

    @Test
    @DisplayName("지하철역 목록을 조회한다.")
    void getStations() {
        /// given
        ExtractableResponse<Response> createResponse1 = requestCreateStationGangnam();
        ExtractableResponse<Response> createResponse2 = requestCreateStationYeoksam();

        // when
        ExtractableResponse<Response> response = requestGetStations();

        // then
        assertGetStations(response);
        assertIncludeStations(response, createResponse1, createResponse2);
    }

    @Test
    @DisplayName("지하철역을 제거한다.")
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = requestCreateStationGangnam();

        // when
        ExtractableResponse<Response> response = requestDeleteStation(createResponse);

        // then
        assertDeleteStation(response);
    }
}
