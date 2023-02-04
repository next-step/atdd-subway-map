package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.common.util.validation.ExistenceValidation;
import subway.common.util.validation.ResponseStatusValidation;
import subway.line.util.LineExtraction;
import subway.line.util.Validation;
import subway.setting.AcceptanceTest;
import subway.station.StationApi;
import subway.station.util.StationExtraction;

import static subway.line.MockLine.분당선;
import static subway.line.MockLine.신분당선;
import static subway.station.MockStation.강남역;
import static subway.station.MockStation.서초역;
import static subway.station.MockStation.신촌역;

@DisplayName("노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {
    long 강남역_ID;
    long 서초역_ID;
    long 신촌역_ID;

    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> responseOfCreate강남역 = StationApi.createStation(강남역);
        ExtractableResponse<Response> responseOfCreate서초역 = StationApi.createStation(서초역);
        ExtractableResponse<Response> responseOfCreate신촌역 = StationApi.createStation(신촌역);
        강남역_ID = StationExtraction.getStationId(responseOfCreate강남역);
        서초역_ID = StationExtraction.getStationId(responseOfCreate서초역);
        신촌역_ID = StationExtraction.getStationId(responseOfCreate신촌역);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철노선 생성")
    void createLine() {
        // When
        LineApi.createLine(신분당선, 강남역_ID, 서초역_ID, 10);

        // Then
        ExtractableResponse<Response> responseOfShowLines = LineApi.showLines();
        ExistenceValidation.checkNameExistenceInList(responseOfShowLines, 신분당선);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("지하철노선 목록 조회")
    void showLines() {
        // Given
        LineApi.createLine(신분당선, 강남역_ID, 서초역_ID, 10);
        LineApi.createLine(분당선, 서초역_ID, 신촌역_ID, 6);

        // When
        ExtractableResponse<Response> responseOfShowLines = LineApi.showLines();

        // Then
        ExistenceValidation.checkCountInList(responseOfShowLines, 2);
        ExistenceValidation.checkNamesExistenceInList(responseOfShowLines, 신분당선, 분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("지하철노선 조회")
    void showLine() {
        // Given
        ExtractableResponse<Response> responseOfCreate신분당선 = LineApi.createLine(신분당선, 강남역_ID, 서초역_ID, 10);
        ResponseStatusValidation.checkCreatedResponse(responseOfCreate신분당선);
        Long 신분당선_ID = LineExtraction.getLineId(responseOfCreate신분당선);

        // When
        ExtractableResponse<Response> responseOfShowLine = LineApi.showLine(신분당선_ID);

        // Then
        ExistenceValidation.checkNameExistence(responseOfShowLine, 신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    @DisplayName("지하철노선 수정")
    void updateLine() {
        // Given
        ExtractableResponse<Response> responseOfCreate신분당선 = LineApi.createLine(신분당선, 강남역_ID, 서초역_ID, 10);
        Long 신분당선_ID = LineExtraction.getLineId(responseOfCreate신분당선);

        // When
        LineApi.updateLine(신분당선_ID, 분당선.getName(), 분당선.getColor());

        // Then
        ExtractableResponse<Response> responseOfShowLine = LineApi.showLine(신분당선_ID);
        ExistenceValidation.checkNameExistence(responseOfShowLine, 분당선);
        Validation.checkColorExistenceInList(responseOfShowLine, 분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    @DisplayName("지하철노선 삭제")
    void deleteLine() {
        // Given
        ExtractableResponse<Response> responseOfCreate신분당선 = LineApi.createLine(신분당선, 강남역_ID, 서초역_ID, 10);

        // When
        Long lineId = LineExtraction.getLineId(responseOfCreate신분당선);
        ExtractableResponse<Response> responseOfDelete = LineApi.deleteLine(lineId);
        ResponseStatusValidation.checkDeletedResponse(responseOfDelete);

        // Then
        ExtractableResponse<Response> responseOfShowLines = LineApi.showLines();
        ExistenceValidation.checkCountInList(responseOfShowLines, 0);
        ExistenceValidation.checkNamesNotExistenceInList(responseOfShowLines, 신분당선);
    }
}
