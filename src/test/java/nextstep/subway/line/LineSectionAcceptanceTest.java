package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.station.StationAcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static nextstep.subway.line.LineAcceptanceTest.노선_2호선;
import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.line.LineSteps.지하철_노선에_구간_등록_실패;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static nextstep.subway.station.StationSteps.지하철_역_생성_요청;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void addSectionToLine() {
        // given
        ExtractableResponse<Response> lineResponse = 지하철_노선_생성_요청(노선_2호선);
        Long lineId = parseIdFromResponseHeader(lineResponse);
        ExtractableResponse<Response> stationResponse1 = 지하철_역_생성_요청(강남역);
        Long upStationId = parseIdFromResponseHeader(stationResponse1);
        ExtractableResponse<Response> stationResponse2 = 지하철_역_생성_요청(역삼역);
        Long downStationId = parseIdFromResponseHeader(stationResponse2);

        // when
        ExtractableResponse<Response> addSectionResponse = 지하철_노선에_구간_등록_요청(lineId, new SectionRequest(upStationId, downStationId, 7));
        // then
        지하철_노선에_구간_등록_성공(addSectionResponse);
    }

    @DisplayName("지하철 노선에 구간 등록에 실패한다.")
    @Test
    void failToAddSectionToLine_upStationError() {
        // given
        ExtractableResponse<Response> lineResponse = 지하철_노선_생성_요청(노선_2호선);
        Long lineId = parseIdFromResponseHeader(lineResponse);
        ExtractableResponse<Response> stationResponse1 = 지하철_역_생성_요청(강남역);
        Long upStationId = parseIdFromResponseHeader(stationResponse1);
        ExtractableResponse<Response> stationResponse2 = 지하철_역_생성_요청(역삼역);
        Long downStationId = parseIdFromResponseHeader(stationResponse2);
        ExtractableResponse<Response> addSectionResponse = 지하철_노선에_구간_등록_요청(lineId, new SectionRequest(upStationId, downStationId, 7));
        지하철_노선에_구간_등록_성공(addSectionResponse);

        // when
        // 새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다.
        ExtractableResponse<Response> newUpStationResponse = 지하철_역_생성_요청(선릉역);
        Long newUpStationId = parseIdFromResponseHeader(newUpStationResponse);
        ExtractableResponse<Response> newDownStationResponse = 지하철_역_생성_요청(삼성역);
        Long newDownStationId = parseIdFromResponseHeader(newDownStationResponse);
        ExtractableResponse<Response> whenResponse = 지하철_노선에_구간_등록_요청(lineId, new SectionRequest(newUpStationId, newDownStationId, 3));
        // then
        지하철_노선에_구간_등록_실패(whenResponse);
    }

    @DisplayName("지하철 노선에 구간 등록에 실패한다.")
    @Test
    void failToAddSectionToLine_downStationError() {
        // given
        ExtractableResponse<Response> lineResponse = 지하철_노선_생성_요청(노선_2호선);
        Long lineId = parseIdFromResponseHeader(lineResponse);
        ExtractableResponse<Response> stationResponse1 = 지하철_역_생성_요청(강남역);
        Long upStationId = parseIdFromResponseHeader(stationResponse1);
        ExtractableResponse<Response> stationResponse2 = 지하철_역_생성_요청(역삼역);
        Long downStationId = parseIdFromResponseHeader(stationResponse2);
        ExtractableResponse<Response> addSectionResponse = 지하철_노선에_구간_등록_요청(lineId, new SectionRequest(upStationId, downStationId, 7));
        지하철_노선에_구간_등록_성공(addSectionResponse);

        // when
        // 새로운 구간의 하행역은 현재 등록되어있는 역일 수 없다.
        ExtractableResponse<Response> newUpStationResponse = 지하철_역_생성_요청(선릉역);
        Long newUpStationId = parseIdFromResponseHeader(newUpStationResponse);
        ExtractableResponse<Response> whenResponse = 지하철_노선에_구간_등록_요청(lineId, new SectionRequest(newUpStationId, downStationId, 3));
        // then
        지하철_노선에_구간_등록_실패(whenResponse);
    }

    @DisplayName("지하철 노선에서 구간을 제거한다.")
    @Test
    void deleteSectionFromLine() {
        // given
        addSectionToLine();
        // when
        지하철_구간_삭제();
        // then
        지하철_구간_삭제_성공();
    }

//    @DisplayName("지하철 노선에서 구간 제거에 실패한.")
//    @Test
//    void failToDeleteSectionFromLine() {
//        // given
//        지하철_노선_생성_요청(노선_2호선);
//        지하철_노선에_구간_등록_요청();
//        // when
////        지하철 노선에 등록된 마지막 역(하행 종점역)만 제거할 수 있다.
////        지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
////          조건에 부합하지 않는 경우
//        지하철_구간_삭제();
//        // then
//        지하철_구간_삭제_실패();
//    }
//
//    @DisplayName("등록된 구간을 통해 역 목록 조회")
//    @Test
//    void getLineStationOrderBySection() {
//        // given
//        지하철_노선_생성_요청(노선_2호선);
//        지하철_노선에_구간_등록_요청();
//        지하철_노선에_구간_등록_요청();
//        // when
//        지하철_노선_목록_조회();
//        // then
//        // 구간 순서대로 정렬되었는가??
//    }

}
