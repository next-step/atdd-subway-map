package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.data.Lines;
import nextstep.subway.data.Stations;
import nextstep.subway.exceptions.AlreadyExistsEntityException;
import nextstep.subway.exceptions.NotEqualsStationException;
import nextstep.subway.exceptions.OnlyOneSectionException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationStep;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineStep.*;
import static nextstep.subway.section.SectionStep.*;
import static nextstep.subway.section.SectionStep.지하철_노선_구간_등록;
import static nextstep.subway.section.SectionStep.지하철_노선_구간_생성;

@DisplayName("지하철 구간 기능 관련")
public class SectionAcceptanceTest extends AcceptanceTest {

    private LineResponse 사호선;
    private StationResponse 금정역;
    private StationResponse 범계역;
    private StationResponse 평촌역;
    private StationResponse 인덕원역;


    @BeforeEach
    void setup() {
        super.setUp();

        금정역 = StationStep.지하철역_생성_요청(Stations.금정역).as(StationResponse.class);
        범계역 = StationStep.지하철역_생성_요청(Stations.범계역).as(StationResponse.class);
        평촌역 = StationStep.지하철역_생성_요청(Stations.평촌역).as(StationResponse.class);
        인덕원역 = StationStep.지하철역_생성_요청(Stations.인덕원역).as(StationResponse.class);

        Lines.사호선.put("upStationId", 금정역.getId() + "");
        Lines.사호선.put("downStationId", 범계역.getId() + "");
        Lines.사호선.put("distance", 10 + "");

        사호선 = 지하철_노선_생성_요청(Lines.사호선).as(LineResponse.class);


    }

    @DisplayName("지하철 노선 구간 등록")
    @Test
    void createSection() {

        //when
        //지하철_노선_구간_등록
        SectionRequest 범계_평촌 = 지하철_노선_구간_생성(범계역, 평촌역, 10);

        ExtractableResponse<Response> response = 지하철_노선_구간_등록(사호선.getId(), 범계_평촌);

        //then
        //기존_지하철_구간_하행역_신규_상행역_일치함
        기존_지하철_구간_하행역_신규_상행역_일치함(response, 금정역, 범계역, 평촌역);
    }

    @DisplayName("지하철 노선 신규 구간의 상행역을 현재 하행 종점역이 아닌 지하철역으로 한다.")
    @Test
    void createSectionWithNotCurrentDownStation() {

        //when
        //지하철_노선_구간_등록
        SectionRequest 평촌_인덕원 = 지하철_노선_구간_생성(평촌역, 인덕원역, 10);

        ExtractableResponse<Response> response = 지하철_노선_구간_등록(사호선.getId(), 평촌_인덕원);

        //then
        //지하철_노선_구간_등록_실패됨
        지하철_노선_구간_등록_실패됨(response);
        응답_에러_메세지_확인(response, NotEqualsStationException.DEFAULT_MSG);
    }

    @DisplayName("지하철 노선 신규 구간의 하행역을 현재 등록되어있는 역으로 한다.")
    @Test
    void createSectionWithRegisteredStation() {
        //when
        //지하철_노선_구간_등록
        SectionRequest 범계역_평촌 = 지하철_노선_구간_생성(범계역, 평촌역, 10);
        SectionRequest 평촌_금정역 = 지하철_노선_구간_생성(평촌역, 금정역, 10);

        ExtractableResponse<Response> createdResponse1 = 지하철_노선_구간_등록(사호선.getId(), 범계역_평촌);
        ExtractableResponse<Response> createdResponse2 = 지하철_노선_구간_등록(사호선.getId(), 평촌_금정역);

        //then
        //지하철_노선_구간_등록_실패됨
        지하철_노선_구간_등록_실패됨(createdResponse2);
        응답_에러_메세지_확인(createdResponse2, AlreadyExistsEntityException.DEFAULT_EXCEPTION_MSG);
    }

    @DisplayName("지하철 노선의 구간을 제거한다.")
    @Test
    void deleteSectionOfLine() {
        //given
        SectionRequest 범계_평촌 = 지하철_노선_구간_생성(범계역, 평촌역, 10);
        SectionRequest 평촌_인덕원 = 지하철_노선_구간_생성(평촌역, 인덕원역, 10);
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_구간_등록(사호선.getId(), 범계_평촌);
        ExtractableResponse<Response> createdResponse2 = 지하철_노선_구간_등록(사호선.getId(), 평촌_인덕원);

        //when
        //지하철_노선_구간_삭제_요청
        ExtractableResponse<Response> deletedResponse = 지하철_노선_구간_삭제_요청(사호선.getId(), 인덕원역.getId());
        ExtractableResponse<Response> readResponse2 = 지하철_노선_조회_요청(사호선.getId());

        //then
        지하철_노선_구간_삭제됨(deletedResponse);
        지하철_노선_지하철역_삭제됨(readResponse2, 인덕원역.getId());
    }

    @DisplayName("지하철 노선의 중간구간을 삭제시도한다.")
    @Test
    void deleteCenterSectionOfLine() {
        //given
        SectionRequest 범계_평촌 = 지하철_노선_구간_생성(범계역, 평촌역, 10);
        ExtractableResponse<Response> createdResponse = 지하철_노선_구간_등록(사호선.getId(), 범계_평촌);

        //when
        ExtractableResponse<Response> deletedResponse = 지하철_노선_구간_삭제_요청(사호선.getId(), 범계역.getId());

        //then
        지하철_노선_구간_삭제_실패됨(deletedResponse);
        응답_에러_메세지_확인(deletedResponse, Line.ONLY_DOWN_STATION_CAN_DELETED);
    }

    @DisplayName("지하철 노선의 구간이 1개인경우 삭제를 시도한다.")
    @Test
    void deleteLastSectionOfLine() {
        //when
        ExtractableResponse<Response> deletedResponse = 지하철_노선_구간_삭제_요청(사호선.getId(), 범계역.getId());

        //then
        지하철_노선_구간_삭제_실패됨(deletedResponse);
        응답_에러_메세지_확인(deletedResponse, OnlyOneSectionException.DEFAULT_MSG);
    }
}
