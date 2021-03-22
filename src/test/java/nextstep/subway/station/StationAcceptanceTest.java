package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static nextstep.subway.station.StationAcceptanceAssertion.*;
import static nextstep.subway.station.StationAcceptanceRequest.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        Map<String, String> stationBody = createStationBody("신규역");

        // when
        ExtractableResponse<Response> response = 지하철_역_생성_요청(stationBody);

        // then
        지하철_역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성하면 실패한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        Map<String, String> stationBody = createStationBody("신규역");
        지하철_역_생성_요청(stationBody);

        // when
        ExtractableResponse<Response> response = 지하철_역_생성_요청(stationBody);

        // then
        지하철_역_생성_실패됨(response);
    }

    @DisplayName("지하철 역 목록을 조회한다.")
    @Test
    void getStations() {
        Map<String, String> stationBody1 = createStationBody("강남역");
        Map<String, String> stationBody2 = createStationBody("역삼역");

        // given
        ExtractableResponse<Response> createResponse1 = 지하철_역_생성_요청(stationBody1);
        ExtractableResponse<Response> createResponse2 = 지하철_역_생성_요청(stationBody2);

        // when
        ExtractableResponse<Response> response = 지하철_역_목록_조회_요청();

        // then
        지하철_역_목록_응답됨(response,
                Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        Map<String, String> stationBody = createStationBody("신규역");
        ExtractableResponse<Response> createResponse = 지하철_역_생성_요청(stationBody);

        // when
        ExtractableResponse<Response> response = 지하철_역_제거_요청(createResponse);

        // then
        지하철_역_제거됨(response);
    }
}
