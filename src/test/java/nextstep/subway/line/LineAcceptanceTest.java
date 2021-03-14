package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.line.SectionSteps.*;
import static nextstep.subway.station.StationSteps.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {


    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        ExtractableResponse<Response> 상행선_지하철_역 = 지하철_역_생성_요청("을지로3가");
        Long 상행선_지하철_역_ID = 생성된_지하철_역_ID_확인(상행선_지하철_역);

        ExtractableResponse<Response> 하행선_지하철_역 = 지하철_역_생성_요청("을지로입구");
        Long 하행선_지하철_역_ID = 생성된_지하철_역_ID_확인(하행선_지하철_역);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("2호선", "green", 상행선_지하철_역_ID, 하행선_지하철_역_ID, 5);

        // then
        지하철_노선_생성_성공(response);
    }

    @DisplayName("존재하지 않는 지하철 역 ID로 지하철 노선을 생성한다.")
    @Test
    void createLineWithNotExistStationId() {
        // given
        ExtractableResponse<Response> 상행선_지하철_역 = 지하철_역_생성_요청("을지로3가");
        Long 상행선_지하철_역_ID = 생성된_지하철_역_ID_확인(상행선_지하철_역);

        ExtractableResponse<Response> 하행선_지하철_역 = 지하철_역_생성_요청("을지로입구");
        Long 하행선_지하철_역_ID = 생성된_지하철_역_ID_확인(하행선_지하철_역);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("2호선", "green", 100L, 하행선_지하철_역_ID, 5);

        // then
        존재하지_않는_지하철_역이기_때문에_잘못된_요청(response);
    }

    @DisplayName("기존에 존재하는 노선 이름으로 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        // given
        ExtractableResponse<Response> 상행선_지하철_역 = 지하철_역_생성_요청("을지로3가");
        Long 상행선_지하철_역_ID = 생성된_지하철_역_ID_확인(상행선_지하철_역);

        ExtractableResponse<Response> 하행선_지하철_역 = 지하철_역_생성_요청("을지로입구");
        Long 하행선_지하철_역_ID = 생성된_지하철_역_ID_확인(하행선_지하철_역);

        지하철_노선_생성_요청("2호선", "green", 상행선_지하철_역_ID, 하행선_지하철_역_ID, 5);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("2호선", "green", 상행선_지하철_역_ID, 하행선_지하철_역_ID, 5);

        // then
        지하철_노선_생성_실패(response);
    }

    @DisplayName("종점역 ID 없이 지하철 노선을 생성한다.")
    @Test
    void createLineWithoutStationID() {
        ExtractableResponse<Response> 상행선_지하철_역 = 지하철_역_생성_요청("을지로3가");
        Long 상행선_지하철_역_ID = 생성된_지하철_역_ID_확인(상행선_지하철_역);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("2호선", "green",상행선_지하철_역_ID,  null, 5);

        // then
        지하철_노선_생성_실패(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> 상행선_지하철_역 = 지하철_역_생성_요청("을지로3가");
        Long 상행선_지하철_역_ID = 생성된_지하철_역_ID_확인(상행선_지하철_역);

        ExtractableResponse<Response> 하행선_지하철_역 = 지하철_역_생성_요청("을지로입구");
        Long 하행선_지하철_역_ID = 생성된_지하철_역_ID_확인(하행선_지하철_역);

        ExtractableResponse<Response> 하행선2_지하철_역 = 지하철_역_생성_요청("충무로입구");
        Long 하행선2_지하철_역_ID = 생성된_지하철_역_ID_확인(상행선_지하철_역);

        지하철_노선_생성_요청("2호선", "green", 상행선_지하철_역_ID, 하행선_지하철_역_ID, 5);
        지하철_노선_생성_요청("3호선", "orange", 상행선_지하철_역_ID, 하행선2_지하철_역_ID, 7);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_조회_성공(response);
        지하철_노선_목록_조회_결과_2건(response);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> 상행선_지하철_역 = 지하철_역_생성_요청("을지로3가");
        Long 상행선_지하철_역_ID = 생성된_지하철_역_ID_확인(상행선_지하철_역);

        ExtractableResponse<Response> 하행선_지하철_역 = 지하철_역_생성_요청("을지로입구");
        Long 하행선_지하철_역_ID = 생성된_지하철_역_ID_확인(하행선_지하철_역);

        ExtractableResponse<Response> createdLine = 지하철_노선_생성_요청("2호선", "green", 상행선_지하철_역_ID, 하행선_지하철_역_ID, 5);
        Long createdLineId = 생성된_지하철_노선_ID_확인(createdLine);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createdLineId);

        // then
        지하철_노선_조회_성공(response);
    }

    @DisplayName("존재하지 않는 노선을 조회한다.")
    @Test
    void getLineWithNoId() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(1L);

        // then
        존재하지_않는_지하철_노선_오류(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> 상행선_지하철_역 = 지하철_역_생성_요청("을지로3가");
        Long upStationId = 생성된_지하철_역_ID_확인(상행선_지하철_역);

        ExtractableResponse<Response> 하행선_지하철_역 = 지하철_역_생성_요청("을지로입구");
        Long downStationId = 생성된_지하철_역_ID_확인(하행선_지하철_역);

        ExtractableResponse<Response> 지하철_노선 = 지하철_노선_생성_요청("2호선", "green", upStationId, downStationId, 5);
        Long createdLineId = 생성된_지하철_노선_ID_확인(지하철_노선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(createdLineId, "2호선", "orange", upStationId, downStationId, 5);

        // then
        지하철_노선_수정_성공(response);
    }

    @DisplayName("존재하지 않는 지하철 노선을 수정한다.")
    @Test
    void updateLineWithNoId() {
        // given
        ExtractableResponse<Response> 상행선_지하철_역 = 지하철_역_생성_요청("을지로3가");
        Long 상행선_지하철_역_ID = 생성된_지하철_역_ID_확인(상행선_지하철_역);

        ExtractableResponse<Response> 하행선_지하철_역 = 지하철_역_생성_요청("을지로입구");
        Long 하행선_지하철_역_ID = 생성된_지하철_역_ID_확인(하행선_지하철_역);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(1L, "2호선", "orange", 상행선_지하철_역_ID, 하행선_지하철_역_ID, 5);

        // then
        존재하지_않는_지하철_노선_오류(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> 상행선_지하철_역 = 지하철_역_생성_요청("을지로3가");
        Long 상행선_지하철_역_ID = 생성된_지하철_역_ID_확인(상행선_지하철_역);

        ExtractableResponse<Response> 하행선_지하철_역 = 지하철_역_생성_요청("을지로입구");
        Long 하행선_지하철_역_ID = 생성된_지하철_역_ID_확인(하행선_지하철_역);

        ExtractableResponse<Response> createdLine = 지하철_노선_생성_요청("2호선", "green", 상행선_지하철_역_ID, 하행선_지하철_역_ID, 5);
        Long createdLineId = 생성된_지하철_노선_ID_확인(createdLine);

        // when
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(createdLineId);

        // then
        지하철_노선_삭제_성공(response);
    }

    @DisplayName("존재하지 않는 지하철 노선을 제거한다.")
    @Test
    void deleteLineWithNoId() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(1L);

        // then
        존재하지_않는_지하철_노선_오류(response);
    }

    //구간 등록 성공
    @DisplayName("구간 등록 정상적으로 성공")
    @Test
    void createSection() {
        //given
        ExtractableResponse<Response> 상행선_지하철_역 = 지하철_역_생성_요청("을지로3가");
        Long 상행선_지하철_역_ID = 생성된_지하철_역_ID_확인(상행선_지하철_역);

        ExtractableResponse<Response> 하행선_지하철_역 = 지하철_역_생성_요청("을지로입구");
        Long 하행선_지하철_역_ID = 생성된_지하철_역_ID_확인(하행선_지하철_역);

        ExtractableResponse<Response> 하행선2_지하철_역 = 지하철_역_생성_요청("시청");
        Long 하행선2_지하철_역_ID = 생성된_지하철_역_ID_확인(하행선2_지하철_역);

        ExtractableResponse<Response> 생성된_지하철_노선 = 지하철_노선_생성_요청("2호선", "green", 상행선_지하철_역_ID, 하행선_지하철_역_ID, 5);
        Long 지하철_노선_ID = 생성된_지하철_노선_ID_확인(생성된_지하철_노선);

        //when
        ExtractableResponse<Response> createdSection = 구간_생성_요청(지하철_노선_ID, 하행선_지하철_역_ID, 하행선2_지하철_역_ID, 3);


        //then
        지하철_구간_등록_성공(createdSection);
    }

    //새로운 구간의 상행역이 현재 등록되어있는 하행 종점역이 아닐 때
    @DisplayName("등록하려는 구간의 상행역이 노선의 하행 종점역이 아닐 때")
    @Test
    void createSectionWithoutLineDownStationId() {
        //given
        ExtractableResponse<Response> 상행선_지하철_역 = 지하철_역_생성_요청("을지로3가");
        Long 상행선_지하철_역_ID = 생성된_지하철_역_ID_확인(상행선_지하철_역);

        ExtractableResponse<Response> 하행선_지하철_역 = 지하철_역_생성_요청("을지로입구");
        Long 하행선_지하철_역_ID = 생성된_지하철_역_ID_확인(하행선_지하철_역);

        ExtractableResponse<Response> 상행선2_지하철_역 = 지하철_역_생성_요청("충무로역");
        Long 상행선2_지하철_역_ID = 생성된_지하철_역_ID_확인(상행선2_지하철_역);

        ExtractableResponse<Response> 생성된_지하철_노선 = 지하철_노선_생성_요청("2호선", "green", 상행선_지하철_역_ID, 하행선_지하철_역_ID, 5);
        Long 지하철_노선_ID = 생성된_지하철_노선_ID_확인(생성된_지하철_노선);

        //when
        ExtractableResponse<Response> response = 구간_생성_요청(지하철_노선_ID, 상행선2_지하철_역_ID, 하행선_지하철_역_ID, 3);

        //then
        지하철_구간_등록_시_상행역이_노선의_종점역이_아님_오류(response);
    }

    @DisplayName("지하철 노선 및 구간을 조회한다.")
    @Test
    void getLinesWithSection() {
        // given
        ExtractableResponse<Response> 상행선_지하철_역 = 지하철_역_생성_요청("을지로3가");
        Long 상행선_지하철_역_ID = 생성된_지하철_역_ID_확인(상행선_지하철_역);

        ExtractableResponse<Response> 하행선_지하철_역 = 지하철_역_생성_요청("을지로입구");
        Long 하행선_지하철_역_ID = 생성된_지하철_역_ID_확인(하행선_지하철_역);

        ExtractableResponse<Response> 하행선2_지하철_역 = 지하철_역_생성_요청("시청역");
        Long 하행선2_지하철_역_ID = 생성된_지하철_역_ID_확인(하행선2_지하철_역);

        ExtractableResponse<Response> 지하철_노선_역 = 지하철_노선_생성_요청("2호선", "green", 상행선_지하철_역_ID, 하행선_지하철_역_ID, 5);
        Long 지하철_노선_ID = 생성된_지하철_노선_ID_확인(지하철_노선_역);

        // when
        구간_생성_요청(지하철_노선_ID, 하행선_지하철_역_ID, 하행선2_지하철_역_ID, 3);

        ExtractableResponse<Response> lines = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_조회_성공(lines);
    }
}
