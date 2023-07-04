package subway.acceptance;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import subway.line.controller.dto.LineResponse;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    @BeforeEach
    void initStations() {
        지하철_역_등록("첫번째역");
        지하철_역_등록("두번째역");
        지하철_역_등록("세번째역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> savedResponse = 지하철_노선도_등록("신분당선",  "bg-red-600", 1L, 2L, 10);
        assertThat(savedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        List<LineResponse> lines = 지하철_노선_목록_조회();
        LineResponse 신분당선 = lines.get(0);
        assertThat(lines).hasSize(1);
        assertThat(신분당선.getId()).isEqualTo(1L);
        assertThat(신분당선.getName()).isEqualTo("신분당선");
        assertThat(신분당선.getColor()).isEqualTo("bg-red-600");
        assertThat(신분당선.getStations()).hasSize(2);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다")
    @Test
    void showLines() {
        //given
        지하철_노선도_등록("신분당선",  "bg-red-600", 1L, 2L, 10);
        지하철_노선도_등록("7호선",  "bg-red-100", 1L, 3L, 10);

        //when
        List<LineResponse> lines = 지하철_노선_목록_조회();

        //then
        assertThat(lines).hasSize(2);

        LineResponse 신분당선 = lines.get(0);
        assertThat(신분당선.getId()).isEqualTo(1L);
        assertThat(신분당선.getName()).isEqualTo("신분당선");
        assertThat(신분당선.getColor()).isEqualTo("bg-red-600");
        assertThat(신분당선.getStations()).hasSize(2);

        LineResponse line7 = lines.get(1);
        assertThat(line7.getId()).isEqualTo(2L);
        assertThat(line7.getName()).isEqualTo("7호선");
        assertThat(line7.getColor()).isEqualTo("bg-red-100");
        assertThat(line7.getStations()).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다")
    @Test
    void showLine() {
        //given
        ExtractableResponse<Response> response = 지하철_노선도_등록("신분당선",  "bg-red-600", 2L, 3L, 5);
        Long savedId = response.jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> searchResponse = 지하철_노선_조회(savedId);

        //then
        assertThat(searchResponse.statusCode()).isEqualTo(200);
        LineResponse 신분당선 = searchResponse.jsonPath().getObject("", LineResponse.class);
        assertThat(신분당선.getId()).isEqualTo(savedId);
        assertThat(신분당선.getName()).isEqualTo("신분당선");
        assertThat(신분당선.getColor()).isEqualTo("bg-red-600");
        assertThat(신분당선.getStations()).hasSize(2);
    }

    private ExtractableResponse<Response> 지하철_노선_조회(Long id) {
        String pathVariable = "/" + id;
        return RestAssured.given().log().all()
                .when().get("/lines" + pathVariable)
                .then().log().all()
                .extract();
    }


    private ExtractableResponse<Response> 지하철_노선도_등록(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return RestAssured.given().log().all()
                .body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private List<LineResponse> 지하철_노선_목록_조회() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract()
                .jsonPath()
                .getList("", LineResponse.class);
    }

    private ExtractableResponse<Response> 지하철_역_등록(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }
}
