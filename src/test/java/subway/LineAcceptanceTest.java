package subway;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.line.LineRequest;
import subway.station.StationRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.extractableResponse.LineApiExtractableResponse.*;
import static subway.extractableResponse.StationApiExtractableResponse.createStation;

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
        String 신분당선 = "신분당선";
        Long upStationId = createStation(StationRequest.from("강남역")).jsonPath().getLong("id");
        Long downStationId = createStation(StationRequest.from("신논현역")).jsonPath().getLong("id");
        LineRequest lineRequest = LineRequest.of(신분당선, "bg-red-600", upStationId, downStationId, 10);

        // when
        assertThat(createLine(lineRequest).statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames =
                selectLines().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf(신분당선);
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
        String 신분당선 = "신분당선";
        Long upStationId1 = createStation(StationRequest.from("강남역")).jsonPath().getLong("id");
        Long downStationId1 = createStation(StationRequest.from("신논현역")).jsonPath().getLong("id");
        LineRequest lineRequest1 = LineRequest.of(신분당선, "bg-red-600", upStationId1, downStationId1, 10);

        createLine(lineRequest1);

        String 수인분당선 = "수인분당선";
        Long upStationId2 = createStation(StationRequest.from("압구정로데오역")).jsonPath().getLong("id");
        Long downStationId2 = createStation(StationRequest.from("강남구청역")).jsonPath().getLong("id");
        LineRequest lineRequest2 = LineRequest.of(수인분당선, "bg-yellow-600", upStationId2, downStationId2, 10);

        createLine(lineRequest2);

        // when & then
        List<String> lineNames =
                selectLines().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf(신분당선, 수인분당선);
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
        String 신분당선 = "신분당선";
        Long upStationId = createStation(StationRequest.from("강남역")).jsonPath().getLong("id");
        Long downStationId = createStation(StationRequest.from("신논현역")).jsonPath().getLong("id");
        LineRequest lineRequest = LineRequest.of(신분당선, "bg-red-600", upStationId, downStationId, 10);

        Long lineId = createLine(lineRequest).jsonPath().getLong("id");

        // when & then
        String responseLineName = selectLine(lineId).jsonPath().get("name");
        assertThat(responseLineName).isEqualTo(신분당선);
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
        String 신분당선 = "신분당선";
        Long upStationId = createStation(StationRequest.from("강남역")).jsonPath().getLong("id");
        Long downStationId = createStation(StationRequest.from("신논현역")).jsonPath().getLong("id");
        LineRequest lineRequest = LineRequest.of(신분당선, "bg-red-600", upStationId, downStationId, 10);

        Long lineId = createLine(lineRequest).jsonPath().getLong("id");

        // when
        String 양재역 = "양재역";
        Long newDownStationId = createStation(StationRequest.from(양재역)).jsonPath().getLong("id");
        String 구분당선 = "구분당선";
        LineRequest modifyLineRequest = LineRequest.of(구분당선, "bg-red-600", upStationId, newDownStationId, 10);

        modifyLine(lineId, modifyLineRequest);

        // then
        JsonPath responseJsonPath = selectLine(lineId).jsonPath();
        List<String> stationNames = responseJsonPath.getList("stations.name");

        assertThat((String)responseJsonPath.get("name")).isEqualTo(구분당선);
        assertThat(stationNames).containsAnyOf(양재역);
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
        String 신분당선 = "신분당선";
        Long upStationId = createStation(StationRequest.from("강남역")).jsonPath().getLong("id");
        Long downStationId = createStation(StationRequest.from("신논현역")).jsonPath().getLong("id");
        LineRequest lineRequest = LineRequest.of(신분당선, "bg-red-600", upStationId, downStationId, 10);

        Long lineId = createLine(lineRequest).jsonPath().getLong("id");

        // when
        deleteLine(lineId);

        // then
        assertThat(selectLine(lineId).statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}
