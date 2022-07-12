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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    int port;

    private static final Map<String, Object> LINE_5 = new HashMap<>();
    private static final Map<String, Object> LINE_2 = new HashMap<>();


    public LineAcceptanceTest() {
        LINE_5.put("name", "5호선");
        LINE_5.put("color", "#996CAC");
        LINE_5.put("upStationId", 1);
        LINE_5.put("downStationId", 2);
        LINE_5.put("distance", 48);

        LINE_2.put("name", "9호선");
        LINE_2.put("color", "#BDB092");
        LINE_2.put("upStationId", 2);
        LINE_2.put("downStationId", 4);
        LINE_2.put("distance", 37);
    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선이 생성된다
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성(LINE_5);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().get("name").equals("5호선")).isTrue();

        // then
        List<String> lineNames = 지하철_노선_목록_조회()
                .jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf("5호선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showAllLines() {
        // given
        지하철_노선_생성(LINE_5);
        지하철_노선_생성(LINE_2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회();

        // then
        assertThat(response.jsonPath().getList("name").size()).isEqualTo(2);
        assertThat(response.jsonPath().getList("name"))
                .isEqualTo(Arrays.asList(LINE_5.get("name"), LINE_2.get("name")));

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findLine() {
        // given
        int id = 지하철_노선_생성(LINE_5).jsonPath().getInt("id");
        지하철_노선_생성(LINE_2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회(id);

        // then
        assertThat(response.jsonPath().get("name").equals(LINE_5.get("name"))).isTrue();
        assertThat(response.jsonPath().get("color").equals(LINE_5.get("color"))).isTrue();
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
        int id = 지하철_노선_생성(LINE_5).jsonPath().getInt("id");
        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("name", "5호선 상행선");
        updateParams.put("color", "#996CAC");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(updateParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();
        ExtractableResponse<Response> updatedLineResponse = 지하철_노선_조회(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updatedLineResponse.jsonPath().get("name").equals(updateParams.get("name"))).isTrue();
        assertThat(updatedLineResponse.jsonPath().get("color").equals(updateParams.get("color"))).isTrue();
    }


    private ExtractableResponse<Response> 지하철_노선_조회(int id) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_생성(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
}
