package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.ApiUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     * @see nextstep.subway.ui.LineController#createLine
     */
    @DisplayName("지하철 노선 생성 테스트")
    @Test
    void 지하철_노선_생성_테스트() {
        //given
        ApiUtil.지하철역_생성_API(연신내역);
        ApiUtil.지하철역_생성_API(서울역);

        // when
        ExtractableResponse<Response> response = ApiUtil.지하철_노선_생성_API(GTXA노선);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     * @see nextstep.subway.ui.LineController#createLine
     */
    @DisplayName("[예외]지하철 노선 이름 중복 생성 방지 테스트")
    @Test
    void 지하철_노선_이름_중복_생성_방지_테스트() {
        //given
        ApiUtil.지하철역_생성_API(연신내역);
        ApiUtil.지하철역_생성_API(서울역);
        ApiUtil.지하철_노선_생성_API(GTXA노선);

        // when
        ExtractableResponse<Response> response = ApiUtil.지하철_노선_생성_API(GTXA노선);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    /**
     * When 지하철 노선 생성을 요청 했을 때 상행 역 정보가 없으면,
     * Then 지하철 노선 생성이 실패한다.
     * @see nextstep.subway.ui.LineController#createLine
     */
    @DisplayName("[예외]지하철 노선 상행 등록정보 없음 방지 테스트")
    @Test
    void 지하철_노선_상행_등록정보_없음_방지_테스트() {
        //given
        ApiUtil.지하철역_생성_API(연신내역);
        ApiUtil.지하철역_생성_API(서울역);

        // when
        ExtractableResponse<Response> response = ApiUtil.지하철_노선_생성_API(GTXA노선_상행_정보없음);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    /**
     * When 지하철 노선 생성을 요청 했을 때 하행 역 정보가 없으면,
     * Then 지하철 노선 생성이 실패한다.
     * @see nextstep.subway.ui.LineController#createLine
     */
    @DisplayName("[예외]지하철 노선 하행 등록정보 없음 방지 테스트")
    @Test
    void 지하철_노선_하행_등록정보_없음_방지_테스트() {
        //given
        ApiUtil.지하철역_생성_API(연신내역);
        ApiUtil.지하철역_생성_API(서울역);

        // when
        ExtractableResponse<Response> response = ApiUtil.지하철_노선_생성_API(GTXA노선_하행_정보없음);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    /**
     * When 지하철 노선 생성을 요청 했을 때 노선 거리 정보가 없으면,
     * Then 지하철 노선 생성이 실패한다.
     * @see nextstep.subway.ui.LineController#createLine
     */
    @DisplayName("[예외]지하철 노선 거리 정보 없음 방지 테스트")
    @Test
    void 지하철_노선_거리_정보_없음_방지_테스트() {
        //given
        ApiUtil.지하철역_생성_API(연신내역);
        ApiUtil.지하철역_생성_API(서울역);

        // when
        ExtractableResponse<Response> response = ApiUtil.지하철_노선_생성_API(GTXA노선_거리_정보없음);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    /**
     * When 지하철 노선 생성을 요청 했을 때 상행역과 하행역이 같으면,
     * Then 지하철 노선 생성이 실패한다.
     * @see nextstep.subway.ui.LineController#createLine
     */
    @DisplayName("[예외]지하철 노선 상행 하행 중복 방지 테스트")
    @Test
    void 지하철_노선_상행_하행_중복_방지_테스트() {
        //given
        ApiUtil.지하철역_생성_API(연신내역);
        ApiUtil.지하철역_생성_API(서울역);

        // when
        ExtractableResponse<Response> response = ApiUtil.지하철_노선_생성_API(GTXA노선_상행_하행_정보같음);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     * @see nextstep.subway.ui.LineController#findAllLines()
     */
    @DisplayName("지하철 노선 목록 조회 테스트")
    @Test
    void 지하철_노선_목록_조회_테스트() {
        // given
        ApiUtil.지하철역_생성_API(연신내역);
        ApiUtil.지하철역_생성_API(서울역);
        ApiUtil.지하철_노선_생성_API(GTXA노선);

        ApiUtil.지하철역_생성_API(강남역);
        ApiUtil.지하철역_생성_API(양재역);
        ApiUtil.지하철_노선_생성_API(신분당선);

        // when
        ExtractableResponse<Response> response = ApiUtil.지하철_노선_전체_리스트_조회_API();

        // then
        List<String> lineNames = response.body().jsonPath().getList("name");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineNames).contains("GTX-A", "신분당선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     * @see nextstep.subway.ui.LineController#getLine
     */
    @DisplayName("지하철 노선 조회 테스트")
    @Test
    void 지하철_노선_조회_테스트() {
        // given
        ApiUtil.지하철역_생성_API(연신내역);
        ApiUtil.지하철역_생성_API(서울역);
        ApiUtil.지하철_노선_생성_API(GTXA노선);

        // when
        ExtractableResponse<Response> response = ApiUtil.지하철_노선_단건_조회_API(1L);

        // then
        String lineName = response.body().jsonPath().get("name").toString();
        String lineColor = response.body().jsonPath().get("color").toString();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineName).isEqualTo("GTX-A");
        assertThat(lineColor).isEqualTo("bg-red-900");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     * @see nextstep.subway.ui.LineController#updateLine
     */
    @DisplayName("지하철 노선 수정 테스트")
    @Test
    void 지하철_노선_수정_테스트() {
        // given
        ApiUtil.지하철역_생성_API(연신내역);
        ApiUtil.지하철역_생성_API(서울역);
        ApiUtil.지하철_노선_생성_API(GTXA노선);

        // when
        ExtractableResponse<Response> response = ApiUtil.지하철_노선_수정_API(1L, 노선색상);

        // then
        ExtractableResponse<Response> updatedLine = ApiUtil.지하철_노선_단건_조회_API(1L);
        String lineName = updatedLine.body().jsonPath().get("name").toString();
        String lineColor = updatedLine.body().jsonPath().get("color").toString();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineName).isEqualTo("GTX-A");
        assertThat(lineColor).isEqualTo("bg-red-800");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     * @see nextstep.subway.ui.LineController#deleteLine
     */
    @DisplayName("지하철 노선 삭제 테스트")
    @Test
    void 지하철_노선_삭제_테스트() {
        //given
        ApiUtil.지하철역_생성_API(연신내역);
        ApiUtil.지하철역_생성_API(서울역);
        ApiUtil.지하철_노선_생성_API(GTXA노선);

        // when
        ExtractableResponse<Response> deleteResponse = ApiUtil.지하철_노선_삭제_API(1L);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    static Map<String, String> GTXA노선;
    static Map<String, String> GTXA노선_상행_정보없음;
    static Map<String, String> GTXA노선_하행_정보없음;
    static Map<String, String> GTXA노선_거리_정보없음;
    static Map<String, String> GTXA노선_상행_하행_정보같음;
    static Map<String, String> 신분당선;
    static Map<String, String> 노선색상;

    static Map<String, String> 연신내역;
    static Map<String, String> 서울역;
    static Map<String, String> 강남역;
    static Map<String, String> 양재역;

    @BeforeAll
    public static void 초기화() {
        연신내역 = new HashMap<>();
        연신내역.put("name", "연신내");
        서울역 = new HashMap<>();
        서울역.put("name", "서울역");
        강남역 = new HashMap<>();
        강남역.put("name", "강남역");
        양재역 = new HashMap<>();
        양재역.put("name", "양재역");

        GTXA노선 = new HashMap<>();
        GTXA노선.put("name", "GTX-A");
        GTXA노선.put("color", "bg-red-900");
        GTXA노선.put("upStationId", "1");
        GTXA노선.put("downStationId", "2");
        GTXA노선.put("distance", "10");

        GTXA노선_상행_정보없음 = new HashMap<>();
        GTXA노선_상행_정보없음.put("name", "GTX-A");
        GTXA노선_상행_정보없음.put("color", "bg-red-900");
        GTXA노선_상행_정보없음.put("downStationId", "2");
        GTXA노선_상행_정보없음.put("distance", "10");

        GTXA노선_하행_정보없음 = new HashMap<>();
        GTXA노선_하행_정보없음.put("name", "GTX-A");
        GTXA노선_하행_정보없음.put("color", "bg-red-900");
        GTXA노선_하행_정보없음.put("upStationId", "1");
        GTXA노선_하행_정보없음.put("distance", "10");

        GTXA노선_거리_정보없음 = new HashMap<>();
        GTXA노선_거리_정보없음.put("name", "GTX-A");
        GTXA노선_거리_정보없음.put("color", "bg-red-900");
        GTXA노선_거리_정보없음.put("upStationId", "1");
        GTXA노선_거리_정보없음.put("downStationId", "2");

        GTXA노선_상행_하행_정보같음 = new HashMap<>();
        GTXA노선_상행_하행_정보같음.put("name", "GTX-A");
        GTXA노선_상행_하행_정보같음.put("color", "bg-red-900");
        GTXA노선_상행_하행_정보같음.put("upStationId", "1");
        GTXA노선_상행_하행_정보같음.put("downStationId", "1");
        GTXA노선_상행_하행_정보같음.put("distance", "10");

        신분당선 = new HashMap<>();
        신분당선.put("name", "신분당선");
        신분당선.put("color", "bg-red-500");
        신분당선.put("upStationId", "2");
        신분당선.put("downStationId", "3");
        신분당선.put("distance", "10");

        노선색상 = new HashMap<>();
        노선색상.put("color", "bg-red-800");
    }
}
