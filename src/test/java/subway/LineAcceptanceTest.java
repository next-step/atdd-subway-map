package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.line.dto.AddLineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.ModifyLineRequest;
import subway.line.dto.ModifyLineResponse;
import subway.station.dto.StationRequest;
import subway.station.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.RestAssuredWrapper.*;

class LineAcceptanceTest extends AcceptanceTestBase {
    private static final String TEST_COLOR = "bg-test-600";
    private static final String 신분당선 = "신분당선";
    private static final String 인천지하철_1호선 = "인천지하철 1호선";
    private static Long 신분당선_상행종점역_ID;
    private static Long 신분당선_하행종점역_ID;
    private static Long 인천지하철_1호선_상행종점역_ID;
    private static Long 인천지하철_1호선_하행종점역_ID;

    @BeforeEach
    void init() {
        // 신분당선 역 생성
        신분당선_상행종점역_ID = createStation("신사");
        createStation("논현");
        createStation("신논현");
        createStation("강남");
        신분당선_하행종점역_ID = createStation("광교");

        // 인천지하철 1호선 역 생성
        인천지하철_1호선_상행종점역_ID = createStation("계양");
        createStation("귤현");
        createStation("박촌");
        createStation("임학");
        인천지하철_1호선_하행종점역_ID = createStation("송도 달빛축제공원");
    }

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // When: 지하철 노선을 생성하면
        ExtractableResponse<Response> postResponse = createLine(신분당선, 신분당선_상행종점역_ID, 신분당선_하행종점역_ID);
        assertThat(postResponse.statusCode()).isEqualTo(HttpStatus.SC_CREATED);

        // Then: 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
        ExtractableResponse<Response> getResponse = get("/lines");
        assertThat(getLineNames(getResponse)).containsAnyOf(신분당선);
        assertThat(getStationIds(getResponse)).containsExactly(신분당선_상행종점역_ID, 신분당선_하행종점역_ID);
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // Given: 2개의 지하철 노선을 생성하고
        createLine(신분당선, 신분당선_상행종점역_ID, 신분당선_하행종점역_ID);
        createLine(인천지하철_1호선, 인천지하철_1호선_상행종점역_ID, 인천지하철_1호선_하행종점역_ID);

        // When: 지하철 노선 목록을 조회하면
        ExtractableResponse<Response> getResponse = get("/lines");

        // Then: 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
        assertThat(getLineNames(getResponse)).containsExactly(신분당선, 인천지하철_1호선);
        assertThat(getStationIds(getResponse)).containsExactly(신분당선_상행종점역_ID, 신분당선_하행종점역_ID, 인천지하철_1호선_상행종점역_ID, 인천지하철_1호선_하행종점역_ID);
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // Given: 지하철 노선을 생성하고
        ExtractableResponse<Response> postResponse = createLine(신분당선, 신분당선_상행종점역_ID, 신분당선_하행종점역_ID);

        // When: 생성한 지하철 노선을 조회하면
        Long id = postResponse.as(LineResponse.class).getId();
        ExtractableResponse<Response> getResponse = get(String.format("lines/%s", id));

        // Then: 생성한 지하철 노선의 정보를 응답받을 수 있다
        String lineName = getResponse.jsonPath().get("name");
        assertThat(lineName).isEqualTo(신분당선);
    }

    @DisplayName("지하철 노선 수정")
    @Test
    void modifyLine() {
        // Given: 지하철 노선을 생성하고
        ExtractableResponse<Response> postResponse = createLine(신분당선, 신분당선_상행종점역_ID, 신분당선_하행종점역_ID);

        // When: 생성한 지하철 노선을 수정하면
        Long id = postResponse.as(LineResponse.class).getId();
        ModifyLineRequest modifyLineRequest = new ModifyLineRequest(인천지하철_1호선, TEST_COLOR);
        ExtractableResponse<Response> putResponse = put(String.format("lines/%s", id), modifyLineRequest);

        // Then: 해당 지하철 노선 정보는 수정된다
        String lineName = putResponse.as(ModifyLineResponse.class).getName();
        assertThat(lineName).isEqualTo(인천지하철_1호선);
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // Given: 지하철 노선을 생성하고
        ExtractableResponse<Response> postResponse = createLine(신분당선, 신분당선_상행종점역_ID, 신분당선_하행종점역_ID);

        // When: 생성한 지하철 노선을 삭제하면
        Long id = postResponse.as(LineResponse.class).getId();
        ExtractableResponse<Response> deleteResponse = delete(String.format("lines/%s", id));
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);

        // Then: 해당 지하철 노선 정보는 삭제된다
        ExtractableResponse<Response> getResponse = get("/lines");
        List<String> lineNames = getLineNames(getResponse);
        assertThat(lineNames).doesNotContain(신분당선);
    }

    private Long createStation(String stationName) {
        return post("/stations", StationRequest.from(stationName)).as(StationResponse.class).id();
    }

    private ExtractableResponse<Response> createLine(String lineName, Long upStationId, Long downStationId) {
        AddLineRequest line = AddLineRequest.builder()
                .name(lineName)
                .color(TEST_COLOR)
                .distance(10)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .build();
        return post("/lines", line);
    }

    private static List<String> getLineNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name", String.class);
    }

    private static List<Long> getStationIds(ExtractableResponse<Response> getResponse) {
        return getResponse.jsonPath().getList("stations.id.flatten()", Long.class);
    }
}
