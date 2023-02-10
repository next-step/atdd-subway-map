package subway.Acceptance.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.Acceptance.AcceptanceTest;

import java.util.List;

import static subway.Acceptance.station.StationAcceptanceStep.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청("사당역");

        // then
        지하철역_생성됨(createResponse);

        // then
        ExtractableResponse<Response> response = 지하철역_목록_조회();
        지하철역_목록_포함됨(response, List.of(createResponse));
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void showStations() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철역_생성_요청("강남역");
        ExtractableResponse<Response> createResponse2 = 지하철역_생성_요청("역삼역");

        // when
        ExtractableResponse<Response> response = 지하철역_목록_조회();

        // then
        지하철역_목록_포함됨(response, List.of(createResponse1, createResponse2));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청("강남역");

        // when
        ExtractableResponse<Response> response = 지하철역_삭제_요청(createResponse);

        // then
        지하철역_삭제됨(response);

        // then
        지하철역_목록_제외됨(createResponse);
    }
}
