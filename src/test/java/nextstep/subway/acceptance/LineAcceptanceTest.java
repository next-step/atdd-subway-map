package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    public static final String 다른분당선 = "다른분당선";
    public static final String 신분당선 = "신분당선";
    public static final String 분당선 = "분당선";

    public static final String BG_RED_600 = "bg-red-600";
    public static final String BG_BLUE_600 = "bg-blue-600";
    public static final String BG_GREEN_600 = "bg-green-600";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    private StationAcceptanceTest stationAcceptanceTest;

    private Long 지하철역Id;
    private Long 새로운지하철역Id;
    private Long 또다른지하철역Id;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        stationAcceptanceTest = new StationAcceptanceTest();
        지하철역Id = stationAcceptanceTest.지하철역_생성("지하철역").jsonPath().getLong("id");
        새로운지하철역Id = stationAcceptanceTest.지하철역_생성("새로운지하철역").jsonPath().getLong("id");
        또다른지하철역Id = stationAcceptanceTest.지하철역_생성("또다른지하철역").jsonPath().getLong("id");
    }


    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성합니다.")
    @Test
    void createLine() {
        //when
        ExtractableResponse<Response> lineResponse = 지하철_노선_생성(신분당선, BG_RED_600, 지하철역Id, 새로운지하철역Id, 10);

        //then
        ExtractableResponse<Response> linesResponse = 지하철_노선목록_조회();
        assertThatStatus(linesResponse, HttpStatus.OK);
        assertThat(linesResponse.jsonPath().getList("lines").size()).isEqualTo(1);
        assertThat(lineResponse.jsonPath().getLong("id")).isEqualTo(1L);
        assertThat(lineResponse.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(lineResponse.jsonPath().getString("color")).isEqualTo("bg-red-600");
        assertThat(lineResponse.jsonPath().getList("stations").size()).isEqualTo(2);

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회합니다.")
    @Test
    void getLines() {
        //given
        ExtractableResponse<Response> 신분당선 = 지하철_노선_생성(LineAcceptanceTest.신분당선, BG_RED_600, 지하철역Id, 새로운지하철역Id, 10);
        ExtractableResponse<Response> 분당선 = 지하철_노선_생성(LineAcceptanceTest.분당선, BG_GREEN_600, 지하철역Id, 또다른지하철역Id, 12);
        assertThatStatus(신분당선, HttpStatus.CREATED);
        assertThatStatus(분당선, HttpStatus.CREATED);

        //when
        ExtractableResponse<Response> linesResponse = 지하철_노선목록_조회();

        //then
        assertThat(linesResponse.jsonPath().getList("$")).hasSize(2);
        assertThatStatus(linesResponse, HttpStatus.OK);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회합니다.")
    @Test
    void getLine() {
        //given
        ExtractableResponse<Response> 신분당선 = 지하철_노선_생성(LineAcceptanceTest.신분당선, BG_RED_600, 지하철역Id, 새로운지하철역Id, 10);
        assertThatStatus(신분당선, HttpStatus.CREATED);

        Long 신분당선Id = 신분당선.jsonPath()
                .getLong("id");

        //when
        ExtractableResponse<Response> lineResponse = 지하철_노선_조회(신분당선Id);

        //then
        assertThatStatus(lineResponse, HttpStatus.OK);
        assertThat(lineResponse.jsonPath().getLong("id")).isEqualTo(1L);
        assertThat(lineResponse.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(lineResponse.jsonPath().getString("color")).isEqualTo("bg-red-600");
        assertThat(lineResponse.jsonPath().getList("stations").size()).isEqualTo(2);

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("지하철 노선을 수정합니다.")
    @Test
    void updateLine() {
        //given
        ExtractableResponse<Response> 신분당선 = 지하철_노선_생성(LineAcceptanceTest.신분당선, BG_RED_600, 지하철역Id, 새로운지하철역Id, 10);
        assertThatStatus(신분당선, HttpStatus.CREATED);

        Long 신분당선Id = 신분당선.jsonPath()
                .getLong("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", 다른분당선);
        params.put("color", BG_BLUE_600);

        //when
        ExtractableResponse<Response> updateLineResponse = 지하철_노선_수정(신분당선Id, params);

        //then
        assertThatStatus(updateLineResponse, HttpStatus.OK);

        ExtractableResponse<Response> getLineResponse = 지하철_노선_조회(신분당선Id);
        assertThat(getLineResponse.jsonPath().getLong("id")).isEqualTo(1L);
        assertThat(getLineResponse.jsonPath().getString("name")).isEqualTo(다른분당선);
        assertThat(getLineResponse.jsonPath().getString("color")).isEqualTo(BG_BLUE_600);
        assertThat(getLineResponse.jsonPath().getList("stations").size()).isEqualTo(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("지하철 노선을 제거합니다.")
    @Test
    void deleteLine() {
        //given
        ExtractableResponse<Response> 신분당선 = 지하철_노선_생성(LineAcceptanceTest.신분당선, BG_RED_600, 지하철역Id, 새로운지하철역Id, 10);
        assertThatStatus(신분당선, HttpStatus.CREATED);

        Long 신분당선Id = 신분당선.jsonPath()
                .getLong("id");

        //when
        ExtractableResponse<Response> updateLineResponse = 지하철_노선_제거(신분당선Id);

        //then
        assertThatStatus(updateLineResponse, HttpStatus.NO_CONTENT);
    }

    private ExtractableResponse<Response> 지하철_노선_수정(Long stationId, Map<String, Object> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/lines/{id}", stationId)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_제거(Long stationId) {
        return RestAssured.given().log().all()
                .when().delete("/lines/{id}", stationId)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_조회(Long stationId) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{id}", stationId)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선목록_조회() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_생성(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        ExtractableResponse<Response> lineResponse = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then()
                .log().all()
                .extract();
        assertThatStatus(lineResponse, HttpStatus.CREATED);
        return lineResponse;
    }

    private void assertThatStatus(ExtractableResponse<Response> response, HttpStatus expectedStatus) {
        assertThat(response.statusCode()).isEqualTo(expectedStatus.value());
    }
}
