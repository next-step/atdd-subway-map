package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.section.dto.SectionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static nextstep.subway.line.LineAcceptanceTest.makeLineRequest;
import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static nextstep.subway.station.StationSteps.지하철_역_생성_요청;
import static nextstep.subway.station.StationSteps.지하철_역_생성;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void addSectionToLine() {
        // given
        Long upStationId = 지하철_역_생성(강남역);
        Long downStationId = 지하철_역_생성(역삼역);
        Map<String, Object> params = makeLineRequest("2호선","bg-green-600",upStationId, downStationId,7);

        // when
        ExtractableResponse<Response> lineResponse = 지하철_노선_생성_요청(params);

        // then
        지하철_노선_생성됨(lineResponse);
    }

    @DisplayName("지하철 노선에 구간 등록에 실패(새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 함)")
    @Test
    void failToAddSectionToLine_upStationError() {
        // given
        Long upStationId = 지하철_역_생성(강남역);
        Long downStationId = 지하철_역_생성(역삼역);
        Map<String, Object> params = makeLineRequest("2호선","bg-green-600", upStationId, downStationId,7);
        ExtractableResponse<Response> lineResponse = 지하철_노선_생성_요청(params);
        Long lineId = getLocationId(lineResponse);

        // when
        ExtractableResponse<Response> stationResponse3 = 지하철_역_생성_요청(삼성역);
        Long newDownStationId = getLocationId(stationResponse3);

        ExtractableResponse<Response> response = 지하철_노선에_구간_등록_요청(lineId, new SectionRequest(upStationId, newDownStationId, 3));

        // then
        지하철_노선에_구간_등록_실패(response);
    }

    @DisplayName("지하철 노선에 구간 등록에 실패(새로운 구간의 하행역은 현재 등록되어있는 역일 수 없음)")
    @Test
    void failToAddSectionToLine_downStationError() {
        // given
        Long upStationId = 지하철_역_생성(강남역);
        Long downStationId = 지하철_역_생성(역삼역);
        Map<String, Object> params = makeLineRequest("2호선","bg-green-600", upStationId, downStationId,7);
        Long lineId = 지하철_노선_생성(params);

        // when
        Long newUpStationId = 지하철_역_생성(선릉역);
        ExtractableResponse<Response> whenResponse = 지하철_노선에_구간_등록_요청(lineId, new SectionRequest(newUpStationId, downStationId, 3));

        // then
        지하철_노선에_구간_등록_실패(whenResponse);
    }

    @DisplayName("지하철 노선에서 구간을 제거한다.")
    @Test
    void deleteSectionFromLine() {
        // given
        Long upStationId = 지하철_역_생성(강남역);
        Long downStationId = 지하철_역_생성(역삼역);
        Map<String, Object> params = makeLineRequest("2호선","bg-green-600",upStationId, downStationId,7);

        Long lineId = 지하철_노선_생성(params);
        Long newDownStationId =  지하철_역_생성(선릉역);

        ExtractableResponse<Response> addSectionResponse2 = 지하철_노선에_구간_등록_요청(lineId, new SectionRequest(downStationId, newDownStationId, 3));
        지하철_노선에_구간_등록_성공(addSectionResponse2);

        // when
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(lineId, newDownStationId);

        // then
        지하철_구간_삭제_성공(response);
    }

    @DisplayName("지하철 노선에서 구간 제거에 실패(하행 종점역이 아님)")
    @Test
    void failToDeleteSectionFromLine_downStationError() {
        // given
        Long upStationId = 지하철_역_생성(강남역);
        Long downStationId = 지하철_역_생성(역삼역);
        Map<String, Object> params = makeLineRequest("2호선","bg-green-600",upStationId, downStationId,7);
        Long lineId = 지하철_노선_생성(params);
        Long newDownStationId = 지하철_역_생성(삼성역);

        ExtractableResponse<Response> addSectionResponse2 = 지하철_노선에_구간_등록_요청(lineId, new SectionRequest(downStationId, newDownStationId, 3));
        지하철_노선에_구간_등록_성공(addSectionResponse2);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철_구간_삭제_요청(lineId, downStationId);
        // then
        지하철_구간_삭제_실패(deleteResponse);
    }

    @DisplayName("지하철 노선에서 구간 제거에 실패(구간이 1개)")
    @Test
    void failToDeleteSectionFromLine_oneSectionError() {
        // given
        Long upStationId = 지하철_역_생성(강남역);
        Long downStationId = 지하철_역_생성(역삼역);
        Map<String, Object> params = makeLineRequest("2호선","bg-green-600",upStationId, downStationId,7);

        Long lineId = 지하철_노선_생성(params);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철_구간_삭제_요청(lineId, downStationId);

        // then
        지하철_구간_삭제_실패(deleteResponse);

    }

    @DisplayName("등록된 구간을 통해 역 목록 조회")
    @Test
    void getLineStationOrderBySection() {
        // given
        Long upStationId = 지하철_역_생성(강남역);
        Long downStationId = 지하철_역_생성(역삼역);
        Map<String, Object> params = makeLineRequest("2호선","bg-green-600",upStationId, downStationId,7);

        Long lineId = 지하철_노선_생성(params);

        Long newDownStationId = 지하철_역_생성(삼성역);

        ExtractableResponse<Response> addSectionResponse2 = 지하철_노선에_구간_등록_요청(lineId, new SectionRequest(downStationId, newDownStationId, 3));
        지하철_노선에_구간_등록_성공(addSectionResponse2);

        // when
        ExtractableResponse<Response> lineStationResponse = 지하철_노선_역_목록_조회_요청(lineId);

        // then
        지하철_노선_역_목록_포함됨(Arrays.asList(upStationId, downStationId, newDownStationId), lineStationResponse);
    }

}
