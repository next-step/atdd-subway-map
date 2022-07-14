package nextstep.subway.acceptance.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static nextstep.subway.acceptance.line.LinePrepare.지하철노선_생성_요청;
import static nextstep.subway.acceptance.line.LinePrepare.지하철노선_조회_요청;
import static nextstep.subway.acceptance.line.LinePrepare.지하철노선목록_조회_요청;
import static nextstep.subway.acceptance.line.LinePrepare.지하철노선_수정_요청;
import static nextstep.subway.acceptance.line.LinePrepare.지하철노선_삭제_요청;
import static org.assertj.core.api.Assertions.assertThat;

@Sql("classpath:/database-init.sql")
@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        ExtractableResponse<Response> 일호선 = 지하철노선_생성_요청("1호선", "br-red-600", 1L, 2L);

        List<Line> 지하철노선목록 = 지하철노선목록_조회_요청();

        assertThat(지하철노선목록).containsExactly(일호선.as(Line.class));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void getLines() {
        ExtractableResponse<Response> 일호선 = 지하철노선_생성_요청("1호선", "br-red-600", 1L, 2L);
        ExtractableResponse<Response> 이호선 = 지하철노선_생성_요청("2호선", "br-red-700", 3L, 4L);

        List<Line> 지하철노선목록 = 지하철노선목록_조회_요청();

        assertThat(지하철노선목록).containsExactly(일호선.as(Line.class), 이호선.as(Line.class));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void getLine() {
        ExtractableResponse<Response> 일호선 = 지하철노선_생성_요청("1호선", "br-red-600", 1L, 2L);

        assertThat(일호선.as(Line.class)).isEqualTo(지하철노선_조회_요청(일호선.header("Location")));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        ExtractableResponse<Response> 일호선 = 지하철노선_생성_요청("1호선", "br-red-600", 1L, 2L);

        지하철노선_수정_요청(일호선.header("Location"), "3호선", "br-blue-600");

        assertThat(지하철노선_조회_요청(일호선.header("Location"))).isEqualTo(new Line(1L, "3호선", "br-blue-600", 1L, 2L));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {
        ExtractableResponse<Response> 일호선 = 지하철노선_생성_요청("1호선", "br-red-600", 1L, 2L);

        지하철노선_삭제_요청(일호선.header("Location"));

        assertThat(지하철노선목록_조회_요청()).doesNotContain(new Line(1L, "1호선", "br-red-600", 1L, 2L));
    }
}
