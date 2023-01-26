package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static subway.LineAcceptanceTestHelper.*;
import static subway.StationAcceptanceTestHelper.지하철역_생성함;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private Long 강남역;
    private Long 양재역;
    private Long 역삼역;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        강남역 = 지하철역_생성함("강남역");
        양재역 = 지하철역_생성함("양재역");
        역삼역 = 지하철역_생성함("역삼역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        final ExtractableResponse<Response> 노선_생성_응답 = 노선_생성_요청("신분당선", "bg-red-600", 강남역, 양재역, 10);

        // then
        노선이_정상적으로_생성되었는지_확인(노선_생성_응답, "신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        노선_생성함("신분당선", "bg-red-600", 강남역, 양재역, 10);
        노선_생성함("2호선", "bg-green-600", 강남역, 역삼역, 5);

        // when
        final ExtractableResponse<Response> 노선_목록_조회_응답 = 노선_목록_조회_요청();

        // then
        노선들이_목록_안에_있는지_확인(노선_목록_조회_응답, "신분당선", "2호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        final Long 노선_ID = 노선_생성함("신분당선", "bg-red-600", 강남역, 양재역, 10);

        // when
        final ExtractableResponse<Response> 노선_상세_조회_응답 = 노선_상세_조회_요청(노선_ID);

        // then
        조회한_노선의_정보와_일치하는지_확인(노선_상세_조회_응답, "신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given

        // when

        // then
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given

        // when

        // then
    }
}
