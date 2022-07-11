package nextstep.subway.acceptance.acceptance;

import static nextstep.subway.acceptance.sample.LineSampleData.신분당선_노선을_생성한다;
import static nextstep.subway.acceptance.sample.LineSampleData.일호선_노선을_생성한다;
import static nextstep.subway.acceptance.template.LineRequestTemplate.노선을_생성한다;
import static nextstep.subway.acceptance.template.LineRequestTemplate.지하철노선_목록을_조회한다;
import static nextstep.subway.acceptance.template.LineRequestTemplate.지하철노선_생성을_요청한다;
import static nextstep.subway.acceptance.template.LineRequestTemplate.지하철노선을_수정한다;
import static nextstep.subway.acceptance.template.LineRequestTemplate.지하철노선을_조회한다;
import static nextstep.subway.acceptance.template.StationRequestTemplate.지하철역_생성을_요청한다;
import static nextstep.subway.acceptance.template.StationRequestTemplate.지하철역을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LineAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노산이 생성된다
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void 지하철역노선_생성() {
        // when
        ExtractableResponse<Response> createdResponse = 신분당선_노선을_생성한다();

        // then
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> linesRequest = 지하철노선_목록을_조회한다();
        List<String> lineNames = linesRequest.jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철역노선 목록을 조회한다.")
    @Test
    void 지하철역노선_목록_조회() {
        // given
        신분당선_노선을_생성한다();
        일호선_노선을_생성한다();

        // when
        ExtractableResponse<Response> linesResponse = 지하철노선_목록을_조회한다();

        // then
        List<String> lineNames = linesResponse.jsonPath().getList("name", String.class);
        assertThat(lineNames).containsOnlyOnce("신분당선", "1호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철역노선을 조회한다.")
    @Test
    void 지하철역노선_조회() {
        // given
        long lineId = 신분당선_노선을_생성한다().jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> lineResponse = 지하철노선을_조회한다(lineId);

        // then
        assertThat(lineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        String lineName = lineResponse.jsonPath().getObject("name", String.class);
        assertThat(lineName).containsAnyOf("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철역노선을 수정한다.")
    @Test
    void 지하철역노선_수정() {
        // given
        long lineId = 신분당선_노선을_생성한다().jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> lineUpdatedResponse = 지하철노선을_수정한다(lineId, "신도림역", "bg-green-600");

        // then
        assertThat(lineUpdatedResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> lineResponse = 지하철노선을_조회한다(lineId);
        String lineName = lineResponse.jsonPath().getObject("name", String.class);
        assertThat(lineName).containsAnyOf("신도림역");

        String lineColor = lineResponse.jsonPath().getObject("color", String.class);
        assertThat(lineColor).containsAnyOf("bg-green-600");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철역노선을 삭제한다.")
    @Test
    void 지하철역노선_삭제() {
    }
}
