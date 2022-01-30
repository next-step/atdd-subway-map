package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.dto.LineTestRequest;
import nextstep.subway.acceptance.dto.SectionTestRequest;
import nextstep.subway.acceptance.step.LineTestStep;
import nextstep.subway.acceptance.step.SectionTestStep;
import nextstep.subway.acceptance.step.StationTestStep;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

//    @DisplayName("지하철 노선 생성 시 구간 초기화 기능")
//    @Test
//    void createLineWithSection() {
//        // given
//        LineTestRequest lineTestRequest = LineTestStep.지하철_노선_요청_신분당선_데이터_생성하기();
//
//        // when
//        ExtractableResponse<Response> response = LineTestStep.지하철_노선을_생성한다(lineTestRequest);
//
//        // then
//        // 섹션 정보도 같이 들어갔는지 체크
//    }


    @DisplayName("구간 등록 기능")
    @Test
    void createSection() {
        // given
        LineTestRequest lineRequest = LineTestStep.지하철_노선_요청_신분당선_데이터_생성하기();
        Long 양재시민의숲역_id = StationTestStep.지하철역_생성_후_아이디_추출하기("양재시민의숲역");
        Long lineId = LineTestStep.지하철_노선_생성한_후_아이디_추출하기(lineRequest);
        SectionTestRequest sectionRequest = new SectionTestRequest(lineRequest.getDownStationId(), 양재시민의숲역_id, 3);

        // when
        ExtractableResponse<Response> response = SectionTestStep.지하철역_구간_생성하기(sectionRequest, lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("구간 등록 시 상행역이 노선의 하행 종점역이 아닐 때 실패 기능")
    @Test
    void createSectionNotDownStationFail() {
        // given
        LineTestRequest lineRequest = LineTestStep.지하철_노선_요청_신분당선_데이터_생성하기();
        Long 양재시민의숲역_id = StationTestStep.지하철역_생성_후_아이디_추출하기("양재시민의숲역");
        Long lineId = LineTestStep.지하철_노선_생성한_후_아이디_추출하기(lineRequest);
        SectionTestRequest sectionRequest = new SectionTestRequest(양재시민의숲역_id, lineRequest.getDownStationId(), 3);

        // when
        ExtractableResponse<Response> response = SectionTestStep.지하철역_구간_생성하기(sectionRequest, lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("구간 등록 시 하행역이 해당 노선에 등록되어 있을 때 실패 기능")
    @Test
    void createSectionDuplicateDownStationFail() {

    }
}
