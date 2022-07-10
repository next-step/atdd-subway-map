package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
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
    @DisplayName("지하철노선 생성한다.")
    @Test
    void createStationLine() {
        // given
        long upStationId = 지하철역_생성_요청("기흥역").jsonPath().getLong("id");
        long downStationId = 지하철역_생성_요청("신갈역").jsonPath().getLong("id");

        // when
        지하철노선_생성_요청("신분당선", "bg-red-600", upStationId, downStationId, 10);

        // then
        List<String> lineNames = 지하철노선_목록_조회().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsExactlyInAnyOrder("신분당선");
    }

    private ExtractableResponse<Response> 지하철노선_목록_조회() {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().get("/lines")
                          .then().log().all()
                          .extract();
    }

    private ExtractableResponse<Response> 지하철노선_생성_요청(final String lineName,
                                                      final String lineColor,
                                                      final long upStationId,
                                                      final long downStationId,
                                                      final int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", lineColor);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));

        return RestAssured.given().log().all()
                   .body(params)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when().post("/lines")
                   .then().log().all()
                   .extract();
    }

    private ExtractableResponse<Response> 지하철역_생성_요청(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/stations")
                          .then().log().all()
                          .extract();
    }
}
