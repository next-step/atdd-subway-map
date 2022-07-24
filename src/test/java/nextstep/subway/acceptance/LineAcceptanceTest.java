package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.testsupport.AcceptanceTest;
import org.assertj.core.api.AbstractIntegerAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.support.LineRequest.지하철노선_목록조회_요청;
import static nextstep.subway.acceptance.support.LineRequest.지하철노선_삭제_요청;
import static nextstep.subway.acceptance.support.LineRequest.지하철노선_생성_요청;
import static nextstep.subway.acceptance.support.LineRequest.지하철노선_생성_요청후_식별자반환;
import static nextstep.subway.acceptance.support.LineRequest.지하철노선_수정_요청;
import static nextstep.subway.acceptance.support.LineRequest.지하철노선_조회_요청;
import static nextstep.subway.acceptance.support.StationRequest.지하철역_생성_요청;
import static nextstep.subway.acceptance.support.StationRequest.지하철역_생성_요청후_식별자_반환;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private static final int LINE_DISTANCE = 10;

    private long 상행역;
    private long 하행역;
    private long 신분당선;

    @BeforeEach
    public void setUp() {
        상행역 = 지하철역_생성_요청후_식별자_반환("기흥역");
        하행역 = 지하철역_생성_요청후_식별자_반환("신갈역");
        신분당선 = 지하철노선_생성_요청후_식별자반환("신분당선", "bg-red-600", 상행역, 하행역, LINE_DISTANCE);
    }

    /**
     * Given 지하철 노선을 생성하면
     * When 지하철 노선 목록 조회하면
     * Then 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선 생성한다.")
    @Test
    void createStationLine() {
        // when & then
        지하철_노선목록_조회후_생성한_노선_확인("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void findAllLine() {
        // given
        지하철노선_생성_요청("에버라인", "bg-red-600", 상행역, 하행역, LINE_DISTANCE);

        // when & then
        지하철_노선목록_조회후_생성한_노선_확인("신분당선", "에버라인");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void findLine() {
        // when
        final ExtractableResponse<Response> 지하철노선_조희_응답 = 지하철노선_조회_요청(신분당선);

        // then
        생성된_노선의_정보_확인(신분당선, 지하철노선_조희_응답);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateLine() {
        // when
        지하철노선_수정_요청(신분당선, "다른분당선", "bg-red-610");

        // then
        노선_수정여부_확인(신분당선, "다른분당선", "bg-red-610");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void deleteLine() {
        // when
        final ExtractableResponse<Response> 지하철노선_삭제_응답 = 지하철노선_삭제_요청(신분당선);

        // then
        지하철노선_삭제_확인(지하철노선_삭제_응답);
    }

    private void 지하철_노선목록_조회후_생성한_노선_확인(String... lineName) {
        List<String> lineNames = 지하철노선_목록조회_요청().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsExactlyInAnyOrder(lineName);
    }

    private void 노선_수정여부_확인(final long lineId, final String lineName, final String color) {
        final ExtractableResponse<Response> response = 지하철노선_조회_요청(lineId);
        assertAll(
            () -> assertThat(response.jsonPath().getString("name")).isEqualTo(lineName),
            () -> assertThat(response.jsonPath().getString("color")).isEqualTo(color)
                 );
    }

    private void 생성된_노선의_정보_확인(final long lineId, final ExtractableResponse<Response> response) {
        assertAll(
            () -> assertThat(response.jsonPath().getLong("id")).isEqualTo(lineId),
            () -> assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선"),
            () -> assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-600"),
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(상행역, 하행역),
            () -> assertThat(response.jsonPath().getList("stations.name", String.class)).containsExactly("기흥역", "신갈역")
                 );
    }

    private AbstractIntegerAssert<?> 지하철노선_삭제_확인(final ExtractableResponse<Response> response) {
        return assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
