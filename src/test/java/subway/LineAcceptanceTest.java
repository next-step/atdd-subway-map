package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        String upStationId = createStation("강남역");
        String downStationId = createStation("양재역");

        // when
        String lineName = "신분당선";
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", "bg-red-600");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", "10");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getString("name"))
                .contains(lineName);
    }

    private String createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        return response.body().jsonPath().getString("id");
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void readLines() {
        // given
        String station1Id = createStation("강남역");
        String station2Id = createStation("신논현역");
        String station3Id = createStation("논현역");

        createLine("신분당선", station1Id, station2Id);
        createLine("신분당선", station2Id, station3Id);

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when().get("/lines")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.body().jsonPath().getList("id")).hasSize(2);
    }

    private String createLine(String lineName, String upStationId, String downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", "bg-red-600");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", "10");

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract().jsonPath().getString("id");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답 받을 수 있다.
     */
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void readLine() {
        // given
        String station1Id = createStation("강남역");
        String station2Id = createStation("신논현역");

        String lineId = createLine("신분당선", station1Id, station2Id);

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when().get("/lines/"+lineId)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.body().jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(response.body().jsonPath().getList("stations.id", String.class)).containsExactlyInAnyOrder(station1Id, station2Id);
    }
}
