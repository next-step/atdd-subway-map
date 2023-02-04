package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.line.LineApi;
import subway.line.util.LineExtraction;
import subway.setting.AcceptanceTest;
import subway.station.StationApi;
import subway.station.util.StationExtraction;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.MockLine.신분당선;
import static subway.station.MockStation.강남역;
import static subway.station.MockStation.서초역;
import static subway.station.MockStation.신촌역;

@DisplayName("지하철 구간 관리 기능")
class SectionAcceptanceTest extends AcceptanceTest {
    long 신분당선_ID;

    long 강남역_ID;
    long 서초역_ID;

    /**
     * Given 지하철역, 지하철 노선을 생성요청하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> responseOfCreate강남역 = StationApi.createStation(강남역);
        ExtractableResponse<Response> responseOfCreate서초역 = StationApi.createStation(서초역);
        강남역_ID = StationExtraction.getStationId(responseOfCreate강남역);
        서초역_ID = StationExtraction.getStationId(responseOfCreate서초역);

        ExtractableResponse<Response> responseOfCreate신분당선 = LineApi.createLine(신분당선, 강남역_ID, 서초역_ID, 10);
        신분당선_ID = LineExtraction.getLineId(responseOfCreate신분당선);
    }

    /**
     * When 지하철 노선에 새로운 구간을 등록 요청하면
     * Then 지하철 노선에 새로운 구간이 추가된다
     */
    @DisplayName("지하철 노선에 새로운 구간을 등록한다")
    @Test
    void addSection() {
        // Given
        ExtractableResponse<Response> responseOfCreate신촌역 = StationApi.createStation(신촌역);
        long 신촌역_ID = StationExtraction.getStationId(responseOfCreate신촌역);

        // When
        LineApi.addSection(신분당선_ID, 서초역_ID, 신촌역_ID, 10);

        // Then
        ExtractableResponse<Response> responseOfShowLine = LineApi.showLine(신분당선_ID);
        checkIdsExistence(responseOfShowLine, 강남역_ID, 서초역_ID, 신촌역_ID);
    }

    private void checkIdsExistence(ExtractableResponse<Response> response, Long... stationIds) {
        List<Long> idsOfResponse = response.jsonPath().getList("stations.id", Long.class);
        assertThat(idsOfResponse).contains(stationIds);
    }

    /**
     * When 지하철 노선에 새로운 구간 등록 시, 하행 종점역이 아닌 역을 상행역으로 요청하면
     * Then 새로운 구간이 등록되지 않는다. (에러 처리)
     */
    @DisplayName("새로운 지하철 구간 등록 시, 구간의 상행역은 해당 노선의 하행 종점역이어야 한다.")
    @Test
    void invalidUpStation() {
        // Given
        ExtractableResponse<Response> responseOfCreate신촌역 = StationApi.createStation(신촌역);
        long 신촌역_ID = StationExtraction.getStationId(responseOfCreate신촌역);

        // When
        ExtractableResponse<Response> responseOfAddSection = LineApi.addSection(신분당선_ID, 신촌역_ID, 서초역_ID, 10);
        ExtractableResponse<Response> responseOfShowLine = LineApi.showLine(신분당선_ID);

        // Then
        checkBadRequest(responseOfAddSection);
        checkIdNotExistence(responseOfShowLine, 신촌역_ID);
    }

    private static void checkIdNotExistence(ExtractableResponse<Response> response, long stationId) {
        List<Long> idsOfResponse = response.jsonPath().getList("stations.id", Long.class);
        assertThat(idsOfResponse).doesNotContain(stationId);
    }

    private void checkBadRequest(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * When 지하철 노선에 새로운 구간 등록 시, 이미 해당 노선에 등록된 역을 하행역으로 요청하면
     * Then 새로운 구간이 등록되지 않는다. (에러 처리)
     */
    @DisplayName("새로운 지하철 구간 등록 시, 구간의 하행역은 해당 노선에 등록되지 않은 역이어야 한다.")
    @Test
    void invalidDownStation() {
        // When
        ExtractableResponse<Response> responseOfAddSection = LineApi.addSection(신분당선_ID, 서초역_ID, 강남역_ID, 10);

        // Then
        checkBadRequest(responseOfAddSection);
    }

    /**
     * Given 지하철 노선에 새로운 구간을 등록 요청하고
     * When 지하철 노선에 마지막 구간을 제거 요청하면
     * Then 지하철 노선에 마지막 구간이 제거된다
     */
    @DisplayName("지하철 노선의 구간을 제거한다")
    @Test
    void removeSection() {
        // Given
        ExtractableResponse<Response> responseOfCreate신촌역 = StationApi.createStation(신촌역);
        long 신촌역_ID = StationExtraction.getStationId(responseOfCreate신촌역);

        LineApi.addSection(신분당선_ID, 서초역_ID, 신촌역_ID, 10);

        // When
        LineApi.deleteSection(신분당선_ID, 신촌역_ID);

        // Then
        ExtractableResponse<Response> responseOfShowLine = LineApi.showLine(신분당선_ID);
        checkIdNotExistence(responseOfShowLine, 신촌역_ID);
    }

    /**
     * When 지하철 노선에 새로운 구간 제거 시, 이미 해당 노선에 등록된 역을 하행 종점역이 아닌 역을 요청하면
     * Then 새로운 구간이 제거되지 않는다. (에러 처리)
     */
    @DisplayName("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.")
    @Test
    void removeNotTailSection() {
        // Given
        ExtractableResponse<Response> responseOfCreate신촌역 = StationApi.createStation(신촌역);
        long 신촌역_ID = StationExtraction.getStationId(responseOfCreate신촌역);

        LineApi.addSection(신분당선_ID, 서초역_ID, 신촌역_ID, 10);

        // When
        ExtractableResponse<Response> responseOfDeleteSection = LineApi.deleteSection(신분당선_ID, 서초역_ID);

        // Then
        checkBadRequest(responseOfDeleteSection);
    }
}
