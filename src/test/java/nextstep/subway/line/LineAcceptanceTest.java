package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineSteps.assertCreateLine;
import static nextstep.subway.line.LineSteps.assertCreateLineFail;
import static nextstep.subway.line.LineSteps.assertCreateSection;
import static nextstep.subway.line.LineSteps.assertCreateSectionFail;
import static nextstep.subway.line.LineSteps.assertDeleteLine;
import static nextstep.subway.line.LineSteps.assertGetLine;
import static nextstep.subway.line.LineSteps.assertGetLines;
import static nextstep.subway.line.LineSteps.assertIncludeLines;
import static nextstep.subway.line.LineSteps.assertUpdateLine;
import static nextstep.subway.line.LineSteps.requestCreateLine2;
import static nextstep.subway.line.LineSteps.requestCreateLineDx;
import static nextstep.subway.line.LineSteps.requestCreateSection;
import static nextstep.subway.line.LineSteps.requestDeleteLine;
import static nextstep.subway.line.LineSteps.requestGetLine;
import static nextstep.subway.line.LineSteps.requestGetLines;
import static nextstep.subway.line.LineSteps.requestUpdateLine;
import static nextstep.subway.station.StationSteps.requestCreateStationGangnam;
import static nextstep.subway.station.StationSteps.requestCreateStationPangyo;
import static nextstep.subway.station.StationSteps.requestCreateStationSadang;
import static nextstep.subway.station.StationSteps.requestCreateStationYeoksam;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    ExtractableResponse<Response> stationGangnamResponse;
    ExtractableResponse<Response> stationPangyoResponse;
    ExtractableResponse<Response> stationYeoksamResponse;
    ExtractableResponse<Response> stationSadangResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();
        stationGangnamResponse = requestCreateStationGangnam();
        stationPangyoResponse = requestCreateStationPangyo();
        stationYeoksamResponse = requestCreateStationYeoksam();
        stationSadangResponse = requestCreateStationSadang();
    }

    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void createLine() {
        // when
        ExtractableResponse<Response> response = requestCreateLineDx(stationGangnamResponse, stationPangyoResponse);

        // then
        assertCreateLine(response);
    }

    @Test
    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    void createLineWithDuplicateName() {
        // given
        requestCreateLineDx(stationGangnamResponse, stationPangyoResponse);

        // when
        ExtractableResponse<Response> response = requestCreateLineDx(stationYeoksamResponse, stationSadangResponse);

        // then
        assertCreateLineFail(response);

    }

    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = requestCreateLineDx(stationGangnamResponse, stationPangyoResponse);
        ExtractableResponse<Response> createResponse2 = requestCreateLine2(stationYeoksamResponse, stationSadangResponse);

        // when
        ExtractableResponse<Response> response = requestGetLines();

        // then
        assertGetLines(response);
        assertIncludeLines(response, createResponse1, createResponse2);
    }

    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = requestCreateLineDx(stationGangnamResponse, stationPangyoResponse);

        // when
        ExtractableResponse<Response> response = requestGetLine(createResponse);

        // then
        assertGetLine(response);
    }

    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = requestCreateLineDx(stationGangnamResponse, stationPangyoResponse);

        // when
        ExtractableResponse<Response> response = requestUpdateLine(createResponse);

        // then
        assertUpdateLine(response);
    }

    @Test
    @DisplayName("지하철 노선을 제거한다.")
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = requestCreateLineDx(stationGangnamResponse, stationPangyoResponse);

        // when
        ExtractableResponse<Response> response = requestDeleteLine(createResponse);

        // then
        assertDeleteLine(response);
    }

    @Test
    @DisplayName("지하철 구간을 생성한다.")
    void createSection() {
        // when
        ExtractableResponse<Response> lineResponse = requestCreateLineDx(stationGangnamResponse, stationPangyoResponse);
        ExtractableResponse<Response> response = requestCreateSection(lineResponse, stationGangnamResponse, stationPangyoResponse);

        // then
        assertCreateSection(response);
    }

    @Test
    @DisplayName("노선의 현재 등록되어있는 하행 종점역이 아닌 상행역으로 지하철 구간을 생성한다.")
    void createSectionWithWrongUpStation() {
        // given
        ExtractableResponse<Response> lineResponse = requestCreateLineDx(stationGangnamResponse, stationPangyoResponse);
        requestCreateSection(lineResponse, stationGangnamResponse, stationPangyoResponse);

        // when
        ExtractableResponse<Response> response = requestCreateSection(lineResponse, stationYeoksamResponse, stationSadangResponse);

        //then
        assertCreateSectionFail(response);
    }
}
