package nextstep.subway.acceptance;

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
import org.springframework.http.MediaType;

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
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void 지하철역노선_생성() {
        // when
        ExtractableResponse<Response> response = 신분당선_노선을_생성한다();

        // then
        ExtractableResponse<Response> linesRequest = readAllLinesRequest();
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
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철역노선을 조회한다.")
    @Test
    void 지하철역노선_조회() {
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철역노선을 수정한다.")
    @Test
    void 지하철역노선_수정() {
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

    private ExtractableResponse<Response> 신분당선_노선을_생성한다() {
        long downStationId = 지하철역을_생성한다("광교역").jsonPath().getLong("id");
        long upStationId = 지하철역을_생성한다("신사역").jsonPath().getLong("id");

        return 노선을_생성한다("신분당선", "bg-red-600", downStationId, upStationId, (long) 10);
    }

    private ExtractableResponse<Response> 노선을_생성한다(String name, String color,
                                                   Long downStationId, Long upStationId, Long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return createLineRequest(params);
    }

    private ExtractableResponse<Response> 지하철역을_생성한다(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);

        return createStationRequest(params);
    }

    private ExtractableResponse<Response> createStationRequest(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createLineRequest(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> readAllLinesRequest() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }
}
