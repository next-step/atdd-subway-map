package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.AcceptanceTest;

import static subway.fixture.TestFixtureStation.*;

@DisplayName("지하철 역 기능 인수 테스트")
class StationAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        final ExtractableResponse<Response> 강남역 = 지하철역_생성_요청함("강남역");

        지하철역_생성됨(강남역);

        final ExtractableResponse<Response> 지하철역_목록 = 지하철역_목록_요청();

        지하철역_조회됨(지하철역_목록, "강남역");
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void showStations() {
        지하철역_생성_요청("잠실역");
        지하철역_생성_요청("검암역");

        final ExtractableResponse<Response> 지하철역_목록_응답 = 지하철역_목록_요청();

        지하철역_목록_조회됨(지하철역_목록_응답, "잠실역", "검암역");
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        final ExtractableResponse<Response> 잠실역 = 지하철역_생성_요청함("잠실역");

        지하철역_삭제_요청(잠실역);

        final ExtractableResponse<Response> 지하철역_목록 = 지하철역_목록_요청();

        지하철역_조회되지_않음(지하철역_목록);
    }
}