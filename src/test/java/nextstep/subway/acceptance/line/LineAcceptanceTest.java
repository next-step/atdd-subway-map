package nextstep.subway.acceptance.line;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.line.LineSteps.지하철노선_생성_요청;
import static nextstep.subway.acceptance.line.LineSteps.지하철노선_생성_결과;
import static nextstep.subway.acceptance.line.LineSteps.지하철노선_수정_요청;
import static nextstep.subway.acceptance.line.LineSteps.지하철노선_삭제_요청;
import static nextstep.subway.acceptance.line.LineSteps.지하철노선_조회_요청;
import static nextstep.subway.acceptance.line.LineSteps.지하철노선_조회_결과;
import static nextstep.subway.acceptance.line.LineSteps.지하철노선목록_조회_결과;
import static nextstep.subway.acceptance.station.StationSteps.지하철역_생성_결과;
import static org.assertj.core.api.Assertions.assertThat;

class LineAcceptanceTest extends AcceptanceTest {
    private static final String NOT_EXIST_LINE_PATH = "/lines/1000";
    private static final Long NOT_EXIST_STATION_ID = 1000L;

    private Long 잠실역;
    private Long 선릉역;
    private Long 강남역;
    private Long 교대역;

    @BeforeEach
    void init() throws JsonProcessingException {
        잠실역 = 지하철역_생성_결과("잠실역").getId();
        선릉역 = 지하철역_생성_결과("선릉역").getId();
        강남역 = 지하철역_생성_결과("강남역").getId();
        교대역 = 지하철역_생성_결과("교대역").getId();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() throws JsonProcessingException {
        ExtractableResponse<Response> 일호선 = 지하철노선_생성_요청("1호선", "br-red-600", 10, 잠실역, 선릉역);

        List<LineResponse> 지하철노선목록 = 지하철노선목록_조회_결과();

        assertThat(지하철노선목록).containsExactly(일호선.as(LineResponse.class));
    }

    /**
     * When 존재하지 않는 지하철 역으로 지하철 노선을 생성하면
     * Then Internal Server Error가 발생한다.
     */
    @DisplayName("존재하지 않는 지하철역으로 지하철 노선을 생성할 수 없다.")
    @Test
    void createLineWithWrongStationId() throws JsonProcessingException {
        ExtractableResponse<Response> 일호선 = 지하철노선_생성_요청("1호선", "br-red-600", 10, 잠실역, NOT_EXIST_STATION_ID);

        assertThat(일호선.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void getLines() throws JsonProcessingException {
        LineResponse 일호선 = 지하철노선_생성_결과("1호선", "br-red-600", 10, 잠실역, 선릉역);
        LineResponse 이호선 = 지하철노선_생성_결과("2호선", "br-red-800", 10, 강남역, 교대역);

        List<LineResponse> 지하철노선목록 = 지하철노선목록_조회_결과();

        assertThat(지하철노선목록).containsExactly(일호선, 이호선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void getLine() throws JsonProcessingException {
        ExtractableResponse<Response> 일호선 = 지하철노선_생성_요청("1호선", "br-red-600", 10, 잠실역, 선릉역);

        assertThat(일호선.as(LineResponse.class))
                .isEqualTo(지하철노선_조회_결과(일호선.header("Location")));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 존재하지 않는 지하철의 노선을 조회하면
     * Then Internal Server Error가 발생한다.
     */
    @DisplayName("존재하지 않는 지하철노선은 조회할 수 없다.")
    @Test
    void getLineWithWrongId() throws JsonProcessingException {
        지하철노선_생성_요청("1호선", "br-red-600", 10, 잠실역, 선릉역);

        assertThat(지하철노선_조회_요청(NOT_EXIST_LINE_PATH).statusCode())
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() throws JsonProcessingException {
        ExtractableResponse<Response> 일호선 = 지하철노선_생성_요청("1호선", "br-red-600", 10, 잠실역, 선릉역);
        Long 일호선_아이디 = 일호선.as(LineResponse.class).getId();

        지하철노선_수정_요청(일호선.header("Location"), "3호선", "br-blue-600");

        assertThat(지하철노선_조회_결과(일호선.header("Location")))
                .isEqualTo(LineResponse.from(new Line(일호선_아이디, "3호선", "br-blue-600", 10, new Station(잠실역, "잠실역"), new Station(선릉역, "선릉역"))));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 존재하지 않는 지하철 노선을 수정하면
     * Then Internal Server Error가 발생한다.
     */
    @DisplayName("존재하지 않는 지하철노선은 수정할 수 없다.")
    @Test
    void updateLineWithWrongId() throws JsonProcessingException {
        지하철노선_생성_요청("1호선", "br-red-600", 10, 잠실역, 선릉역);

        assertThat(지하철노선_수정_요청(NOT_EXIST_LINE_PATH, "3호선", "br-blue-600").statusCode())
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() throws JsonProcessingException {
        ExtractableResponse<Response> 일호선 = 지하철노선_생성_요청("1호선", "br-red-600", 10, 잠실역, 선릉역);

        지하철노선_삭제_요청(일호선.header("Location"));

        assertThat(지하철노선목록_조회_결과())
                .doesNotContain(LineResponse.from(new Line(1L, "3호선", "br-blue-600", 10, new Station(잠실역, "잠실역"), new Station(선릉역, "선릉역"))));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 존재하지 않는 지하철 노선을 삭제하면
     * Then Internal Server Error가 발생한다.
     */
    @DisplayName("존재하지 않는 지하철 노선은 삭제할 수 없다.")
    @Test
    void deleteLineWithWrongId() throws JsonProcessingException {
        지하철노선_생성_요청("1호선", "br-red-600", 10, 잠실역, 선릉역);

        assertThat(지하철노선_삭제_요청(NOT_EXIST_LINE_PATH).statusCode())
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
