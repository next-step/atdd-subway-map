package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        String stationName = "강남역";
        ExtractableResponse<Response> response = createStationWithName(stationName);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = getAllStations().jsonPath()
                .getList("name", String.class);
        assertThat(stationNames).containsAnyOf(stationName);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // Given
        List<String> stationNames = List.of("강남역", "서울대입구역");
        List<Long> createdStationIds = stationNames.stream()
                .map(name -> extractIdInResponse(createStationWithName(name)))
                .collect(Collectors.toList());

        // When
        ExtractableResponse<Response> getAllStationsResponse = getAllStations();

        // Then
        List<Long> getAllStationsIds = extractIdsInListTypeResponse(getAllStationsResponse);
        assertThat(getAllStationsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getAllStationsIds.size()).isEqualTo(createdStationIds.size());
    }

    private ExtractableResponse<Response> getAllStations() {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .get("/stations")
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> createStationWithName(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given()
                .log()
                .all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .log()
                .all()
                .extract();
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // Given
        String stationName = "강남역";
        Long createdStationId = extractIdInResponse(createStationWithName(stationName));

        // When
        ExtractableResponse<Response> deleteResponse = deleteStationWithId(createdStationId);

        // Then
        List<Long> ids = extractIdsInListTypeResponse(getStationWithId(createdStationId));
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(ids).doesNotContain(createdStationId);
    }

    private ExtractableResponse<Response> deleteStationWithId(Long createdStationId) {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/stations/{id}", createdStationId)
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> getStationWithId(Long id) {
        return RestAssured.given()
                .log()
                .all()
                .param("id", id)
                .when()
                .get("/stations")
                .then()
                .log()
                .all()
                .extract();
    }

    private List<Long> extractIdsInListTypeResponse(ExtractableResponse response) {
        return response.jsonPath()
                .getList("id", Long.class);
    }

    private Long extractIdInResponse(ExtractableResponse response) {
        return response.jsonPath()
                .getLong("id");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createStationLine() {
        // When
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        ExtractableResponse<Response> response = createStationLine(stationLineName, stationLineColor, upStationId, downStationId);
        Long createdStationLineId = extractIdInResponse(response);

        // Then
        List<Long> ids = extractIdsInListTypeResponse(getAllStationLines());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(ids).containsAnyOf(createdStationLineId);
    }

    private ExtractableResponse<Response> createStationLine(String stationLineName, String stationLineColor, Long upStationId, Long downStationId
    ) {
        Map<Object, Object> params = new HashMap<>();
        params.put("name", stationLineName);
        params.put("color", stationLineColor);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);

        return RestAssured.given()
                .log()
                .all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stationLines")
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> getAllStationLines() {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .get("/stationLines")
                .then()
                .log()
                .all()
                .extract();
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선의 목록을 조회한다.")
    @Test
    void getStationLines() {
        // Given
        String stationLineName1 = "신분당선";
        String stationLineColor1 = "bg-red-600";
        String stationLineName2 = "신분당선2";
        String stationLineColor2 = "bg-red-6002";

        String upStationName1 = "지하철역";
        String downStationName1 = "새로운지하철역";
        String upStationName2 = "지하철역2";
        String downStationName2 = "새로운지하철역2";

        Long upStationId1 = extractIdInResponse(createStationWithName(upStationName1));
        Long downStationId1 = extractIdInResponse(createStationWithName(downStationName1));
        Long upStationId2 = extractIdInResponse(createStationWithName(upStationName2));
        Long downStationId2 = extractIdInResponse(createStationWithName(downStationName2));

        Long createdStationLineId1 = extractIdInResponse(createStationLine(stationLineName1, stationLineColor1, upStationId1, downStationId1));
        Long createdStationLineId2 = extractIdInResponse(createStationLine(stationLineName2, stationLineColor2, upStationId2, downStationId2));

        // When
        ExtractableResponse<Response> allStationLines = getAllStationLines();

        // Then
        List<Long> ids = extractIdsInListTypeResponse(allStationLines);
        assertThat(allStationLines.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(ids).hasSize(2);
        assertThat(ids).contains(createdStationLineId1, createdStationLineId2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getStationLine(){
        // Given
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        Long createdStationLineId = extractIdInResponse(createStationLine(stationLineName, stationLineColor, upStationId, downStationId));

        // When
        ExtractableResponse<Response> response = getStationLineWithId(createdStationLineId);

        // Then
        Long responseId = extractIdInResponse(response);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseId).isEqualTo(createdStationLineId);
    }

    private ExtractableResponse<Response> getStationLineWithId(Long id) {
        return RestAssured.given()
                .log()
                .all()
                .param("id", id)
                .when()
                .get("/stationLines")
                .then()
                .log()
                .all()
                .extract();
    }
}