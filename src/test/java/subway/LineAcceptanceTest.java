package subway;

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

@Sql(value = "/sql/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @BeforeEach
    void setUp() {
        createStation("강남역");
        createStation("역삼역");
        createStation("선릉역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        createLine("신분당선", "bg-red-600", 1L, 2L);

        // then
        List<String> lineNames = getLineNames();
        assertThat(lineNames).containsAnyOf("신분당선");
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
        createLine("신분당선", "bg-red-600", 1L, 2L);
        createLine("분당선", "bg-green-600", 1L, 3L);

        // when
        List<String> lineNames = getLineNames();

        // then
        assertThat(lineNames).containsAnyOf("신분당선", "분당선");
        assertThat(lineNames).hasSize(2);
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
        ExtractableResponse<Response> line = createLine("신분당선", "bg-red-600", 1L, 2L);
        String locationHeader = line.header("Location");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get(locationHeader)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
    }

    private ExtractableResponse<Response> createLine(String name, String color, Long upStationId, Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private static List<String> getLineNames() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }

    private static ExtractableResponse<Response> createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

}