package subway;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.extractableResponse.LineApiExtractableResponse.*;
import static subway.extractableResponse.StationApiExtractableResponse.createStationByStationName;

@DisplayName("지하철 노선 관련 기능")
@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선을_생성() {
        // given
        String lineName = "신분당선";
        Long upStationId = createStationByStationName("강남역").jsonPath().getLong("id");
        Long downStationId = createStationByStationName("신논현역").jsonPath().getLong("id");
        Map<String, Object> requestBody = createRequestBody(lineName, "bg-red-600", upStationId, downStationId, 10);

        // when
        createLine(requestBody);

        // then
        List<String> lineNames =
                selectLines().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf(lineName);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void 지하철_노선_목록을_조회() {
        // given
        String lineName1 = "신분당선";
        Long upStationId1 = createStationByStationName("강남역").jsonPath().getLong("id");
        Long downStationId1 = createStationByStationName("신논현역").jsonPath().getLong("id");
        Map<String, Object> requestBody = createRequestBody(lineName1, "bg-red-600", upStationId1, downStationId1, 10);

        createLine(requestBody);

        String lineName2 = "수인분당선";
        Long upStationId2 = createStationByStationName("압구정로데오역").jsonPath().getLong("id");
        Long downStationId2 = createStationByStationName("강남구청역").jsonPath().getLong("id");
        Map<String, Object> requestBody2 = createRequestBody(lineName2, "bg-yellow-600", upStationId2, downStationId2, 10);

        createLine(requestBody2);

        // when & then
        List<String> lineNames =
                selectLines().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선", "수인분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void 지하철_노선을_조회() {
        // given
        String lineName = "신분당선";
        Long upStationId = createStationByStationName("강남역").jsonPath().getLong("id");
        Long downStationId = createStationByStationName("신논현역").jsonPath().getLong("id");
        Map<String, Object> requestBody = createRequestBody(lineName, "bg-red-600", upStationId, downStationId, 10);

        Long lineId = createLine(requestBody).jsonPath().getLong("id");

        // when & then
        String responseLineName = selectLine(lineId, HttpStatus.OK).jsonPath().get("name");
        assertThat(lineName).isEqualTo(responseLineName);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void 지하철_노선을_수정() {
        // given
        String lineName = "신분당선";
        Long upStationId = createStationByStationName("강남역").jsonPath().getLong("id");
        Long downStationId = createStationByStationName("신논현역").jsonPath().getLong("id");
        Map<String, Object> requestBody = createRequestBody(lineName, "bg-red-600", upStationId, downStationId, 10);

        Long lineId = createLine(requestBody).jsonPath().getLong("id");

        // when
        Long newDownStationId = createStationByStationName("양재역").jsonPath().getLong("id");
        String newLineName = "구분당선";
        Map<String, Object> modifyRequestBody = createRequestBody(newLineName, "bg-red-600", upStationId, newDownStationId, 10);

        modifyLine(lineId, modifyRequestBody);

        // then
        JsonPath responseJsonPath = selectLine(lineId, HttpStatus.OK).jsonPath();
        List<String> stationNames = responseJsonPath.getList("stations.name");

        assertThat(newLineName).isEqualTo(responseJsonPath.get("name"));
        assertThat(stationNames).containsAnyOf("양재역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void 지하철_노선을_삭제() {
        // given
        String lineName = "신분당선";
        Long upStationId = createStationByStationName("강남역").jsonPath().getLong("id");
        Long downStationId = createStationByStationName("신논현역").jsonPath().getLong("id");
        Map<String, Object> requestBody = createRequestBody(lineName, "bg-red-600", upStationId, downStationId, 10);

        Long lineId = createLine(requestBody).jsonPath().getLong("id");

        // when
        deleteLine(lineId);

        // then
        assertThat(selectLine(lineId, HttpStatus.NOT_FOUND).statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private Map<String, Object> createRequestBody(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("color", color);
        requestBody.put("upStationId", upStationId);
        requestBody.put("downStationId", downStationId);
        requestBody.put("distance", distance);

        return requestBody;
    }

}
