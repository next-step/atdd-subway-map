package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.AssertSteps.httpStatusCode_검증;
import static nextstep.subway.acceptance.LineFixture.이호선_색상;
import static nextstep.subway.acceptance.LineFixture.이호선_이름;
import static nextstep.subway.acceptance.SectionSteps.*;
import static nextstep.subway.acceptance.StationFixture.*;
import static nextstep.subway.acceptance.StationSteps.지하철_역_생성_요청_응답;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@DisplayName("노선 구간 관리 기능")
class SectionAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 선릉역;
    private LineResponse 이호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철_역_생성_요청_응답(FIXTURE_강남역);
        역삼역 = 지하철_역_생성_요청_응답(FIXTURE_역삼역);
        선릉역 = 지하철_역_생성_요청_응답(FIXTURE_선릉역);

        LineRequest lineRequest = LineRequest.of(이호선_이름, 이호선_색상, 강남역.getId(), 역삼역.getId(), 10);
        이호선 = LineSteps.createLine(lineRequest).as(LineResponse.class);
    }

    /**
     * Given 강남-역삼 구간이 등록된 노선 생성 요청을 하고 <br>
     * When 하행 종점역과 일치하지 않는 상행역(선릉)으로 구간 생성 요청을 하면 <br>
     * Then 구간 생성이 실패한다.
     */
    @DisplayName("하행 종점역과 신규 상행역 불일치")
    @Test
    void 노선_구간_생성_실패_하행종점역_상행역_불일치_예외() {
        //given
        SectionRequest sectionRequest = 신규_구간(이호선, 선릉역, 역삼역);

        //when
        int statusCode = 구간_생성_요청_응답_HttpStatusCode(이호선, sectionRequest);

        //then
        httpStatusCode_검증(statusCode, BAD_REQUEST.value());

    }

    /**
     * Given 강남-역삼 구간이 등록된 노선 생성 요청을 하고 <br>
     * When 이미 구간으로 등록된 역(강남)을 하행역(강남)으로 구간 생성 요청을 하면 <br>
     * Then 구간 생성이 실패한다.
     */
    @DisplayName("구간에 등록되어 있는 역을 신규 하행역 생성 예외")
    @Test
    void 이미_등록된_역을_하행역으로_생성_예외() {
        //given
        SectionRequest sectionRequest = 신규_구간(이호선, 강남역, 선릉역);

        //when
        int statusCode = 구간_생성_요청_응답_HttpStatusCode(이호선, sectionRequest);

        //then
        httpStatusCode_검증(statusCode, BAD_REQUEST.value());

    }

    /**
     * Given 노선 생성 요청을 하고 <br>
     * When 구간 생성 요청을 하면 <br>
     * Then 구간 생성이 성공한다.
     */
    @DisplayName("노선 구간 생성")
    @Test
    void 노선_구간_생성() {
        //given
        SectionRequest sectionRequest = 신규_구간(이호선, 역삼역, 선릉역);

        //when
        int statusCode = 구간_생성_요청_응답_HttpStatusCode(이호선, sectionRequest);

        //then
        httpStatusCode_검증(statusCode, CREATED.value());
    }

    /**
     * Given 강남-역삼 구간이 등록된 노선 생성 요청을 하고 <br>
     * When 구간 삭제 요청을 하면 <br>
     * Then 구간 삭제 실패한다.
     */
    @DisplayName("구간이 1개만 남은 경우 구간 삭제 실패")
    @Test
    void 구간_삭제_실패_예외() {
        //given
        SectionRequest sectionRequest = SectionRequest.valueOf(역삼역.getId(), 이호선.getId());

        //when
        int statusCode = 구간_삭제_요청_응답_HttpStatusCode(sectionRequest);

        //then
        httpStatusCode_검증(statusCode, BAD_REQUEST.value());

    }
}
