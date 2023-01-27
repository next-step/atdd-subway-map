package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import subway.AcceptanceTest;

import static subway.acceptance.TestFixtureStation.*;

@DisplayName("지하철 역 기능 인수 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StationAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * When 지하철역 조회하면
     * Then 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        final ExtractableResponse<Response> 강남역 = 지하철역_생성_요청함("강남역");

        지하철역_생성됨(강남역);

        final ExtractableResponse<Response> 지하철역_목록 = 지하철역_목록_요청();

        지하철역_조회됨(지하철역_목록, "강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void showStations() {
        지하철역_생성_요청("잠실역");
        지하철역_생성_요청("검암역");

        final ExtractableResponse<Response> 지하철역_목록_응답 = 지하철역_목록_요청();

        지하철역_목록_조회됨(지하철역_목록_응답, "잠실역", "검암역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하고
     * When 지하철역 목록 조회를 하면
     * Then 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        final ExtractableResponse<Response> 잠실역 = 지하철역_생성_요청함("잠실역");

        지하철역_삭제_요청(잠실역);

        final ExtractableResponse<Response> 지하철역_목록 = 지하철역_목록_요청();

        지하철역_조회되지_않음(지하철역_목록);
    }
}