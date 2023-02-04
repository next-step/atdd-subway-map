package subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import setting.RandomPortSetting;
import subway.common.util.validation.ExistenceValidation;
import subway.line.LineApi;
import subway.line.util.LineExtraction;
import subway.station.StationApi;
import subway.station.util.StationExtraction;

import static subway.line.MockLine.신분당선;
import static subway.station.MockStation.강남역;
import static subway.station.MockStation.서초역;
import static subway.station.MockStation.신촌역;

@DisplayName("지하철 구간 관리 기능")
class SectionAcceptanceTest extends RandomPortSetting {
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
        LineApi.addSection(신분당선_ID, 서초역_ID, 신촌역_ID, 10)

        // Then
        ExtractableResponse<Response> responseOfShowLine = LineApi.showLine(신분당선_ID);
        ExistenceValidation.checkNamesExistenceInList(responseOfShowLine, 서초역, 신촌역);
    }

    /**
     * Given 지하철 노선에 새로운 구간을 등록 요청하고
     * When 지하철 노선에 마지막 구간을 제거 요청하면
     * Then 지하철 노선에 마지막 구간이 제거된다
     */
    @DisplayName("지하철 노선의 구간을 제거한다")
    @Test
    void removeSection() {
    }
}
