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

    @DisplayName("노선 생성")
    @Test
    void createLine() {
        // Given 지하철 노선을 생성하고
        createLine("신분당선", "bg-red-600", 1, 2, 10);

        // When 지하철 노선 목록 조회하면
        List<String> lineNames = getLines();

        // Then 생성한 노선을 찾을 수 있다
        assertThat(lineNames).containsAnyOf("신분당선");
    }

    @DisplayName("노선 목록")
    @Test
    void getLines_() {
        // Given 2개의 지하철 노선을 생성하고
        createLine("성수역", "green", 1, 2, 10);
        createLine("왕십리역", "green", 1, 2, 10);

        // When 지하철 노선 목록을 조회하면
        List<String> lineNames = getLines();

        // Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
        assertThat(lineNames).containsAnyOf("성수역");
        assertThat(lineNames).containsAnyOf("왕십리역");
    }

    @DisplayName("노선 조회")
    @Test
    void getLine() {
        // Given 지하철 노선을 생성하고
        long id = createLine("사당역", "blue", 1, 2, 10);

        // When 생성한 지하철 노선을 조회하면
        Map<String, String> line = getLine(id);

        // Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
        assertThat(line).containsValue("사당역");
    }

    @DisplayName("노선 수정")
    @Test
    void updateLine() {
        // Given 지하철 노선을 생성하고
        long id = createLine("건대입구역", "green", 1, 2, 10);

        // When 생성한 지하철 노선을 수정하면
        Map<String, String> line = updateLine(id, "건대출구역", "5");

        // Then 해당 지하철 노선 정보는 수정된다
        assertThat(line).containsValue("건대출구역");
        assertThat(line).containsValue("5");
    }

    @DisplayName("노선 삭제")
    @Test
    void deleteLine() {
        // Given 지하철 노선을 생성하고
        long id = createLine("역삼역", null, 1, 2, 10);

        // When 생성한 지하철 노선을 삭제하면
        deleteLine(id);

        // Then 해당 지하철 노선 정보는 삭제된다
        List<String> lineNames = getLines();
        assertThat(lineNames).doesNotContain("역삼역");
    }

    // 목록 api
    private List<String> getLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }

    // 조회 api
    private Map<String, String> getLine(long id) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract().jsonPath().get();
    }

    // 삭제 api
    private void deleteLine(long id) {
        RestAssured.given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract();
    }

    // 등록 api
    private long createLine(String name, String color, int upStationId, int downStationId, int distance) {
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

    // 수정 api
    private Map<String, String> updateLine(long id, String name, String color) {
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
