//package subway.acceptance;
//
//import support.fixture.LineFixture;
//import support.fixture.SectionFixture;
//import io.restassured.response.ExtractableResponse;
//import io.restassured.response.Response;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//import support.annotation.AcceptanceTest;
//import support.step.SectionSteps;
//import static org.assertj.core.api.Assertions.assertThat;
//import static support.fixture.LineFixture.이호선_생성;
//import static support.step.LineSteps.지하철_노선_생성_요청;
//import static support.step.LineSteps.지하철_노선_응답에서_아이디_추출;
//
//// 윤태한님, 이주오님
//
//@DisplayName("지하철 구간 관련 기능")
//@AcceptanceTest
//class SectionAcceptanceTest {
//
//
//
//    /*
//    Given 지하철 노선이 있을 때
//    When 구간을 노선에 등록하면
//    Then 구간이 노선에 등록되어야 한다.
//    Then 노선의 하행역이 구한의 하행역으로 바뀌어야 한다.
//     */
//    @DisplayName("지하철 구간 등록을 성공한다.")
//    @Test
//    void createSection() {
//        // given
//        Long 이호선_아이디 = 지하철_노선_응답에서_아이디_추출(지하철_노선_생성_요청(이호선_생성()));
//
//        // when
//        ExtractableResponse<Response> response = SectionSteps.지하철_구간_생성_요청(이호선_아이디, SectionFixture.구간_생성(
//
//        ))
//        var 강남_선릉_구간 = response.as(SectionResponse.class);
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
//
//        // then
//        이호선 = LineFixture.get(이호선.getId()).as(LineResponse.class);
//        assertThat(이호선.getStations().get(이호선.getStations().size()-1).getId()).isEqualTo(강남_선릉_구간.getDownStationId());
//    }
//
//    /*
//    Given 지하철 노선이 있을 때
//    When 상행역이 노선의 하행역과 다른 구간을 노선에 등록하면
//    Then 구간이 노선에 등록되지 않고 InvalidUpstationException 발생한다.
//     */
//    @DisplayName("지하철 구간의 상행역이 노선의 하행역이 아닌경우 등록에 실패한다.")
//    @Test
//    void createSectionExceptionWithInvalidUpsation() {
//        // given
//
//        // when
//
//        // then
//
//    }
//
//
//    /*
//    Given 지하철 노선이 있을 때
//    When 노선에 이미 구간의 하행역이 있는 경우, 구간을 노선에 등록하면
//    Then 구간이 노선에 등록되지 않고 AlreadyExistStationException 발생한다.
//     */
//    @DisplayName("지하철 구간 하행역이 노선에 등록되어 있는 경우 등록에 실패한다.")
//    @Test
//    void createSectionExceptionWithAlreadyExistsStation() {
//        // given
//
//        // when
//
//        // then
//
//    }
//
//}
