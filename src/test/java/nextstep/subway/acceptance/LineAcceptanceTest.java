package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("지하철노선 생성")
    @Test
    void createLine() {
        // Given 지하철 노선을 생성하고
        createStation("신분당선", "bg-red-600", 1, 2, 10);

      /*  // When 지하철 노선 목록 조회하면
        List<String> stationNames = getStations();

        // Then 생성한 노선을 찾을 수 있다
        assertThat(stationNames).containsAnyOf("강남역");*/
    }

    @DisplayName("지하철노선 목록 조회")
    @Test
    void getLines() {
        // Given 2개의 지하철 노선을 생성하고
        createStation("성수역", "green", 1, 2, 10);
        createStation("왕십리역", "green", 1, 2, 10);

        // When 지하철 노선 목록을 조회하면
        List<String> stationNames = getStations();

        // Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
        assertThat(stationNames).containsAnyOf("성수역");
        assertThat(stationNames).containsAnyOf("왕십리역");
    }

    @DisplayName("지하철노선 단일 조회")
    @Test
    void getLine() {
        // Given 지하철 노선을 생성하고
        long id = createStation("강남역", null, 1, 2, 10);

        // When 생성한 지하철 노선을 조회하면
        Map<String, String> station = getStation(id);

        // Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
        assertThat(station).containsValue("강남역");
    }

    @DisplayName("지하철노선 수정")
    @Test
    void updateLine() {
        // Given 지하철 노선을 생성하고
        long id = createStation("건대입구역", "green", 1, 2, 10);

        // When 생성한 지하철 노선을 수정하면
        Map<String, String> station = updateStation(id, "건대역", "red");

        // Then 해당 지하철 노선 정보는 수정된다
        assertThat(station).containsValue("건대역");
        assertThat(station).containsValue("red");
    }

    @DisplayName("지하철노선 삭제")
    @Test
    void deleteLine() {
        // Given 지하철 노선을 생성하고
        long id = createStation("역삼역", null, 1, 2, 10);

        // When 생성한 지하철 노선을 삭제하면
        deleteStation(id);

        // Then 해당 지하철 노선 정보는 삭제된다
        List<String> stationNames = getStations();
        assertThat(stationNames).doesNotContain("역삼역");
    }

    private List<String> getStations() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }

    private Map<String, String> getStation(long id) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract().jsonPath().get();
    }

    private void deleteStation(long id) {
        RestAssured.given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private long createStation(String name, String color, int upStationId, int downStationId, int distance) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        param.put("upStationId", upStationId);
        param.put("downStationId", downStationId);
        param.put("distance", distance);
        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract().jsonPath().getLong("id");

    }

    private Map<String, String> updateStation(long id, String name, String color) {
        Map<String, String> param = new HashMap<>();
        param.put("id", String.valueOf(id));
        param.put("name", name);
        param.put("color", color);
        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines")
                .then().log().all()
                .extract().jsonPath().get();
    }
}
