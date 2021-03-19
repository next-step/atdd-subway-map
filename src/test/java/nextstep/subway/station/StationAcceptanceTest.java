package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.station.StationTestStep.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철_역_생성_요청("강남역");

        // then
        지하철_역_생성_확인(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        지하철_역_등록되어_있음("강남역");

        // when
        ExtractableResponse<Response> response = 지하철_역_생성_요청("강남역");

        // then
        지하철_역_생성_실패_확인(response);
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        /// given
        List<StationResponse> stationResponseList = 지하철_역_목록_등록되어_있음();

        // when
        ExtractableResponse<Response> response = 지하철_역_목록_조회_요청();

        // then
        지하철_역_목록_조회_확인(stationResponseList, response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStation() {
        // given
        StationResponse addedStationResponse = 지하철_역_등록되어_있음("강남역");

        // when
        ExtractableResponse<Response> response = 지하철_역_조회_요청(addedStationResponse.getId());

        // then
        지하철_역_조회_확인(addedStationResponse, response);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationResponse addedStation = 지하철_역_등록되어_있음("강남역");

        // when
        ExtractableResponse<Response> response = 지하철_역_삭제_요청(addedStation.getId());

        // then
        지하철_역_제거_확인(response);
    }
}
