package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineSteps.assertCreateLine;
import static nextstep.subway.line.LineSteps.assertCreateLineFail;
import static nextstep.subway.line.LineSteps.assertDeleteLine;
import static nextstep.subway.line.LineSteps.assertGetLine;
import static nextstep.subway.line.LineSteps.assertGetLines;
import static nextstep.subway.line.LineSteps.assertIncludeLines;
import static nextstep.subway.line.LineSteps.assertUpdateLine;
import static nextstep.subway.line.LineSteps.requestCreateLine2;
import static nextstep.subway.line.LineSteps.requestCreateLineDx;
import static nextstep.subway.line.LineSteps.requestDeleteLine;
import static nextstep.subway.line.LineSteps.requestGetLine;
import static nextstep.subway.line.LineSteps.requestGetLines;
import static nextstep.subway.line.LineSteps.requestUpdateLine;
import static nextstep.subway.line.SectionSteps.requestCreateSection;
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
        ExtractableResponse<Response> lineResponse1 = requestCreateLineDx(stationGangnamResponse, stationPangyoResponse);
        requestCreateSection(lineResponse1, stationGangnamResponse, stationPangyoResponse);
        ExtractableResponse<Response> lineResponse2 = requestCreateLine2(stationYeoksamResponse, stationSadangResponse);
        requestCreateSection(lineResponse2, stationYeoksamResponse, stationSadangResponse);

        // when
        ExtractableResponse<Response> response = requestGetLines();

        // then
        assertGetLines(response);
        assertIncludeLines(response, lineResponse1, lineResponse2);
    }

    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void getLine() {
        // given
        ExtractableResponse<Response> lineResponse = requestCreateLineDx(stationGangnamResponse, stationPangyoResponse);
        requestCreateSection(lineResponse, stationGangnamResponse, stationPangyoResponse);

        // when
        ExtractableResponse<Response> response = requestGetLine(lineResponse);

        // then
        assertGetLine(response);
    }

    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void updateLine() {
        // given
        ExtractableResponse<Response> lineResponse = requestCreateLineDx(stationGangnamResponse, stationPangyoResponse);

        // when
        ExtractableResponse<Response> response = requestUpdateLine(lineResponse, stationYeoksamResponse, stationSadangResponse);

        // then
        assertUpdateLine(response);
    }

    @Test
    @DisplayName("지하철 노선을 제거한다.")
    void deleteLine() {
        // given
        ExtractableResponse<Response> lineResponse = requestCreateLineDx(stationGangnamResponse, stationPangyoResponse);

        // when
        ExtractableResponse<Response> response = requestDeleteLine(lineResponse);

        // then
        assertDeleteLine(response);
    }
}
