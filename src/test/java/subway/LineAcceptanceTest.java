package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.StationAcceptanceTest.createStation;

@AcceptanceTest
@DisplayName("노선 관련 기능")
public class LineAcceptanceTest {

    private Long station1Id;
    private Long station2Id;

    @BeforeEach
    void setUp() {
        station1Id = createStation("station1").jsonPath()
                .getLong("id");
        station2Id = createStation("station2").jsonPath()
                .getLong("id");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("노선을 생성한다.")
    @Test
    void test_createStation() {
        // when
        String newlineName = "newLine";
        createLine(newlineName, station1Id, station2Id)
                .jsonPath().getLong("id");

        // then
        List<String> responseNewLineNames = getLines().jsonPath()
                .getList("name", String.class);
        assertThat(responseNewLineNames).contains(newlineName);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void test_showLines() {
        //given
        String newlineName1 = "newLine1";
        createLine(newlineName1, station1Id, station2Id)
                .jsonPath().getLong("id");
        String newlineName2 = "newLine2";
        createLine(newlineName2, station1Id, station2Id)
                .jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        List<String> names = response.jsonPath()
                .getList("name", String.class);
        assertThat(names).containsExactly(newlineName1, newlineName2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void test_getLine() {
        //given
        String lineName = "newLine1";
        long lineId = createLine(lineName, station1Id, station2Id)
                .jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = getLine(lineId);

        // then
        String responseName = response.jsonPath()
                .getString("name");
        assertThat(responseName).isEqualTo(lineName);
    }

    public static ExtractableResponse<Response> createLine(String name, Long upStationId, Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        ExtractableResponse<Response> response =
                RestAssured.given()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();
        return response;
    }

    public static ExtractableResponse<Response> getLine(Long id) {
        return RestAssured.given()
                .when().get("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getLines() {
        return RestAssured.given()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }
}
