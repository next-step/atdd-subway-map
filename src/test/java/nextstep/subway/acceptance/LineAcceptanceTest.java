package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.util.List;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private static final String 이호선 = "2호선";
    private static final String BG_GREEN_600 = "bg-green-600";
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";
    private static final int 강남역_역삼역_거리 = 7;

    private static final String 신분당선 = "신분당선";
    private static final String BG_RED_600 = "bg-red-600";
    private static final String 미금역 = "미금역";
    private static final String 양재역 = "양재역";
    private static final int 미금역_양재역_거리 = 2;

    private Long 강남역_아이디, 역삼역_아이디, 미금역_아이디, 양재역_아이디;

    @BeforeEach
    void beforeEach() {
        강남역_아이디 = 지하철역_생성_요청(강남역).jsonPath().getLong("id");
        역삼역_아이디 = 지하철역_생성_요청(역삼역).jsonPath().getLong("id");
        미금역_아이디 = 지하철역_생성_요청(미금역).jsonPath().getLong("id");
        양재역_아이디 = 지하철역_생성_요청(양재역).jsonPath().getLong("id");
    }

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> createResponse =
                지하철_노선_생성_요청(신분당선, BG_RED_600, 미금역_아이디, 양재역_아이디, 미금역_양재역_거리);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        지하철_노선_생성_요청(신분당선, BG_RED_600, 미금역_아이디, 양재역_아이디, 미금역_양재역_거리);
        지하철_노선_생성_요청(이호선, BG_GREEN_600, 강남역_아이디, 역삼역_아이디, 강남역_역삼역_거리);

        // when
        ExtractableResponse<Response> getResponse = 지하철_노선_조회_요청();

        // then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = getResponse.jsonPath().getList("name");
        assertThat(lineNames).contains(신분당선, 이호선);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse =
                지하철_노선_생성_요청(신분당선, BG_RED_600, 미금역_아이디, 양재역_아이디, 미금역_양재역_거리);

        // when
        ExtractableResponse<Response> getResponse = 지하철_노선_조회_요청(createResponse.header("Location"));

        // then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.jsonPath().getString("name")).isEqualTo(신분당선);
        assertThat(getResponse.jsonPath().getString("color")).isEqualTo(BG_RED_600);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse =
                지하철_노선_생성_요청(신분당선, BG_RED_600, 미금역_아이디, 양재역_아이디, 미금역_양재역_거리);

        // when
        ExtractableResponse<Response> updateResponse =
                지하철_노선_수정_요청(createResponse.header("Location"), 이호선, BG_GREEN_600);

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updateResponse.jsonPath().getString("name")).isEqualTo(이호선);
        assertThat(updateResponse.jsonPath().getString("color")).isEqualTo(BG_GREEN_600);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse =
                지하철_노선_생성_요청(신분당선, BG_RED_600, 미금역_아이디, 양재역_아이디, 미금역_양재역_거리);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철_노선_삭제_요청(createResponse.header("Location"));

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void duplicateLine() {
        // given
        지하철_노선_생성_요청(신분당선, BG_RED_600, 미금역_아이디, 양재역_아이디, 미금역_양재역_거리);

        // when
        ExtractableResponse<Response> duplicateResponse =
                지하철_노선_생성_요청(신분당선, BG_RED_600, 미금역_아이디, 양재역_아이디, 미금역_양재역_거리);

        // then
        assertThat(duplicateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
