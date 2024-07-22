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
import org.springframework.test.annotation.DirtiesContext;
import subway.line.LineResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private static final String BASE_URL = "/lines";
    private static final String STATION_URL = "/stations";
    private Station gangnamStation, yeoksamStation, yangjaeStation;

    @BeforeEach
    void setUp() {
        gangnamStation = createStation("강남역");
        yeoksamStation = createStation("역삼역");
        yangjaeStation = createStation("양재역");
    }

    @DisplayName("지하철 노선 생성")
    @Test
    void createLineTest() {
        // given
        String name = "신분당선";
        String color = "bg-red-600";
        int distance = 10;

        // when
        ExtractableResponse<Response> response = createLine(name, color, gangnamStation.getId(), yeoksamStation.getId(), distance);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(getResponseBody(response, "name")).isEqualTo(name);
        assertThat(getResponseBody(response, "color")).isEqualTo(color);
        assertThat(getResponseBody(response, "stations")).contains(gangnamStation.getName(), yeoksamStation.getName());
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void findAllLineTest() {
        // given
        createTestLines();

        // when
        ExtractableResponse<Response> response = findAllLines();

        // then
        List<LineResponse> lines = response.jsonPath().getList(".", LineResponse.class);
        assertThat(lines).hasSize(2);
        assertThat(lines).extracting("name").containsExactly("2호선", "신분당선");
        assertThat(lines.get(0).getStations().get(1).getId()).isEqualTo(yeoksamStation.getId());
        assertThat(lines.get(1).getStations().get(1).getId()).isEqualTo(yangjaeStation.getId());
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void findLineTest() {
        // given
        createTestLines();

        // when
        ExtractableResponse<Response> response = findLine(gangnamStation.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponseBody(response, "name")).isEqualTo("2호선");
        assertThat(getResponseBody(response, "color")).isEqualTo("bg-green-700");
        assertThat(getResponseBody(response, "stations")).contains(gangnamStation.getName(), yeoksamStation.getName());
    }

    @DisplayName("지하철 노선 수정")
    @Test
    void updateLineTest() {
        // given
        createLine("신분당선", "bg-red-600", gangnamStation.getId(), yeoksamStation.getId(), 10);

        // when
        ExtractableResponse<Response> response = updateLine(1L, "3호선", "bg-blue-600");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLineTest() {
        // given
        createLine("신분당선", "bg-red-600", gangnamStation.getId(), yeoksamStation.getId(), 10);

        // when
        ExtractableResponse<Response> deleteResponse = deleteLine(1L);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        List<LineResponse> lines = findAllLines().jsonPath().getList(".", LineResponse.class);
        assertThat(lines).isEmpty();
    }

    private void createTestLines() {
        createLine("2호선", "bg-green-700", gangnamStation.getId(), yeoksamStation.getId(), 20);
        createLine("신분당선", "bg-red-600", gangnamStation.getId(), yangjaeStation.getId(), 10);
    }

    private ExtractableResponse<Response> createLine(String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(BASE_URL)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> findAllLines() {
        return RestAssured.given().log().all()
                .when().get(BASE_URL)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> findLine(Long id) {
        return RestAssured.given().log().all()
                .when().get(BASE_URL + "/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> updateLine(Long id, String name, String color) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(BASE_URL + "/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteLine(Long id) {
        return RestAssured.given().log().all()
                .when().delete(BASE_URL + "/" + id)
                .then().log().all()
                .extract();
    }

    private String getResponseBody(ExtractableResponse<Response> response, String key) {
        return response.body().jsonPath().getString(key);
    }

    private Station createStation(String name) {
        ExtractableResponse<Response> response = createStationAPI(name);
        Long id = response.body().jsonPath().getLong("id");
        String stationName = response.body().jsonPath().getString("name");
        return new Station(id, stationName);
    }

    private ExtractableResponse<Response> createStationAPI(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(STATION_URL)
                .then().log().all()
                .extract();
    }
}