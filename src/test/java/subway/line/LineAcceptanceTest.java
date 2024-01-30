package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.StationRequest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        ExtractableResponse<Response> 강남역 = createStation("강남역");
        ExtractableResponse<Response> 건대입구역 = createStation("건대입구역");
        String 강남역_ID = 강남역.jsonPath().getString("id");
        String 건대입구역_ID = 건대입구역.jsonPath().getString("id");

        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-green-999");
        params.put("upStationId", 강남역_ID);
        params.put("downStationId", 건대입구역_ID);
        params.put("distance", "10");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo("2호선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-green-999");
        assertThat(response.jsonPath().getList("stations")).hasSize(2);
        assertThat(response.jsonPath().getList("stations.id", String.class)).containsExactly(강남역_ID, 건대입구역_ID);
        assertThat(response.jsonPath().getList("stations.name", String.class)).containsExactly("강남역", "건대입구역");
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
        ExtractableResponse<Response> 강남역 = createStation("강남역");
        ExtractableResponse<Response> 건대입구역 = createStation("건대입구역");
        ExtractableResponse<Response> 군자역 = createStation("군자역");
        String 강남역_ID = 강남역.jsonPath().getString("id");
        String 건대입구역_ID = 건대입구역.jsonPath().getString("id");
        String 군자역_ID = 군자역.jsonPath().getString("id");

        ExtractableResponse<Response> 이호선 = createLine("2호선", "bg-green-999", 강남역_ID, 건대입구역_ID, "10");
        ExtractableResponse<Response> 칠호선 = createLine("7호선", "bg-orange-600", 건대입구역_ID, 군자역_ID, "20");
        String 이호선_ID = 이호선.jsonPath().getString("id");
        String 칠호선_ID = 칠호선.jsonPath().getString("id");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/lines")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".")).hasSize(2);
        assertThat(response.jsonPath().getList("id", String.class)).containsExactly(이호선_ID, 칠호선_ID);
        assertThat(response.jsonPath().getList("name", String.class)).containsExactly("2호선", "7호선");
        assertThat(response.jsonPath().getList("color", String.class)).containsExactly("bg-green-999", "bg-orange-600");
        assertThat(response.jsonPath().getList("stations", String.class)).hasSize(2);
        assertThat(response.jsonPath().getList("stations[0].id", String.class)).containsExactly(강남역_ID, 건대입구역_ID);
        assertThat(response.jsonPath().getList("stations[0].name", String.class)).containsExactly("강남역", "건대입구역");
        assertThat(response.jsonPath().getList("stations[1].id", String.class)).containsExactly(건대입구역_ID, 군자역_ID);
        assertThat(response.jsonPath().getList("stations[1].name", String.class)).containsExactly("건대입구역", "군자역");
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
        ExtractableResponse<Response> 강남역 = createStation("강남역");
        ExtractableResponse<Response> 건대입구역 = createStation("건대입구역");
        String 강남역_ID = 강남역.jsonPath().getString("id");
        String 건대입구역_ID = 건대입구역.jsonPath().getString("id");

        ExtractableResponse<Response> 이호선 = createLine("2호선", "bg-green-999", 강남역_ID, 건대입구역_ID, "10");
        String 이호선_ID = 이호선.jsonPath().getString("id");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/lines/{lineId}", 이호선_ID)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("id")).isEqualTo(이호선_ID);
        assertThat(response.jsonPath().getString("name")).isEqualTo("2호선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-green-999");
        assertThat(response.jsonPath().getList("stations")).hasSize(2);
        assertThat(response.jsonPath().getList("stations.id", String.class)).containsExactly(강남역_ID, 건대입구역_ID);
        assertThat(response.jsonPath().getList("stations.name", String.class)).containsExactly("강남역", "건대입구역");
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
        ExtractableResponse<Response> 강남역 = createStation("강남역");
        ExtractableResponse<Response> 건대입구역 = createStation("건대입구역");
        String 강남역_ID = 강남역.jsonPath().getString("id");
        String 건대입구역_ID = 건대입구역.jsonPath().getString("id");

        ExtractableResponse<Response> 이호선 = createLine("2호선", "bg-green-999", 강남역_ID, 건대입구역_ID, "10");
        String 이호선_ID = 이호선.jsonPath().getString("id");

        Map<String, String> params = new HashMap<>();
        params.put("name", "3호선");
        params.put("color", "bg-blue-222");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().put("/lines/{lineId}", 이호선_ID)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> loadLine = loadLine(Long.valueOf(이호선_ID));
        assertThat(loadLine.jsonPath().getString("id")).isEqualTo(이호선_ID);
        assertThat(loadLine.jsonPath().getString("name")).isEqualTo("3호선");
        assertThat(loadLine.jsonPath().getString("color")).isEqualTo("bg-blue-222");
        assertThat(loadLine.jsonPath().getList("stations")).hasSize(2);
        assertThat(loadLine.jsonPath().getList("stations.id", String.class)).containsExactly(강남역_ID, 건대입구역_ID);
        assertThat(loadLine.jsonPath().getList("stations.name", String.class)).containsExactly("강남역", "건대입구역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> 강남역 = createStation("강남역");
        ExtractableResponse<Response> 건대입구역 = createStation("건대입구역");
        String 강남역_ID = 강남역.jsonPath().getString("id");
        String 건대입구역_ID = 건대입구역.jsonPath().getString("id");

        ExtractableResponse<Response> 이호선 = createLine("2호선", "bg-green-999", 강남역_ID, 건대입구역_ID, "10");
        String 이호선_ID = 이호선.jsonPath().getString("id");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().delete("/lines/{lineId}", 이호선_ID)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> loadLine = loadLine(Long.valueOf(이호선_ID));
        assertThat(loadLine.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    private ExtractableResponse<Response> createStation(String stationName) {
        StationRequest request = new StationRequest(stationName);
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createLine(String lineName, String lineColor, String upStationId, String downStationId, String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", lineColor);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> loadLine(Long lineId) {
        return RestAssured.given().log().all()
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
    }
}
