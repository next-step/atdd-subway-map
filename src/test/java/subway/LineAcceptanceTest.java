package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void lineCreate() {
        // when
        Map<String, String> line = new HashMap<>();
        line.put("name", "2호선");
        line.put("color", "bg-green-600");
        line.put("upStationId", "1");
        line.put("downStationId", "2");
        line.put("distance", "10");

        LineApi.createLine(line);

        // then
        ExtractableResponse<Response> retrieveLineResponse = LineApi.retrieveLines();
        assertThat(retrieveLineResponse.jsonPath().getList("name")).containsAnyOf(line.get("name"));

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void retrieveLines() {
        // given
        Map<String, String> firstBlueLine = new HashMap<>();
        firstBlueLine.put("name", "1호선");
        firstBlueLine.put("color", "bg-blue-600");
        firstBlueLine.put("upStationId", "1");
        firstBlueLine.put("downStationId", "2");
        firstBlueLine.put("distance", "10");
        LineApi.createLine(firstBlueLine);

        Map<String, String> secondGreenLine = new HashMap<>();
        secondGreenLine.put("name", "2호선");
        secondGreenLine.put("color", "bg-green-600");
        secondGreenLine.put("upStationId", "1");
        secondGreenLine.put("downStationId", "2");
        secondGreenLine.put("distance", "10");
        LineApi.createLine(secondGreenLine);


        // when
        ExtractableResponse<Response> retrieveLineResponse = LineApi.retrieveLines();
        List<Station> stations = retrieveLineResponse.jsonPath().getList("stations", Station.class);

        // then
        stations.forEach(station -> assertThat(station.getName()).containsAnyOf("1호선", "2호선"));

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 하나를 조회한다.")
    @Test
    void createLineAndRetrieve() {
        // given
        Map<String, String> firstBlueLine = new HashMap<>();
        firstBlueLine.put("name", "1호선");
        firstBlueLine.put("color", "bg-blue-600");
        firstBlueLine.put("upStationId", "1");
        firstBlueLine.put("downStationId", "2");
        firstBlueLine.put("distance", "10");
        ExtractableResponse<Response> createResponse = LineApi.createLine(firstBlueLine);
        final String location = createResponse.header("Location");
        final Integer createdId = createResponse.body().jsonPath().get("id");

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
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void modifyLine() {
        // given
        Map<String, String> firstBlueLine = new HashMap<>();
        firstBlueLine.put("name", "1호선");
        firstBlueLine.put("color", "bg-blue-600");
        firstBlueLine.put("upStationId", "1");
        firstBlueLine.put("downStationId", "2");
        firstBlueLine.put("distance", "10");
        ExtractableResponse<Response> createResponse = LineApi.createLine(firstBlueLine);
        final String createdLocation = createResponse.header("Location");

        // when
        Map<String, String> firstBlueLineModify = new HashMap<>();
        firstBlueLineModify.put("name", "1호선천안");
        firstBlueLineModify.put("color", "bg-blue-800");

        ExtractableResponse<Response> modifyLineResponse = LineApi.modifyLineByLocation(createdLocation, firstBlueLineModify);

        // then
        assertThat(modifyLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
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
        Map<String, String> firstBlueLine = new HashMap<>();
        firstBlueLine.put("name", "1호선");
        firstBlueLine.put("color", "bg-blue-600");
        firstBlueLine.put("upStationId", "1");
        firstBlueLine.put("downStationId", "2");
        firstBlueLine.put("distance", "10");
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

}
