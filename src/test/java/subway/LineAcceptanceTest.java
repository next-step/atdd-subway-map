package subway;

import static org.assertj.core.api.Assertions.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private static final String URL_LINES = "/lines";
    private static final String LINE_분당선 = "분당선";
    private static final String LINE_3호선 = "3호선";
    private static final String STATION_수서역 = "수서역";
    private static final String STATION_복정역 = "복정역";
    private static final String STATION_오금역 = "오금역";

    private Long 수서역_id;
    private Long 복정역_id;
    private Long 오금역_id;

    @BeforeEach
    void setup() {
        super.setup();

        수서역_id = 지하철_역_생성(STATION_수서역);
        복정역_id = 지하철_역_생성(STATION_복정역);
        오금역_id = 지하철_역_생성(STATION_오금역);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> createResponse = 지하철_분당선_생성();

        // then
        List<String> lineNames = 지하철_노선_목록_이름_조회();

        assertThat(lineNames).contains(LINE_분당선);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        지하철_분당선_생성();
        지하철_3호선_생성();

        // when
        ExtractableResponse<Response> getResponse = 지하철_노선_목록_조회();

        // then
        List<String> lineNames = getResponse.jsonPath().getList("name", String.class);

        assertThat(lineNames).hasSize(2).contains(LINE_분당선, LINE_3호선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        Long lineId = 지하철_분당선_생성().jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> getResponse = 지하철_노선_조회(lineId);

        // then
        assertThat(getResponse.jsonPath().getString("name")).isEqualTo(LINE_분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        Long lineId = 지하철_분당선_생성().jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> putResponse = 지하철_노선_수정(lineId, "새로운 분당선", "lightyellow");

        // then
        ExtractableResponse<Response> getResponse = 지하철_노선_조회(lineId);

        assertThat(putResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.jsonPath().getString("name")).isEqualTo("새로운 분당선");
        assertThat(getResponse.jsonPath().getString("color")).isEqualTo("lightyellow");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        Long lineId = 지하철_분당선_생성().jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> deleteResponse = 지하철_노선_삭제(lineId);

        // then
        ExtractableResponse<Response> getResponse = 지하철_노선_조회(lineId);

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * When 지하철 노선 생성 요청 시, 상행역과 하행역이 동일하면
     * Then 지하철 노선이 생성되지 않는다.
     */
    @DisplayName("상행역과 하행역이 동일하면 지하철 노선이 생성되지 않는다.")
    @Test
    void identicalStations() {
        // when
        ExtractableResponse<Response> createResponse = 지하철_노선_생성(LINE_3호선, "brown", 수서역_id, 수서역_id, 1);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    static Long 지하철_역_생성(String name) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);

        return RestAssured
            .given()
                .log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post(URI.create("/stations"))
            .then()
                .log().all()
            .extract()
            .jsonPath()
            .getLong("id");
    }

    private ExtractableResponse<Response> 지하철_분당선_생성() {
        return 지하철_노선_생성(LINE_분당선, "yellow", 수서역_id, 복정역_id, 5);
    }

    private ExtractableResponse<Response> 지하철_3호선_생성() {
        return 지하철_노선_생성(LINE_3호선, "brown", 수서역_id, 오금역_id, 7);
    }

    static ExtractableResponse<Response> 지하철_노선_생성(String name, String color, Long upStationId, Long downStationId, long distance) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        param.put("upStationId", upStationId);
        param.put("downStationId", downStationId);
        param.put("distance", distance);

        return RestAssured
            .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
            .when()
                .post(URI.create(URL_LINES))
            .then()
                .log().all()
            .extract();
    }

    static ExtractableResponse<Response> 지하철_노선_목록_조회() {
        return RestAssured
            .given()
                .log().all()
            .when()
                .get(URL_LINES)
            .then()
                .log().all()
            .extract();
    }

    static List<String> 지하철_노선_목록_이름_조회() {
        return 지하철_노선_목록_조회().jsonPath().getList("name", String.class);
    }

    static ExtractableResponse<Response> 지하철_노선_조회(Long id) {
        return RestAssured
            .given()
                .log().all()
            .when()
                .get(URL_LINES + "/{id}", id)
            .then()
                .log().all()
            .extract();
    }

    static ExtractableResponse<Response> 지하철_노선_수정(Long id, String name, String color) {
        Map<String, String> updateParam = new HashMap<>();
        updateParam.put("name", name);
        updateParam.put("color", color);

        return RestAssured
            .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateParam)
            .when()
                .put(URL_LINES + "/{id}", id)
            .then()
                .log().all()
            .extract();
    }

    static ExtractableResponse<Response> 지하철_노선_삭제(Long id) {
        return RestAssured
            .given()
                .log().all()
            .when()
                .delete(URL_LINES + "/{id}", id)
            .then()
                .log().all()
            .extract();
    }
}
