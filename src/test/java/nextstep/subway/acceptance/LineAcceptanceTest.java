package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void findAllLine() {
        // given
        long upStationId1 = 지하철역_생성_요청("기흥역").jsonPath().getLong("id");
        long downStationId1 = 지하철역_생성_요청("신갈역").jsonPath().getLong("id");
        지하철노선_생성_요청("신분당선", "bg-red-600", upStationId1, downStationId1, 10);
        long upStationId2 = 지하철역_생성_요청("동백역").jsonPath().getLong("id");
        long downStationId2 = 지하철역_생성_요청("강남대역").jsonPath().getLong("id");
        지하철노선_생성_요청("에버라인", "bg-red-600", upStationId2, downStationId2, 10);

        // when
        List<String> lineNames = 지하철노선_목록_조회().jsonPath().getList("name", String.class);

        // then
        assertThat(lineNames).containsExactlyInAnyOrder("신분당선", "에버라인");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void findLine() {
        // given
        long upStationId = 지하철역_생성_요청("기흥역").jsonPath().getLong("id");
        long downStationId = 지하철역_생성_요청("신갈역").jsonPath().getLong("id");
        long lineId = 지하철노선_생성_요청("신분당선", "bg-red-600", upStationId, downStationId, 10).jsonPath().getLong("id");

        // when
        final ExtractableResponse<Response> response = 지하철노선_조회(lineId);

        assertAll(
            () -> assertThat(response.jsonPath().getLong("id")).isEqualTo(lineId),
            () -> assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선"),
            () -> assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-600"),
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(upStationId, downStationId),
            () -> assertThat(response.jsonPath().getList("stations.name", String.class)).containsExactly("기흥역", "신갈역")
                 );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateLine() {
        // given
        long upStationId = 지하철역_생성_요청("기흥역").jsonPath().getLong("id");
        long downStationId = 지하철역_생성_요청("신갈역").jsonPath().getLong("id");
        long lineId = 지하철노선_생성_요청("신분당선", "bg-red-600", upStationId, downStationId, 10).jsonPath().getLong("id");

        // when
        지하철노선_수정_요청(lineId, "다른분당선", "bg-red-610");

        // then
        final ExtractableResponse<Response> response = 지하철노선_조회(lineId);
        assertAll(
            () -> assertThat(response.jsonPath().getString("name")).isEqualTo("다른분당선"),
            () -> assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-610")
                 );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void deleteLine(){
        // given
        long upStationId = 지하철역_생성_요청("기흥역").jsonPath().getLong("id");
        long downStationId = 지하철역_생성_요청("신갈역").jsonPath().getLong("id");
        long lineId = 지하철노선_생성_요청("신분당선", "bg-red-600", upStationId, downStationId, 10).jsonPath().getLong("id");

        // when
        final ExtractableResponse<Response> response = 지하철노선_삭제_요청(lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 지하철노선_삭제_요청(final long lineId) {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().delete("/lines/" + lineId)
                          .then().log().all()
                          .extract();
    }

    private ExtractableResponse<Response> 지하철노선_수정_요청(final long lineId, final String lineName, final String lineColor) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", lineColor);

        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().put("/lines/" + lineId)
                          .then().log().all()
                          .extract();
    }

    private ExtractableResponse<Response> 지하철노선_조회(final long lineId) {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().get("/lines/" + lineId)
                          .then().log().all()
                          .extract();
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
