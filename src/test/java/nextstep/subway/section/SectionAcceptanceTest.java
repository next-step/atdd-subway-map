package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineSteps.requestCreateLineDx;
import static nextstep.subway.section.SectionSteps.assertCreateSection;
import static nextstep.subway.section.SectionSteps.assertCreateSectionFail;
import static nextstep.subway.section.SectionSteps.requestCreateSection;
import static nextstep.subway.station.StationSteps.requestCreateStationGangnam;
import static nextstep.subway.station.StationSteps.requestCreateStationPangyo;
import static nextstep.subway.station.StationSteps.requestCreateStationSadang;
import static nextstep.subway.station.StationSteps.requestCreateStationYeoksam;

@DisplayName("지하철 구간 관련 기능")
class SectionAcceptanceTest extends AcceptanceTest {

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
    @DisplayName("지하철 구간을 생성한다.")
    void createSection() {
        // when
        ExtractableResponse<Response> lineResponse = requestCreateLineDx(stationGangnamResponse, stationPangyoResponse);
        ExtractableResponse<Response> response = requestCreateSection(lineResponse, stationGangnamResponse, stationPangyoResponse);

        // then
        assertCreateSection(response);
    }

    @Test
    @DisplayName("노선에 현재 등록되어있는 하행 종점역이 아닌 상행역으로 지하철 구간을 생성한다.")
    void createSectionWithWrongUpStation() {
        // given
        ExtractableResponse<Response> lineResponse = requestCreateLineDx(stationGangnamResponse, stationPangyoResponse);
        requestCreateSection(lineResponse, stationGangnamResponse, stationPangyoResponse);

        // when
        ExtractableResponse<Response> response = requestCreateSection(lineResponse, stationYeoksamResponse, stationSadangResponse);

        //then
        assertCreateSectionFail(response);
    }

    @Test
    @DisplayName("노선에 현재 등록되어있는 하행역으로 지하철 구간을 생성한다.")
    void createSectionWithDuplicatedDownStation() {
        // given
        ExtractableResponse<Response> lineResponse = requestCreateLineDx(stationGangnamResponse, stationPangyoResponse);
        requestCreateSection(lineResponse, stationGangnamResponse, stationPangyoResponse);
        requestCreateSection(lineResponse, stationPangyoResponse, stationYeoksamResponse);

        // when
        ExtractableResponse<Response> response = requestCreateSection(lineResponse, stationYeoksamResponse, stationPangyoResponse);

        //then
        assertCreateSectionFail(response);
    }
}
