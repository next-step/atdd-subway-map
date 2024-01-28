package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("classpath:db/teardown.sql")
@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @BeforeEach
    void init() {

    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("노선을 생성한다.")
    @Test
    void apiCreateLine() {
        // when
        Map<String, String> params = createLineRequestPixture("신분당선", "bg-red-600", 1L, 2L);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        final ExtractableResponse<Response> extract = apiGetLines();
        assertThat(extract.jsonPath().getList("name")).contains("신분당선");
        assertThat(extract.jsonPath().getList("color")).contains("bg-red-600");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        Map<String, String> params1 = createLineRequestPixture("신분당선", "bg-red-600", 1L, 2L);
        Map<String, String> params2 = createLineRequestPixture("분당선", "bg-green-600", 1L, 3L);
        apiCreateLine(params1);
        apiCreateLine(params2);

        // when
        final ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        assertThat(extract.jsonPath().getList("name").size()).isEqualTo(2);
        assertThat(extract.jsonPath().getList("name")).contains("신분당선", "분당선");
        assertThat(extract.jsonPath().getList("color")).contains("bg-red-600", "bg-green-600");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("노선을 조회한다.")
    @Test
    void apiGetLine() {
        // given
        Map<String, String> params1 = createLineRequestPixture("신분당선", "bg-red-600", 1L, 2L);
        final Long id = apiCreateLine(params1).as(LineResponse.class).getId();

        // when
        final ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        final LineResponse lineResponse = extract.as(LineResponse.class);
        assertThat(lineResponse.getName()).isEqualTo("신분당선");
        assertThat(lineResponse.getColor()).isEqualTo("bg-red-600");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("노선을 수정한다.")
    @Test
    void modifyLine() {
        // given
        Map<String, String> params1 = createLineRequestPixture("신분당선", "bg-red-600", 1L, 2L);
        final Long id = apiCreateLine(params1).as(LineResponse.class).getId();
        final Map<String, String> modifyBody = modifyLineRequestPixture(id, "다른분당선", "bg-red-900");

        // when
        final ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(modifyBody)
                .when().put("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        final LineResponse modifyLineResponse = apiGetLine(id).as(LineResponse.class);
        assertThat(modifyLineResponse.getName()).isEqualTo("다른분당선");
        assertThat(modifyLineResponse.getColor()).isEqualTo("bg-red-900");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        Map<String, String> params1 = createLineRequestPixture("신분당선", "bg-red-600", 1L, 2L);
        final Long id = apiCreateLine(params1).as(LineResponse.class).getId();

        // when
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();

        // then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + id)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract();
    }

    private ExtractableResponse<Response> apiCreateLine(final Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private ExtractableResponse<Response> apiGetLines() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse<Response> apiGetLine(final Long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private Map<String, String> createLineRequestPixture(final String name, final String color, final Long upStationId, final Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        return params;
    }

    private Map<String, String> modifyLineRequestPixture(final Long id, final String name, final String color) {
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        params.put("name", name);
        params.put("color", color);
        return params;
    }
}
