package subway;

import static org.assertj.core.api.Assertions.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

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
        List<String> lineNames = 지하철_노선_목록_이름_조회();

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(lineNames).doesNotContain(LINE_분당선);
    }

    private Long 지하철_역_생성(String name) {
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

    private ExtractableResponse<Response> 지하철_노선_생성(String name, String color, Long upStationId, Long downStationId, Long distance) {
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
                .post(URI.create("/lines"))
            .then()
                .log().all()
            .extract();
    }

    private ExtractableResponse<Response> 지하철_분당선_생성() {
        return 지하철_노선_생성(LINE_분당선, "yellow", 수서역_id, 복정역_id, 10L);
    }

    private ExtractableResponse<Response> 지하철_3호선_생성() {
        return 지하철_노선_생성(LINE_3호선, "brown", 수서역_id, 오금역_id, 20L);
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회() {
        return RestAssured
            .given()
                .log().all()
            .when()
                .get("/lines")
            .then()
                .log().all()
            .extract();
    }

    private List<String> 지하철_노선_목록_이름_조회() {
        return 지하철_노선_목록_조회().jsonPath().getList("name", String.class);
    }

    private ExtractableResponse<Response> 지하철_노선_조회(Long id) {
        return RestAssured
            .given()
                .log().all()
            .when()
                .get("/lines/{id}", id)
            .then()
                .log().all()
            .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정(Long id, String name, String color) {
        Map<String, String> updateParam = new HashMap<>();
        updateParam.put("name", name);
        updateParam.put("color", color);

        return RestAssured
            .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateParam)
            .when()
                .put("/lines/{id}", id)
            .then()
                .log().all()
            .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_삭제(Long id) {
        return RestAssured
            .given()
                .log().all()
            .when()
                .delete("/lines/{id}", id)
            .then()
                .log().all()
            .extract();
    }
}
