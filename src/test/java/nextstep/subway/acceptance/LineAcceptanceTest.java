package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        지하철_역들이_생성되어_있다(5);
        
        // when
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_노선_생성_요청을_한다(
                "신분당선",
                "bg-red-600",
                "1",
                "4",
                "10");

        // then
        지하철_노선_생성이_성공한다("bg-red-600", "신분당선", 지하철_노선_생성_응답);
    }

    /**
     * Given 지하철 노선의 상행 또는 하행이 존재하지 않는 역이고
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("생성할 노선의 지하철 역이 존재하지 않는 지하철 역인 지하철 노선 생성")
    @CsvSource(delimiter = ':', value = {"1:6", "6:1"})
    @ParameterizedTest
    void createLineWithNotExistUpStation(String 상행역, String 하행역) {
        // given
        지하철_역들이_생성되어_있다(5);

        // when
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_노선_생성_요청을_한다(
                "신분당선",
                "bg-red-600",
                상행역,
                하행역,
                "10");

        //then
        지하철_노선_생성이_실패한다(지하철_노선_생성_응답);
    }

    /**
     * Given 지하철 노선을 생성 요청 하고
     * When 같은 이름으로 지하철 노선을 생성 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선 중복이름 생성")
    @Test
    void createDuplicateNameLine() {
        // given
        지하철_역들이_생성되어_있다(5);
        지하철_노선_생성_요청을_한다(
                "신분당선",
                "bg-red-600",
                "1",
                "4",
                "10");

        // when
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_노선_생성_요청을_한다(
                "신분당선",
                "bg-red-600",
                "2",
                "5",
                "3");

        // then
        지하철_노선_생성이_실패한다(지하철_노선_생성_응답);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 노선에 구간을 추가하고
     * THen 지하철 노선에 구간 추가가 성공한다.
     */
    @DisplayName("지하철 노선 구간 추가")
    @Test
    void addSection() {
        지하철_역들이_생성되어_있다(5);
        ExtractableResponse<Response> 지하철_노선_구간_생성_응답 = 지하철_노선_생성_요청을_한다(
                "신분당선",
                "bg-red-600",
                "1",
                "4",
                "10");

        //when
        ExtractableResponse<Response> 지하철_노선_구간_추가_응답 =
                지하철_노선_구간_추가를_요청한다(지하철_노선_구간_생성_응답, "4", "5", "1");

        //then
        지하철_노선_구간_추가에_성공한다(지하철_노선_구간_추가_응답);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 구간의 상행역이 현재 등록되어있는 하행 종점역이 아닌 구간을 노선에 추가하고
     * THen 지하철 노선에 구간 추가가 실패한다.
     */
    @DisplayName("지하철 노선에 구간의 상행역이 현재 등록되어있는 하행 종점역이 아닌 구간을 노선에 추가")
    @Test
    void addSectionFailedByUpStation() {
        지하철_역들이_생성되어_있다(5);
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_노선_생성_요청을_한다(
                "신분당선",
                "bg-red-600",
                "1",
                "4",
                "10");

        //when
        ExtractableResponse<Response> 지하철_노선_구간_추가_응답 =
                지하철_노선_구간_추가를_요청한다(지하철_노선_생성_응답, "3", "5", "10");

        //then
        지하철_노선에_구간_추가가_실패한다(지하철_노선_구간_추가_응답);
    }



    /**
     * Given 지하철 노선을 생성하고
     * When 구간의 하행역이 현재 노선에 등록되어있는 역인 구간을 노선에 추가하고
     * THen 지하철 노선에 구간 추가가 실패한다.
     */
    @DisplayName("지하철 노선에 구간의 상행역이 현재 등록되어있는 하행 종점역이 아닌 구간을 노선에 추가")
    @Test
    void addSectionFailedByDownStation() {
        //given
        지하철_역들이_생성되어_있다(5);
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_노선_생성_요청을_한다(
                "신분당선",
                "bg-red-600",
                "1",
                "4",
                "10");

        //when
        ExtractableResponse<Response> 지하철_노선_구간_추가_응답 =
                지하철_노선_구간_추가를_요청한다(지하철_노선_생성_응답, "5", "2", "10");

        //then
        지하철_노선에_구간_추가가_실패한다(지하철_노선_구간_추가_응답);
    }


    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        지하철_역들이_생성되어_있다(5);
        ExtractableResponse<Response> 신분당선_지하철_노선_생성_응답 = 지하철_노선_생성_요청을_한다(
                "신분당선",
                "bg-red-600",
                "1",
                "3",
                "5");

        ExtractableResponse<Response> 이호선_지하철_노선_생성_응답 = 지하철_노선_생성_요청을_한다(
                "2호선",
                "bg-green-600",
                "2",
                "5",
                "5");

        //when
        ExtractableResponse<Response> 지하철_노선_목록_조회_응답 = 지하철_노선_목록_조회를_요청한다();

        //then
        지하철_노선_목록들을_응답받는다(신분당선_지하철_노선_생성_응답, 이호선_지하철_노선_생성_응답, 지하철_노선_목록_조회_응답);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        지하철_역들이_생성되어_있다(5);
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_노선_생성_요청을_한다(
                "신분당선",
                "bg-red-600",
                "1",
                "4",
                "10");
        지하철_노선_구간_추가를_요청한다(지하철_노선_생성_응답, "4", "5", "3");

        // when
        ExtractableResponse<Response> 지하철_노선_목록_조회_응답 = 지하철_노선_조회를_요청한다(지하철_노선_생성_응답);

        // then
        List<Station> 지하철_역_목록 = Arrays.asList(new Station(1L), new Station(4L), new Station(5L));
        지하철_노선을_응답받는다(지하철_노선_생성_응답, 지하철_노선_목록_조회_응답, 지하철_역_목록);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        지하철_역들이_생성되어_있다(5);
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_노선_생성_요청을_한다(
                "신분당선",
                "bg-red-600",
                "1",
                "2",
                "5");

        // when
        ExtractableResponse<Response> 지하철_노선_수정_요청_응답 = 지하철_노선_수정을_요청한다(지하철_노선_생성_응답, "구분당선", "bg-blue-600");

        // then
        지하철_노선_수정이_성공한다(지하철_노선_수정_요청_응답);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        지하철_역들이_생성되어_있다(5);
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_노선_생성_요청을_한다(
                "신분당선",
                "bg-red-600",
                "1",
                "2",
                "5");

        // when
        ExtractableResponse<Response> 지하철_노선_삭제_요청_응답 = 지하철_노선_삭제를_요청한다(지하철_노선_생성_응답);

        // then
        지하철_노선_삭제가_성공한다(지하철_노선_삭제_요청_응답);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * GIven 노선에 구간의 추가를 요청하고
     * When 생성한 지하철 노선에 구간의 삭제를 요청 하면
     * Then 생성한 지하철 노선에 구간의 삭제가 성공한다.
     */
    @DisplayName("지하철 노선의 구간 삭제")
    @Test
    void deleteSection() {
        // given
        지하철_역들이_생성되어_있다(5);
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_노선_생성_요청을_한다(
                "신분당선",
                "bg-red-600",
                "1",
                "4",
                "10");

        String 마지막_노선_구간의_하행 = "5";
        지하철_노선_구간_추가를_요청한다(지하철_노선_생성_응답, "4", 마지막_노선_구간의_하행, "2");

        // when
        ExtractableResponse<Response> 지하철_노선_구간_삭제_요청_응답 = 지하철_노선_구간_삭제를_요청한다(지하철_노선_생성_응답, 마지막_노선_구간의_하행);

        //then
        지하철_노선_구간_삭제가_성공한다(지하철_노선_구간_삭제_요청_응답);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선에 구간의 삭제를 요청 하면
     * Then 생성한 지하철 노선에 구간의 삭제가 실패한다.
     */
    @DisplayName("지하철 노선의 구간이 하나일때 삭제")
    @Test
    void deleteSectionFailed() {
        // given
        지하철_역들이_생성되어_있다(5);
        String 노선_구간의_하행 = "4";
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_노선_생성_요청을_한다(
                "신분당선",
                "bg-red-600",
                "1",
                노선_구간의_하행,
                "10");

        // when
        ExtractableResponse<Response> 지하철_노선_구간_삭제_요청_응답 = 지하철_노선_구간_삭제를_요청한다(지하철_노선_생성_응답, 노선_구간의_하행);

        //then
        지하철_노선에_구간_삭제가_실패한다(지하철_노선_구간_삭제_요청_응답);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 노선에 구간의 추가를 요청하고
     * When 생성한 지하철 노선에 마지막 구간이 아닌 다른 구간의 지하철역을 삭제요청 하면
     * Then 생성한 지하철 노선에 구간의 삭제가 실패한다.
     */
    @DisplayName("지하철 노선의 마지막 구간의 하행이 아닌 다른 구간의 지하철역을 삭제")
    @Test
    void deleteSectionFailedByNotLastDownStation() {
        // given
        지하철_역들이_생성되어_있다(5);
        String 노선_구간의_하행 = "4";
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_노선_생성_요청을_한다(
                "신분당선",
                "bg-red-600",
                "1",
                노선_구간의_하행,
                "10");
        지하철_노선_구간_추가를_요청한다(지하철_노선_생성_응답, "4", "5", "10");

        // when
        ExtractableResponse<Response> 지하철_노선_구간_삭제_요청_응답 = 지하철_노선_구간_삭제를_요청한다(지하철_노선_생성_응답, 노선_구간의_하행);

        //then
        지하철_노선에_구간_삭제가_실패한다(지하철_노선_구간_삭제_요청_응답);
    }
}