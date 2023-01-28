package subway;

import common.Colors;
import common.Endpoints;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import utils.JsonBodyParam;
import utils.JsonBodyParams;
import utils.RestAssuredClient;

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
        // when 지하철 노선을 생성하면
        ExtractableResponse<Response> response = RestAssuredClient.post(
                Endpoints.LINES,
                new JsonBodyParams(List.of(
                        new JsonBodyParam("name", "신분당선"),
                        new JsonBodyParam("color", Colors.RED),
                        new JsonBodyParam("upStationId", "1"),
                        new JsonBodyParam("downStationId", "2"),
                        new JsonBodyParam("distance", "10")
                )).toMap()
        );

        // then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
        JsonPath getLineResponseJsonPath = RestAssuredClient.get(response.header("Location")).jsonPath();

        assertThat(getLineResponseJsonPath.getString("name")).isEqualTo("신분당선");
        assertThat(getLineResponseJsonPath.getString("color")).isEqualTo(Colors.RED);
        assertThat(getLineResponseJsonPath.getLong("upStationId")).isEqualTo(1L);
        assertThat(getLineResponseJsonPath.getLong("downStationId")).isEqualTo(2L);
        assertThat(getLineResponseJsonPath.getLong("distance")).isEqualTo(10L);
    }
}
