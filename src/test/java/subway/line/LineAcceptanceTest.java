package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.station.StationApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    public List<Long> stationIds = new ArrayList<>();

    @BeforeEach
    void stationAdd() {
        List.of("강남역", "역삼역", "잠실역").forEach(StationApi::createStationByName);
        ExtractableResponse<Response> response = StationApi.retrieveStations();
        stationIds = response.body().jsonPath().getList("id", Long.class);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성 한다.")
    @Test
    void lineCreate() {
        // when
        Map<String, String> secondGreenLine = generateLineCreateRequest("2호선", "bg-green-600", stationIds.get(0), stationIds.get(2), 10L);
        LineApi.createLine(secondGreenLine);

        // then
        ExtractableResponse<Response> retrieveLineResponse = LineApi.retrieveLines();
        assertThat(retrieveLineResponse.jsonPath().getList("name", String.class)).containsAnyOf(secondGreenLine.get("name"));

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회 한다.")
    @Test
    void retrieveLines() {
        // given
        Map<String, String> firstBlueLine = generateLineCreateRequest("1호선", "bg-blue-600", stationIds.get(0), stationIds.get(1), 10L);
        LineApi.createLine(firstBlueLine);

        Map<String, String> secondGreenLine = generateLineCreateRequest("2호선", "bg-green-600", stationIds.get(0), stationIds.get(2), 10L);
        LineApi.createLine(secondGreenLine);

        // when
        ExtractableResponse<Response> retrieveLineResponse = LineApi.retrieveLines();
        List<String> stations = retrieveLineResponse.jsonPath().getList("name", String.class);

        // then
        stations.forEach(station -> assertThat(station).containsAnyOf("1호선", "2호선"));

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 하나를 조회 한다.")
    @Test
    void createLineAndRetrieve() {
        // given
        Map<String, String> firstBlueLine = generateLineCreateRequest("1호선", "bg-blue-600", stationIds.get(0), stationIds.get(1), 10L);
        ExtractableResponse<Response> createResponse = LineApi.createLine(firstBlueLine);
        final String location = createResponse.header("Location");

        // when
        ExtractableResponse<Response> retrieveLineResponse = LineApi.retrieveLineByLocation(location);
        String lineName = retrieveLineResponse.jsonPath().get("name");

        // then
        assertThat(lineName).isEqualTo(firstBlueLine.get("name"));

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정 한다.")
    @Test
    void modifyLine() {
        // given
        Map<String, String> firstBlueLine = generateLineCreateRequest("1호선", "bg-blue-600", stationIds.get(0), stationIds.get(1), 10L);
        ExtractableResponse<Response> createResponse = LineApi.createLine(firstBlueLine);
        final String createdLocation = createResponse.header("Location");

        // when
        Map<String, String> firstBlueLineModify = generateLineModifyRequest("1호선천안", "bg-blue-800");
        ExtractableResponse<Response> modifyLineResponse = LineApi.modifyLineByLocation(createdLocation, firstBlueLineModify);

        // then
        assertThat(modifyLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제 한다.")
    @Test
    void deleteLine() {
        // given
        Map<String, String> firstBlueLine = generateLineCreateRequest("1호선", "bg-blue-600", stationIds.get(0), stationIds.get(1), 10L);
        ExtractableResponse<Response> createResponse = LineApi.createLine(firstBlueLine);
        final String createdLocation = createResponse.header("Location");
        final Integer createdId = createResponse.body().jsonPath().get("id");

        // when
        ExtractableResponse<Response> deletedStation = StationApi.deleteStationByLocation(createdLocation);
        assertThat(deletedStation.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> retrieveLinesResponse = LineApi.retrieveLines();
        assertThat(retrieveLinesResponse.body().jsonPath().getList("id")).doesNotContain(createdId);
    }

    private Map<String, String> generateLineCreateRequest(String name,
                                                          String color,
                                                          Long upStationId,
                                                          Long downStationId,
                                                          Long distance) {
        Map<String, String> lineRequest = new HashMap<>();
        lineRequest.put("name", name);
        lineRequest.put("color", color);
        lineRequest.put("upStationId", String.valueOf(upStationId));
        lineRequest.put("downStationId", String.valueOf(downStationId));
        lineRequest.put("distance", String.valueOf(distance));
        return lineRequest;
    }

    private Map<String, String> generateLineModifyRequest(String name,
                                                          String color) {
        Map<String, String> lineRequest = new HashMap<>();
        lineRequest.put("name", name);
        lineRequest.put("color", color);
        return lineRequest;
    }

}
