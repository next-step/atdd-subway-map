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
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    int port;

    private final StationAcceptanceTest stationAcceptanceTest;

    static final String GANGNAM_STATION = "강남역";
    static final String YUKSAM_STATION = "역삼역";
    static final String NONHYUN_STATION = "논현역";

    static final String SHIN_BUNDANG_LINE = "신분당선";
    static final String FIRST_LINE = "1호선";
    static final String BUNDANG_LINE = "분당선";

    static final String RED_COLOR = "bg-red-600";
    static final String GREEN_COLOR = "bg-green-600";
    static final String BLUE_COLOR = "blue";

    static final Long DISTANCE = 5L;

    public LineAcceptanceTest() {
        this.stationAcceptanceTest = new StationAcceptanceTest();
    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선이 생성된다
     * Then 지하철 노선 목록 조회시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선_생성() {
        // when 지하철 노선을 생성하면
        Long upStationId = Long.valueOf(지하철역_생성(GANGNAM_STATION).jsonPath().getString("id"));
        Long downStationId = Long.valueOf(지하철역_생성(YUKSAM_STATION).jsonPath().getString("id"));
        ExtractableResponse<Response> response = 지하철_노선_생성(SHIN_BUNDANG_LINE, RED_COLOR, upStationId, downStationId, DISTANCE);
        assertAll(
                // then 지하철 노선이 생성된다
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.jsonPath().getString("name")).isEqualTo(SHIN_BUNDANG_LINE),
                () -> assertThat(response.jsonPath().getString("color")).isEqualTo(RED_COLOR),
                // then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
                () -> assertThat(지하철_노선_목록_조회().jsonPath().getList("name")).containsAnyOf(SHIN_BUNDANG_LINE)
        );
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 2개의 지하철 노선을 응답 받는다
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void 지하철_노선_목록_조회_테스트() {

        // given 2개의 지하철역을 생성하고
        지하철_노선_생성(SHIN_BUNDANG_LINE, RED_COLOR, Long.valueOf(지하철역_생성(GANGNAM_STATION).jsonPath().getString("id")), Long.valueOf(지하철역_생성(YUKSAM_STATION).jsonPath().getString("id")), DISTANCE);
        지하철_노선_생성(BUNDANG_LINE, BLUE_COLOR, Long.valueOf(지하철역_생성(GANGNAM_STATION).jsonPath().getString("id")), Long.valueOf(지하철역_생성(YUKSAM_STATION).jsonPath().getString("id")), DISTANCE);

        // when 지하철역 목록을 조회하면
        List<String> lineNames = 지하철_노선_목록_조회().jsonPath().getList("name");
        // then 2개의 지하철역을 응답 받는다
        assertAll(
                () -> assertThat(lineNames).containsAnyOf(SHIN_BUNDANG_LINE),
                () -> assertThat(lineNames).containsAnyOf(BUNDANG_LINE)
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void 지하철_노선_조회() {
        // given 지하철 노선을 생성하고
        Long upStationId = Long.valueOf(지하철역_생성(GANGNAM_STATION).jsonPath().getString("id"));
        Long downStationId = Long.valueOf(지하철역_생성(YUKSAM_STATION).jsonPath().getString("id"));
        ExtractableResponse<Response> createLine = 지하철_노선_생성(SHIN_BUNDANG_LINE, RED_COLOR, upStationId, downStationId, DISTANCE);
        // when 생성한 지하철 노선을 조회하면
        String lineName = 지하철_노선_조회(createLine.jsonPath().get("id")).jsonPath().get("name");
        // then 생성한 지하철 노선의 정보를 응답받을 수 있다.
        assertThat(lineName).isEqualTo(SHIN_BUNDANG_LINE);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다")
    @Test
    void 지하철_노선_수정() {
        // given 지하철 노선 생성
        Long upStationId = Long.valueOf(지하철역_생성(GANGNAM_STATION).jsonPath().getString("id"));
        Long downStationId = Long.valueOf(지하철역_생성(YUKSAM_STATION).jsonPath().getString("id"));
        ExtractableResponse<Response> createLine = 지하철_노선_생성(SHIN_BUNDANG_LINE, RED_COLOR, upStationId, downStationId, DISTANCE);
        // When 지하철 노선 수정
        지하철_노선_수정(Long.valueOf(createLine.jsonPath().getString("id")), FIRST_LINE, GREEN_COLOR);
        ExtractableResponse<Response> updatedLine = 지하철_노선_조회(createLine.jsonPath().get("id"));
        //    Then 해당 지하철 노선 정보는 수정된다
        assertAll(
                () -> assertThat(updatedLine.jsonPath().getString("name")).isEqualTo(FIRST_LINE),
                () -> assertThat(updatedLine.jsonPath().getString("color")).isEqualTo(GREEN_COLOR)
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다")
    @Test
    void 지하철_노선_삭제() {
        // given 지하철_노선_생성
        ExtractableResponse<Response> createLine = 지하철_노선_생성(SHIN_BUNDANG_LINE, RED_COLOR, Long.valueOf(지하철역_생성(GANGNAM_STATION).jsonPath().getString("id")),
                Long.valueOf(지하철역_생성(YUKSAM_STATION).jsonPath().getString("id")), DISTANCE);
        // When 지하철_노선_삭제
        ExtractableResponse<Response> deleteLineResponse = 지하철_노선_삭제(createLine.jsonPath().get("id"));
        // Then 해당 지하철 노선 정보를 찾을 수 없다
        assertThat(deleteLineResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 지하철역_생성(String stationName) {
        return stationAcceptanceTest.createStation(GANGNAM_STATION);
    }

    private ExtractableResponse<Response> 지하철_노선_생성(String name, String color, Long upStationId, Long downStationId, Long distance) {
        return RestAssured.given().log().all()
                .body(지하철_노선_생성_파라미터(name, color, String.valueOf(upStationId), String.valueOf(downStationId), String.valueOf(distance)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_조회(int id) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정(Long id, String name, String color) {
        return RestAssured.given().log().all()
                .body(지하철_노선_수정_파라미터(name, color))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_삭제(int id) {
        return RestAssured.given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private Map<String, String> 지하철_노선_수정_파라미터(String name, String color) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        return param;
    }

    private Map<String, String> 지하철_노선_생성_파라미터(String name, String color, String upStationId, String downStationId, String distance) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        param.put("upStationId", upStationId);
        param.put("downStationId", downStationId);
        param.put("distance", distance);
        return param;
    }

}