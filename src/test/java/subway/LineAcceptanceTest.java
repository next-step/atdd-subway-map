package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(value = "setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철 노선을 생성한다")
    void createLine() {
        // when
        ExtractableResponse<Response> response = createRequest(
                Map.of(
                        "name", "신분당선",
                        "color", "bg-red-600",
                        "upStationId", 1L,
                        "downStationId", 2L,
                        "distance", 10
                )
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> linesResponse = getLinesRequest();
        List<String> lineNames = linesResponse.jsonPath().getList("name", String.class);
        List<String> lineColors = linesResponse.jsonPath().getList("color", String.class);
        List<Long> stationIds = linesResponse.jsonPath().getList("stations.id.flatten()", Long.class);
        Assertions.assertAll(
                () -> assertThat(lineNames).containsAnyOf("신분당선"),
                () -> assertThat(lineColors).containsAnyOf("bg-red-600"),
                () -> assertThat(stationIds).containsExactlyInAnyOrder(1L, 2L)
        );
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("지하철 노선 목록을 조회한다")
    void getLines() {
        // given
        createRequest(
                Map.of(
                        "name", "신분당선",
                        "color", "bg-red-600",
                        "upStationId", 1L,
                        "downStationId", 2L,
                        "distance", 10
                )
        );
        createRequest(
                Map.of(
                        "name", "분당선",
                        "color", "bg-green-600",
                        "upStationId", 1L,
                        "downStationId", 3L,
                        "distance", 15
                )
        );

        // when
        ExtractableResponse<Response> response = getLinesRequest();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        List<String> lineNames = response.jsonPath().getList("name", String.class);
        List<String> lineColors = response.jsonPath().getList("color", String.class);
        List<Long> stationIds = response.jsonPath().getList("stations.id.flatten()", Long.class); // flatten 시켜야 할지?
        Assertions.assertAll(
                () -> assertThat(lineNames).containsAnyOf("신분당선", "분당선"),
                () -> assertThat(lineColors).containsAnyOf("bg-red-600", "bg-green-600"),
                () -> assertThat(stationIds).containsExactlyInAnyOrder(1L, 2L, 1L, 3L)
        );
    }

    private ExtractableResponse<Response> createRequest(Map<String, Object> body) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> getLinesRequest() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }
}
