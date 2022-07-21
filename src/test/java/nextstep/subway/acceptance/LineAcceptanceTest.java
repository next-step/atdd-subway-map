package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.LineClient;
import nextstep.subway.StationClient;
import nextstep.subway.config.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleaner dataBaseCleaner;

    StationClient stationClient;

    LineClient lineClient;


    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        dataBaseCleaner.afterPropertiesSet();
        dataBaseCleaner.clear();

        stationClient = new StationClient();
        stationClient.create("지하철역", "새로운지하철역", "또다른지하철역");

        lineClient = new LineClient();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        // when
        Map<String, Object> params = params("신분당선", "bg-red-600", 1L, 2L, 10L);

        ExtractableResponse<Response> response = lineClient.create(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames = lineClient.findAll().jsonPath().getList("name", String.class);

        assertThat(lineNames).containsAnyOf("신분당선");

    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        Map<String, Object> params1 = params("신분당선", "bg-red-600", 1L, 2L, 10L);
        Map<String, Object> params2 = params("분당선", "bg-yellow-600", 1L, 3L, 20L);

        lineClient.create(params1);
        lineClient.create(params2);

        // when
        List<String> lineNames = lineClient.findAll().jsonPath().getList("name", String.class);

        // then
        assertThat(lineNames).containsExactly("신분당선", "분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void getLine() {
        // given
        Map<String, Object> params = params("신분당선", "bg-red-600", 1L, 2L, 10L);

        lineClient.create(params);

        // when
        ExtractableResponse<Response> response = lineClient.findById(1L);

        // then
        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void putLine() {
        // given
        Map<String, Object> params = params("신분당선", "bg-red-600", 1L, 2L, 10L);

        lineClient.create(params);

        // when
        lineClient.putById(1L, Map.of("name", "5호선", "color", "bg-purple-600"));

        // then
        assertAll(
                () -> assertThat(lineClient.findById(1L).jsonPath().getString("name")).isEqualTo("5호선"),
                () -> assertThat(lineClient.findById(1L).jsonPath().getString("color")).isEqualTo("bg-purple-600")
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        Map<String, Object> params = params("신분당선", "bg-red-600", 1L, 2L, 10L);

        lineClient.create(params);

        // when
        lineClient.deleteById(1L);

        // then
        assertAll(
                () -> assertThat(lineClient.findById(1L).jsonPath().getString("name")).isNullOrEmpty(),
                () -> assertThat(lineClient.findById(1L).jsonPath().getString("color")).isNullOrEmpty()
        );
    }

    private Map<String, Object> params(String name, String color, Long upStationId, Long downStationId, Long distance) {
        return Map.of(
                "name", name,
                "color", color,
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", distance
        );
    }

}
