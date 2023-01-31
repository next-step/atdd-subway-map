package subway.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.fixture.FieldFixture.노선_이름;
import static subway.common.fixture.LineFixture.사호선;
import static subway.common.fixture.LineFixture.이호선;
import static subway.common.fixture.StationFixture.강남역;
import static subway.common.fixture.StationFixture.서울대입구역;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {
    private String 강남역_id;
    private String 서울대입구역_id;

    @BeforeEach
    void setUp() {
        Map<String, String> 강남역_데이터 = 강남역.요청_데이터_생성();
        강남역_id = 지하철역_생성_요청(강남역_데이터)
                .jsonPath().getString("id");

        Map<String, String> 서울대입구역_데이터 = 서울대입구역.요청_데이터_생성();
        서울대입구역_id = 지하철역_생성_요청(서울대입구역_데이터)
                .jsonPath().getString("id");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        Map<String, String> 이호선_데이터 = 이호선.요청_데이터_생성(강남역_id, 서울대입구역_id);
        지하철_노선_생성_요청(이호선_데이터);

        // then
        List<Object> nameList = 지하철_노선_목록_조회_요청()
                .jsonPath().getList(노선_이름.필드명());

        assertThat(nameList).contains("2호선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void selectLines() {
        // given
        Map<String, String> 이호선_데이터 = 이호선.요청_데이터_생성(강남역_id, 서울대입구역_id);
        지하철_노선_생성_요청(이호선_데이터);

        Map<String, String> 사호선_데이터 = 사호선.요청_데이터_생성(강남역_id, 서울대입구역_id);
        지하철_노선_생성_요청(사호선_데이터);

        // when
        List<String> 지하철_노선_이름_목록 = 지하철_노선_목록_조회_요청()
                .jsonPath().getList(노선_이름.필드명(), String.class);

        // then
        assertThat(지하철_노선_이름_목록).hasSize(2)
                .contains(이호선.노선_이름(), 사호선.노선_이름());
    }
}
