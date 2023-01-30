package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static subway.LineAcceptanceTestHelper.노선_생성함;
import static subway.SectionAcceptanceTestHelper.*;
import static subway.StationAcceptanceTestHelper.지하철역_생성함;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private Long 강남역;
    private Long 역삼역;
    private Long 선릉역;
    private Long 이호선;

    @BeforeEach

    protected void setUp() {
        super.setUp();
        강남역 = 지하철역_생성함("강남역");
        역삼역 = 지하철역_생성함("역삼역");
        선릉역 = 지하철역_생성함("선릉역");
        이호선 = 노선_생성함("2호선", "bg-green-600", 강남역, 역삼역, 10);
    }

    /**
     * When 한 개의 구간이 존재하는 지하철 노선에 구간을 추가하면
     * Then 지하철 노선 조회 시, 역 목록에서 추가한 구간의 하행역을 찾을 수 있다.
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void addSection() {
        // when
        final ExtractableResponse<Response> 구간_등록_응답 = 구간_등록_요청(이호선, 역삼역, 선릉역, 10);

        // then
        구간이_정상적으로_등록되었는지_확인(구간_등록_응답, 이호선, "선릉역");
    }

    /**
     * When 지하철 노선의 하행 종점역이 아닌 역을 상행역으로 하는 구간을 등록하면
     * Then 지하철 구간 등록에 실패한다.
     */
    @DisplayName("새로운 구간의 상행역이 지하철 노선의 하행 종점역이 아닐 경우, 등록 불가")
    @Test
    void cannotAddSectionWhatIsUpStationNotEqualToLastDownStationInLine() {
        // when
        final ExtractableResponse<Response> 구간_등록_응답 = 구간_등록_요청(이호선, 강남역, 선릉역, 10);

        // then
        하행_종점역을_상행역으로_하는_구간_등록_실패를_확인(구간_등록_응답, 이호선, "선릉역");
    }

    /**
     * Given 한 개의 구간이 존재하는 지하철 노선에 새로운 구간을 추가하고
     * When 지하철 노선의 하행 종점역을 하행역으로 하는 구간을 등록하면
     * Then 지하철 구간 등록에 실패한다.
     */
    @DisplayName("새로운 구간의 하행역이 지하철 노선에 이미 등록되어있을 경우, 등록 불가")
    @Test
    void cannotAddSectionWhatDownStationIsAlreadyRegisteredInLIne() {
        // given
        구간_등록함(이호선, 역삼역, 선릉역, 10);

        // when
        final ExtractableResponse<Response> 구간_등록_응답 = 구간_등록_요청(이호선, 선릉역, 역삼역, 10);

        // then
        노선에_이미_존재하는_역에_대한_구간_등록_실패를_확인(구간_등록_응답);
    }

    /**
     * Given 한 개의 구간이 존재하는 지하철 노선에 구간을 추가한 뒤,
     * When 지하철 노선의 하행 종점역(추가한 구간의 하행역)을 제거하면
     * Then 지하철 노선 조회 시, 역 목록에서 추가한 구간의 하행역을 찾을 수 없다.
     */
    @DisplayName("지하철 마지막 구간 제거")
    @Test
    void removeLastSection() {
        // given
        구간_등록함(이호선, 역삼역, 선릉역, 10);

        // when
        final ExtractableResponse<Response> 구간_제거_응답 = 구간_제거_요청(이호선, 선릉역);

        // then
        구간이_정상적으로_제거되었는지_확인(구간_제거_응답, 이호선, "선릉역");
    }

    /**
     * When 지하철 노선에 존재하지 않는 역에 대한 구간 삭제를 하면
     * Then 지하철 구간 삭제에 실패한다.
     */
    @DisplayName("삭제하려는 역이 노선에 존재하지 않을 경우, 구간 삭제 불가")
    @Test
    void cannotRemoveSectionWhatIsNotExistsStation() {
        // when
        final ExtractableResponse<Response> 구간_제거_응답 = 구간_제거_요청(이호선, 선릉역);

        // then
        노선에_존재하지_않는_역_삭제에_대한_실패를_확인(구간_제거_응답);
    }

    /**
     * Given 한 개의 구간이 존재하는 지하철 노선에 구간을 추가한 뒤,
     * When 지하철 노선의 중간 역(추가한 구간의 상행역)을 제거하면
     * Then 지하철 구간 삭제에 실패한다.
     */
    @DisplayName("마지막 구간이 아닌 구간을 삭제할 경우, 삭제 불가")
    @Test
    void cannotRemoveSectionWhatIsNotLastSection() {
        // given
        구간_등록함(이호선, 역삼역, 선릉역, 10);

        // when
        final ExtractableResponse<Response> 구간_제거_응답 = 구간_제거_요청(이호선, 역삼역);

        // then
        마지막_구간이_아닌_구간_삭제에_대한_실패를_확인(구간_제거_응답, 이호선, "역삼역");
    }

    /**
     * When 한 개의 구간이 존재하는 지하철 노선의 하행 종점역을 제거하면
     * Thne 지하철 구간 삭제에 실패한다.
     */
    @DisplayName("지하철 노선의 구간이 1개일 경우, 구간 삭제 불가")
    @Test
    void cannotRemoveSectionWhenSectionCountInLineIsOne() {
        // when
        final ExtractableResponse<Response> 구간_제거_응답 = 구간_제거_요청(이호선, 역삼역);

        // then
        마지막으로_남은_구간_삭제에_대한_실패를_확인(구간_제거_응답, 이호선, "역삼역");
    }
}
