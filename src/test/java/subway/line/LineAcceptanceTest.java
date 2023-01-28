package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.StationApiClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineApiClient.requestCreateLine;
import static subway.line.LineApiClient.requestShowLines;

@DisplayName("지하철 노선 관리 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private Long upStationId;
    private Long downStationId;

    @BeforeEach
    void initStations() {
        ExtractableResponse<Response> upStationResponse = StationApiClient.requestCreateStation("장암");
        this.upStationId = upStationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> downStationResponse = StationApiClient.requestCreateStation("도봉산");
        this.downStationId = downStationResponse.jsonPath().getLong("id");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> createLineResponse = requestCreateLine("7호선", "#747F00", upStationId, downStationId, 12);
        String name = createLineResponse.jsonPath().getString("name");

        assertThat(createLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(name).isEqualTo("7호선");

        // then
        ExtractableResponse<Response> showLinesResponse = requestShowLines();
        List<String> names = showLinesResponse.jsonPath().getList("name", String.class);

        assertThat(names).containsAnyOf("7호선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void showLines() {
        // given
        requestCreateLine("1호선", "#0052A4", upStationId, downStationId, 8);
        requestCreateLine("2호선", "#00A84D", upStationId, downStationId, 10);

        // when
        ExtractableResponse<Response> showLinesResponse = requestShowLines();
        List<String> names = showLinesResponse.jsonPath().getList("name", String.class);

        // then
        assertThat(showLinesResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(names).containsExactly("1호선", "2호선");
    }
}
