package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static support.fixture.LineFixture.이호선_생성;
import static support.fixture.StationFixture.봉천역_생성;
import static support.step.LineSteps.*;
import static support.step.LineSteps.지하철_노선_단일_조회_요청;
import static support.step.LineSteps.지하철_노선_생성_요청;
import static support.step.LineSteps.지하철_노선_응답에서_노선_아이디_추출;
import static support.step.LineSteps.지하철_노선_응답에서_노선의_하행_종점역_아이디_추출;
import static support.step.SectionSteps.*;
import static support.step.StationSteps.지하철_역_생성_요청;
import static support.step.StationSteps.지하철역_응답에서_역_아이디_추출;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import support.annotation.AcceptanceTest;
import support.fixture.SectionFixture;
import support.step.LineSteps;
import support.step.SectionSteps;


@DisplayName("지하철 구간 관련 기능")
@AcceptanceTest
class SectionAcceptanceTest {



    /*
    Given 지하철 노선이 있을 때
    When 구간을 노선에 등록하면
    Then 구간이 노선에 등록되어야 한다.
    Then 노선의 하행역이 구간의 하행역으로 바뀌어야 한다.
     */
    @DisplayName("지하철 구간 등록을 성공한다.")
    @Test
    void createSection() {
        // given
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_노선_생성_요청(이호선_생성());
        var 구간의_상행역_아이디 = 지하철_노선_응답에서_노선의_하행_종점역_아이디_추출(지하철_노선_생성_응답);
        var 구간의_하행역_아이디 = 지하철역_응답에서_역_아이디_추출(지하철_역_생성_요청(봉천역_생성()));
        Long 이호선_아이디 = 지하철_노선_응답에서_노선_아이디_추출(지하철_노선_생성_응답);

        // when
        ExtractableResponse<Response> 지하철_구간_등록_응답 = 지하철_구간_등록_요청(이호선_아이디, SectionFixture.구간_생성(
            구간의_상행역_아이디,
            구간의_하행역_아이디,
            10L
        ));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(지하철_구간_등록_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(지하철_노선_응답에서_노선의_하행_종점역_아이디_추출(지하철_노선_단일_조회_요청(이호선_아이디))).isEqualTo(구간의_하행역_아이디);
        });

    }

    /*
    Given 지하철 노선이 있을 때
    When 상행역이 노선의 하행역과 다른 구간을 노선에 등록하면
    Then 구간이 노선에 등록되지 않고 InvalidUpstationException 발생한다.
     */
    @DisplayName("지하철 구간의 상행역이 노선의 하행역이 아닌경우 등록에 실패한다.")
    @Test
    void createSectionExceptionWithInvalidUpsation() {
        // given

        // when

        // then

    }


    /*
    Given 지하철 노선이 있을 때
    When 노선에 이미 구간의 하행역이 있는 경우, 구간을 노선에 등록하면
    Then 구간이 노선에 등록되지 않고 AlreadyExistStationException 발생한다.
     */
    @DisplayName("지하철 구간 하행역이 노선에 등록되어 있는 경우 등록에 실패한다.")
    @Test
    void createSectionExceptionWithAlreadyExistsStation() {
        // given

        // when

        // then

    }

}
