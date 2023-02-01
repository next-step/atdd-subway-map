package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.AcceptanceTest;

import static subway.fixture.TestFixtureLine.*;
import static subway.fixture.TestFixtureStation.지하철역_생성_요청;

@DisplayName("지하철 노선 기능 인수 테스트")
class LineAcceptanceTest extends AcceptanceTest {

    private Long 강남역;
    private Long 잠실역;
    private Long 검암역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역");
        잠실역 = 지하철역_생성_요청("잠실역");
        검암역 = 지하철역_생성_요청("검암역");
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        final ExtractableResponse<Response> 이호선 = 지하철_노선_생성_요청("2호선", "bg-red-600", 강남역 ,잠실역, 10);

        지하철_노선_생성됨(이호선);

        final ExtractableResponse<Response> 지하철_노선_목록_조회_응답 = 지하철_노선_목록_조회_요청();

        지하철_노선_목록_중_생성한_노선_조회됨(지하철_노선_목록_조회_응답, 이호선);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        지하철_노선_생성_요청("2호선", "bg-red-600", 강남역 ,잠실역, 10);
        지하철_노선_생성_요청("신분당선", "bg-green-600", 강남역 ,검암역, 10);

        final ExtractableResponse<Response> 지하철_노선_목록_응답 = 지하철_노선_목록_조회_요청();

        지하철_노선_목록_조회됨(지하철_노선_목록_응답, 2);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findLine() {
        final ExtractableResponse<Response> 이호선 = 지하철_노선_생성_요청("2호선", "bg-red-600", 강남역 ,잠실역, 10);

        지하철_노선_생성됨(이호선);

        final ExtractableResponse<Response> 지하철_노선_조회_응답 = 지하철_노선_조회_요청(이호선);

        지하철_노선_조회됨(지하철_노선_조회_응답, "2호선", "bg-red-600", 2);
    }

    @DisplayName("지하철 노선 정보를 수정한다.")
    @Test
    void updateLine() {
        final ExtractableResponse<Response> 이호선_응답 = 지하철_노선_생성_요청("2호선", "bg-red-600", 강남역 ,잠실역, 10);

        지하철_노선_생성됨(이호선_응답);

        final ExtractableResponse<Response> 이호선_수정_응답 = 지하철_노선_수정_요청(이호선_응답, "3호선", "bg-yellow-600");

        지하철_노선_수정됨(이호선_수정_응답);

        final ExtractableResponse<Response> 지하철_노선_조회_응답 = 지하철_노선_조회_요청(이호선_응답);

        지하철_노선_조회됨(지하철_노선_조회_응답, "3호선", "bg-yellow-600", 2);
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        final ExtractableResponse<Response> 이호선_응답 = 지하철_노선_생성_요청("2호선", "bg-red-600", 강남역, 잠실역, 10);

        지하철_노선_생성됨(이호선_응답);

        final ExtractableResponse<Response> 이호선_삭제_응답 = 지하철_노선_삭제_요청(이호선_응답);

        지하철_노선_삭제됨(이호선_삭제_응답);
    }
}
