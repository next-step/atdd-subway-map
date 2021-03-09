package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.station.StationRequestStep.*;
import static nextstep.subway.station.StationVerificationStep.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        final ExtractableResponse<Response> response = 지하철역_생성_요청("강남역");

        지하철역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        지하철역_등록되어_있음("강남역");

        final ExtractableResponse<Response> response = 지하철역_생성_요청("강남역");

        지하철역_생성_실패됨(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        final Long 강남역 = 지하철역_등록되어_있음("강남역");
        final Long 역삼역 = 지하철역_등록되어_있음("역삼역");

        final ExtractableResponse<Response> response = 지하철역_조회_요청();

        지하철역_조회됨(response, 강남역, 역삼역);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        final Long 강남역 = 지하철역_등록되어_있음("강남역");

        final ExtractableResponse<Response> response = 지하철역_제거_요청(강남역);

        지하철역_제거됨(response);
    }
}
