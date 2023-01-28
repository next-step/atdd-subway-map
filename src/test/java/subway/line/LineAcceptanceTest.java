package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import subway.common.Colors;
import subway.common.Endpoints;
import subway.station.StationResponse;
import subway.utils.JsonBodyParam;
import subway.utils.RestAssuredClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        ExtractableResponse<Response> 강남역_생성_응답 = RestAssuredClient.post(Endpoints.STATIONS, new JsonBodyParam("name", "강남역").toMap());
        ExtractableResponse<Response> 서울대입구역_생성_응답 = RestAssuredClient.post(Endpoints.STATIONS, new JsonBodyParam("name", "서울대입구역").toMap());

        // when 지하철 노선을 생성하면
        long 강남역_아이디 = 강남역_생성_응답.jsonPath().getLong("id");
        long 서울대입구역_아이디 = 서울대입구역_생성_응답.jsonPath().getLong("id");

        RestAssuredClient.post(
                Endpoints.LINES,
                new LineRequest(
                        "신분당선",
                        Colors.RED,
                        강남역_아이디,
                        서울대입구역_아이디,
                        10L
                )
        );

        // then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
        List<LineResponse> lineResponses = RestAssuredClient.get(Endpoints.LINES).jsonPath().getList(".", LineResponse.class);
        assertThat(lineResponses).hasSize(1);

        var lineResponse = lineResponses.get(0);
        assertThat(lineResponse.getName()).isEqualTo("신분당선");
        assertThat(lineResponse.getColor()).isEqualTo(Colors.RED);
        assertThat(lineResponse.getStations()).hasSize(2);
        assertThat(lineResponse.getStations().stream().map(StationResponse::getId))
                .containsExactly(
                        강남역_아이디,
                        서울대입구역_아이디
                );
    }
}
