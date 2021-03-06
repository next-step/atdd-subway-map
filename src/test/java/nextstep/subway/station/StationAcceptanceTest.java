package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.station.StationSteps.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("지하철역을 생성한다.")
    void createStation() {
        // given & when - 강남역 지하철 생성 요청
        ExtractableResponse<Response> response = 지하철_역_생성_요청("강남역");

        // then
        지하철_역_생성_됨(response);
    }

    @Test
    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    void createStationWithDuplicateName() {
        // given - 존재할 지하철 역 생성
        지하철_역_생성_요청("강남역");

        // when - 기존에 존재하는 지하철역 이름으로 지하철 역 생성 요청
        ExtractableResponse<Response> response = 지하철_역_생성_요청("강남역");

        // then - 지하철 역 생성 실패
        지하철_역_생성_실패(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given - 조회될 지하철 역 생성
        ExtractableResponse<Response> createResponse1 = 지하철_역_생성_요청("강남역");
        ExtractableResponse<Response> createResponse2 = 지하철_역_생성_요청("역삼역");

        // when - 지하철 역 조회
        ExtractableResponse<Response> response = 지하철_역_목록_조회_요청();

        // then
        지하철_역_목록_조회_응답_됨(response);
        지하철_역_목록_결과에_포함되는_역_확인(createResponse1, createResponse2, response);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given - 제거할 지하철 역
        ExtractableResponse<Response> createResponse = 지하철_역_생성_요청("강남역");

        // when
        String uri = 생성된_지하철_역_URI_경로_확인(createResponse);
        ExtractableResponse<Response> response = 지하철_역_삭제_요청(uri);

        // then
        지하철_역_삭제_됨(response);
    }
}
