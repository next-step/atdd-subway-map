package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.fixture.FieldFixture.노선_내_역_이름_목록;
import static subway.common.fixture.FieldFixture.노선_색깔;
import static subway.common.fixture.FieldFixture.노선_이름;
import static subway.common.fixture.FieldFixture.식별자_아이디;
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
        강남역_id = 지하철역_생성_요청(강남역.요청_데이터_생성())
                .jsonPath().getString(식별자_아이디.필드명());

        서울대입구역_id = 지하철역_생성_요청(서울대입구역.요청_데이터_생성())
                .jsonPath().getString(식별자_아이디.필드명());
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        지하철_노선_생성_요청(이호선.생성_요청_데이터_생성(강남역_id, 서울대입구역_id));

        // then
        assertThat(지하철_노선_목록_조회_요청().jsonPath().getList(노선_이름.필드명()))
                .contains(이호선.노선_이름());
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
        지하철_노선_생성_요청(이호선.생성_요청_데이터_생성(강남역_id, 서울대입구역_id));
        지하철_노선_생성_요청(사호선.생성_요청_데이터_생성(강남역_id, 서울대입구역_id));

        // when
        ExtractableResponse<Response> 지하철_노선_목록_조회_결과 = 지하철_노선_목록_조회_요청();

        // then
        assertThat(지하철_노선_목록_조회_결과.jsonPath().getList(노선_이름.필드명()))
                .hasSize(2)
                .contains(이호선.노선_이름(), 사호선.노선_이름());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void selectLine() {
        // given
        String 생성된_지하철_노선_id = 지하철_노선_생성_요청(이호선.생성_요청_데이터_생성(강남역_id, 서울대입구역_id))
                .jsonPath().getString(식별자_아이디.필드명());

        // when
        ExtractableResponse<Response> 지하철_노선_조회_결과 = 지하철_노선_단건_조회_요청(생성된_지하철_노선_id);

        // then
        assertThat(지하철_노선_조회_결과.jsonPath().getString(노선_이름.필드명()))
                .isEqualTo(이호선.노선_이름());
        assertThat(지하철_노선_조회_결과.jsonPath().getString(노선_색깔.필드명()))
                .isEqualTo(이호선.노선_색깔());
        assertThat(지하철_노선_조회_결과.jsonPath().getList(노선_내_역_이름_목록.필드명()))
                .hasSize(2)
                .contains(강남역.역_이름(), 서울대입구역.역_이름());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        String 생성된_지하철_노선_id = 지하철_노선_생성_요청(이호선.생성_요청_데이터_생성(강남역_id, 서울대입구역_id))
                .jsonPath().getString(식별자_아이디.필드명());

        // when
        지하철_노선_수정_요청(생성된_지하철_노선_id, 사호선.수정_요청_데이터_생성());

        // then
        ExtractableResponse<Response> 지하철_노선_단건_조회_결과 = 지하철_노선_단건_조회_요청(생성된_지하철_노선_id);

        assertThat(지하철_노선_단건_조회_결과.jsonPath().getString(노선_이름.필드명()))
                .isEqualTo(사호선.노선_이름());
        assertThat(지하철_노선_단건_조회_결과.jsonPath().getString(노선_색깔.필드명()))
                .isEqualTo(사호선.노선_색깔());
    }
}
