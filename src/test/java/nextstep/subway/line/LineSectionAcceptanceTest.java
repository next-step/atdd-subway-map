package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static nextstep.subway.line.LineSectionSteps.*;
import static nextstep.subway.line.LineSteps.지하철_노선_구간_삭제됨;
import static nextstep.subway.line.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.station.StationSteps.지하철_역_생성_요청;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("지하철 구간 관련 테스트")
public class LineSectionAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역, 양재역, 양재시민의숲, 광교역;
    private LineResponse 신분당선;

    public LineSectionAcceptanceTest(){
        super(); // setUp메서드 상속을 받아서 사용
    }

    @BeforeEach
    void setRequest(){
        // given
        강남역 = 지하철_역_생성_요청("강남역").as(StationResponse.class);
        양재역 = 지하철_역_생성_요청("양재역").as(StationResponse.class);
        양재시민의숲 = 지하철_역_생성_요청("양재시민의숲").as(StationResponse.class);
        광교역 = 지하철_역_생성_요청("광교역").as(StationResponse.class);

        신분당선 = 지하철_노선_생성_요청(new LineRequest("신분당선", "bg-red-600")).as(LineResponse.class);
    }

    /* =========================================
     * (1) 구간 등록 기능
     * =========================================*/

    String setCreateUri(LineResponse line){
        return "/lines/" + line.getId() + "/sections";
    }

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void createLineSection(){
        // given
        지하철_노선에_구간이_등록되어_있음(new SectionRequest(강남역.getId(), 양재역.getId(), 10), setCreateUri(신분당선));

        //when
        ExtractableResponse<Response> response = 지하철_노선에_구간_등록_요청(new SectionRequest(양재역.getId(), 양재시민의숲.getId(), 5), setCreateUri(신분당선));

        // then
        지하철_노선에_구간_생성됨(response);
        지하철_노선_순서대로_정렬됨(response, Arrays.asList(강남역.getId(), 양재역.getId(), 양재시민의숲.getId()));
    }

    @DisplayName("하행 종점역이 아닌 상행역을 등록한다.")
    @Test
    void createLineSectionWithUpStation(){
        // given
        지하철_노선에_구간이_등록되어_있음(new SectionRequest(강남역.getId(), 양재역.getId(), 10), setCreateUri(신분당선));

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_등록_요청(new SectionRequest(광교역.getId(), 양재시민의숲.getId(), 5), setCreateUri(신분당선));

        // then
        지하철_노선에_구간_생성_실패됨(response);
    }

    @DisplayName("이미 등록된 역을 하행역으로 등록한다.")
    @Test
    void createLineSectionWithDownStation(){
        // given
        지하철_노선에_구간이_등록되어_있음(new SectionRequest(강남역.getId(), 양재역.getId(), 10), setCreateUri(신분당선));
        지하철_노선에_구간이_등록되어_있음(new SectionRequest(양재역.getId(), 양재시민의숲.getId(), 5), setCreateUri(신분당선));

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_등록_요청(new SectionRequest(양재시민의숲.getId(), 강남역.getId(), 7), setCreateUri(신분당선));

        // then
        지하철_노선에_구간_생성_실패됨(response);
    }

    @DisplayName("등록되지 않은 역으로 노선 등록한다.")
    @Test
    void createLineSectionWithNotExistsStation(){
        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_등록_요청(new SectionRequest(광교역.getId(), 99L, 5), setCreateUri(신분당선));

        // then
        지하철_노선에_구간_생성_실패됨(response);
    }

    /* =========================================
     * (2) 구간 제거 기능
     * =========================================*/

    String setDeleteUri(LineResponse line, StationResponse station){
        return "/lines/" + line.getId() + "/sections?stationId=" + station.getId();
    }

    @DisplayName("지하철 노선의 구간을 제거한다.")
    @Test
    void deleteLineSection(){
        // given
        지하철_노선에_구간이_등록되어_있음(new SectionRequest(강남역.getId(), 양재역.getId(), 10), setCreateUri(신분당선));
        지하철_노선에_구간이_등록되어_있음(new SectionRequest(양재역.getId(), 양재시민의숲.getId(), 5), setCreateUri(신분당선));

        // when
        ExtractableResponse<Response> response = 지하철_노선의_구간_삭제_요청(setDeleteUri(신분당선, 양재시민의숲));

        // then
        지하철_노선_구간_삭제됨(response);
    }



    @DisplayName("지하철 노선의 마지막역이 아닌 역을 제거 한다.")
    @Test
    void deleteNoneLastStation(){
        // given
        지하철_노선에_구간이_등록되어_있음(new SectionRequest(강남역.getId(), 양재역.getId(), 10), setCreateUri(신분당선));
        지하철_노선에_구간이_등록되어_있음(new SectionRequest(양재역.getId(), 양재시민의숲.getId(), 5), setCreateUri(신분당선));

        // when
        ExtractableResponse<Response> response = 지하철_노선의_구간_삭제_요청(setDeleteUri(신분당선, 양재역));

        // then
        지하철_노선_구간_삭제_실패됨(response);
    }

    @DisplayName("구간이 한개인 지하철 노선의 역을 제거 한다.")
    @Test
    void deleteSingleSectionLine(){
        // given
        지하철_노선에_구간이_등록되어_있음(new SectionRequest(강남역.getId(), 양재역.getId(), 10), setCreateUri(신분당선));

        // when
        ExtractableResponse<Response> response = 지하철_노선의_구간_삭제_요청(setDeleteUri(신분당선, 양재역));

        // then
        지하철_노선_구간_삭제_실패됨(response);
    }

    /* =========================================
     * (3) 구간 목록 조회 기능
     * =========================================*/

    @DisplayName("지하철 노선의 목록을 조회한다.")
    @Test
    void selectLine(){
        // given
        지하철_노선에_구간이_등록되어_있음(new SectionRequest(강남역.getId(), 양재역.getId(), 10), setCreateUri(신분당선));
        지하철_노선에_구간이_등록되어_있음(new SectionRequest(양재역.getId(), 양재시민의숲.getId(), 5), setCreateUri(신분당선));

        // when
        ExtractableResponse<Response> response = 지하철_노선의_목록_조회_요청("lines/"+신분당선.getId());

        // then
        지하철_노선의_목록_조회됨(response);
        지하철_노선_순서대로_정렬됨(response, Arrays.asList(강남역.getId(), 양재역.getId(), 양재시민의숲.getId()));
    }

}
