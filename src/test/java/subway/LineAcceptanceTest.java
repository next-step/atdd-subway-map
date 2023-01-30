package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(value = "setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LineAcceptanceTest extends AcceptanceTestBase {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철 노선을 생성한다")
    void createLine() {
        // when
        ExtractableResponse<Response> response = createRequest(createBody("신분당선"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> linesResponse = getAllLinesRequest();
        Assertions.assertAll(
            withNames(linesResponse, "신분당선"),
            withColors(linesResponse, "bg-red-600"),
            withStationIds(linesResponse, 1L, 2L)
        );
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("지하철 노선 목록을 조회한다")
    void getAllLines() {
        // given
        createRequest(createBody("신분당선"));
        createRequest(createBody("분당선"));

        // when
        ExtractableResponse<Response> response = getAllLinesRequest();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        Assertions.assertAll(
            withNames(response, "신분당선", "분당선"),
            withColors(response, "bg-red-600", "bg-green-600"),
            withStationIds(response, 1L, 2L, 1L, 3L)
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("지하철 노선을 조회한다")
    void getLine() {
        // given
        Long id = createRequest(createBody("신분당선")).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = getLineRequest(id);

        // then
        Assertions.assertAll(
            withName(response, "신분당선"),
            withColor(response, "bg-red-600"),
            withStationIds(response, 1L, 2L)
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    @DisplayName("지하철 노선을 수정한다")
    void updateLine() {
        //given
        Long id = createRequest(createBody("분당선")).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(Map.of(
                    "name", "다른분당선",
                    "color", "bg-red-600"
            ))
            .pathParam("id", id)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/lines/{id}")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        ExtractableResponse<Response> lineResponse = getLineRequest(id);
        Assertions.assertAll(
            withName(lineResponse, "다른분당선"),
            withColor(lineResponse,"bg-red-600")
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    @DisplayName("지하철 노선을 삭제한다")
    void deleteLine() {
        // given
        Long id = createRequest(createBody("신분당선")).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .pathParam("id", id)
            .when().delete("/lines/{id}")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> createRequest(Map<String, Object> body) {
        return RestAssured.given().log().all()
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> getAllLinesRequest() {
        return RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> getLineRequest(Long id) {
        return RestAssured.given().log().all().pathParam("id", id)
            .when().get("/lines/{id}")
            .then().log().all()
            .extract();
    }

    private Map<String, Object> createBody(String name) {
        Map<String, Map<String, Object>> lines = Map.of(
            "신분당선", Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1L,
                "downStationId", 2L,
                "distance", 10
            ),
            "분당선", Map.of(
                "name", "분당선",
                "color", "bg-green-600",
                "upStationId", 1L,
                "downStationId", 3L,
                "distance", 15
            )
        );
        return lines.getOrDefault(name, lines.get("신분당선"));
    }

    private Executable withNames(ExtractableResponse<Response> response, String...names) {
        return () -> assertThat(response.jsonPath().getList("name", String.class)).containsAnyOf(names);
    }

    private Executable withColors(ExtractableResponse<Response> response, String...colors) {
        return () -> assertThat(response.jsonPath().getList("color", String.class)).containsAnyOf(colors);
    }

    private Executable withStationIds(ExtractableResponse<Response> response, Long...ids) {
        return () -> assertThat(response.jsonPath().getList("stations.id.flatten()", Long.class)).containsExactlyInAnyOrder(ids);
    }

    private Executable withName(ExtractableResponse<Response> response, String name) {
        return () -> assertThat((String) response.jsonPath().get("name")).isEqualTo(name);
    }

    private Executable withColor(ExtractableResponse<Response> response, String color) {
        return () -> assertThat((String) response.jsonPath().get("color")).isEqualTo(color);
    }
}
