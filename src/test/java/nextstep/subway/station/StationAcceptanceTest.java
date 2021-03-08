package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.station.StationSteps.*;
import static nextstep.subway.station.StationSteps.Station.강남역;
import static nextstep.subway.station.StationSteps.Station.역삼역;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given & when
        ExtractableResponse<Response> response = 지하철역_생성요청(강남역.name);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        지하철역_생성요청(강남역.name);

        // when
        ExtractableResponse<Response> response = 지하철역_생성요청(강남역.name);

        // then
        지하철역_생성실패됨(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        ExtractableResponse<Response> createdResponse1 = 지하철역_생성요청(강남역.name);
        ExtractableResponse<Response> createdResponse2 = 지하철역_생성요청(역삼역.name);

        // when
        ExtractableResponse<Response> response = 지하철역_조회요청();

        // then
        지하철역_조회_응답됨(response);
        지하철역_조회_포함됨(response, Arrays.asList(createdResponse1, createdResponse2));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = 지하철역_생성요청(강남역.name);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철역_제거요청(uri);

        // then
        지하철역_제거됨(response);
    }
}
