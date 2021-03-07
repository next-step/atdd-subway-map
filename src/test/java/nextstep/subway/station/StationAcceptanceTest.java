package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.station.StationSteps.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철_역_생성_요청("강남역");

        // then
        지하철_역_생성_성공(response);
        생성된_지하철_역_uri_반환(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        지하철_역_생성_요청("강남역");

        // when

        ExtractableResponse<Response> response = 지하철_역_생성_요청("강남역");

        // then
        지하철_역_생성_실패(response);
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        /// given
        지하철_역_생성_요청("강남역");
        지하철_역_생성_요청("역삼역");

        // when
        ExtractableResponse<Response> response = 지하철_역_목록_조회_요청();

        // then
        지하철_역_조회_성공(response);
        지하철_역_목록_조회_결과_2건(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStation() {
        /// given
        ExtractableResponse<Response> createdLine = 지하철_역_생성_요청("강남역");
        Long createdLineId = 생성된_지하철_역_ID_확인(createdLine);

        // when
        ExtractableResponse<Response> response = 지하철_역_조회_요청(createdLineId);

        // then
        지하철_역_조회_성공(response);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createdLine = 지하철_역_생성_요청("강남역");
        Long createdLineId = 생성된_지하철_역_ID_확인(createdLine);

        // when
        ExtractableResponse<Response> response = 지하철_역_삭제_요청(createdLineId);

        // then
        지하철_역_삭제_성공(response);
    }
}
