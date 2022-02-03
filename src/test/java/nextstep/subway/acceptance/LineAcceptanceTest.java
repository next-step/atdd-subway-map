package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.linestep.LineRequestStep.*;
import static nextstep.subway.acceptance.linestep.LineValidateStep.*;
import static nextstep.subway.acceptance.sectionstep.SectionRequestStep.구간_생성_요청;
import static nextstep.subway.acceptance.sectionstep.SectionRequestStep.응답_상태_검증;
import static nextstep.subway.acceptance.stationstep.StationRequestStep.역_생성;
import static nextstep.subway.acceptance.testenum.TestLine.신분당선;
import static nextstep.subway.acceptance.testenum.TestLine.이호선;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    private Long 강남역Id;
    private Long 양재역Id;
    private int 강남_양재_거리;

    @BeforeEach
    void 지하철_역_미리_생성하기() {
        강남역Id = extractId(역_생성("강남역"));
        양재역Id = extractId(역_생성("양재역"));
        강남_양재_거리 = 10;
    }

    /**
     * Background : 지하철 역 2개가 생성되어있다.
     * When  지하철 노선 생성을 요청 하면
     * Then  지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 노선_생성(신분당선, 강남역Id, 양재역Id, 강남_양재_거리);

        // then
        노선_응답_검증(response, HttpStatus.CREATED, 신분당선);
    }

    /**
     * Background : 지하철 역 2개가 생성되어있다.
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        노선_생성(신분당선, 강남역Id, 양재역Id, 강남_양재_거리);

        // given
        Long 역삼역 = extractId(역_생성("역삼역"));
        Long 선릉역 = extractId(역_생성("선릉역"));
        int 역삼_선릉_거리 = 8;
        노선_생성(이호선, 역삼역, 선릉역, 역삼_선릉_거리);

        // when
        ExtractableResponse<Response> response = 노선_목록_조회();

        // then
        노선_목록_조회_응답_검증(response, HttpStatus.OK, 신분당선, 이호선);
    }

    /**
     * Background : 지하철 역 2개가 생성되어있다.
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        Long lineId = extractId(노선_생성(신분당선, 강남역Id, 양재역Id, 강남_양재_거리));

        // when
        ExtractableResponse<Response> response = 노선_조회(lineId);

        // then
        노선_응답_검증(response, HttpStatus.OK, 신분당선);
    }

    /**
     * Scenario : 노선 조회시 최상행역에서 최하행으로 순서대로 조회
     * background: 역 2개가 생성된다.
     * given    : 노선을 생성하고
     * given    : 역 2개를 추가로 생성하고 각각 노선에 구간을 추가하면
     * when     : 노선 조회요청을 했을때
     * then     : 노선의 역 목록은 최하행에서 하행 순으로 조회된다.
     */
    @DisplayName("노선 조회 - 역 목록 순서대로")
    @Test
    void getLine2() {
        // given
        Long 신분당선Id = extractId(노선_생성(신분당선, 강남역Id, 양재역Id, 강남_양재_거리));
        Long 선릉역Id = extractId(역_생성("선릉역"));
        Long 역삼역Id = extractId(역_생성("역삼역"));

        구간_생성_요청(양재역Id, 선릉역Id, 4, 신분당선Id);
        구간_생성_요청(선릉역Id, 역삼역Id, 6, 신분당선Id);

        // when
        ExtractableResponse<Response> getResponse = 노선_조회(신분당선Id);

        // then
        응답_상태_검증(getResponse, HttpStatus.OK);
        노선_역_목록_검증(getResponse, Arrays.array("강남역", "양재역", "선릉역", "역삼역"));
    }

    /**
     * Background : 지하철 역 2개가 생성되어있다.
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        String modifyName = "구분당선";
        String modifyColor = "bg-blue-600";

        // given
        Long modifiedId = extractId(노선_생성(신분당선, 강남역Id, 양재역Id, 강남_양재_거리));

        // when
        ExtractableResponse<Response> response = 노선_변경(modifyName, modifyColor, modifiedId);

        // then
        노선_응답_상태_검증(response, HttpStatus.OK);
    }

    /**
     * Background : 지하철 역 2개가 생성되어있다.
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        Long deletedId = extractId(노선_생성(신분당선, 강남역Id, 양재역Id, 강남_양재_거리));

        // when
        ExtractableResponse<Response> response = 노선_삭제(deletedId);

        // then
        노선_응답_상태_검증(response, HttpStatus.NO_CONTENT);
    }

    /**
     * Background : 지하철 역 2개가 생성되어있다.
     * Scenario: 지하철 노선의 이름 중복을 검증한다.
     * given   : 지하철 노선 생성을 요청하고
     * when    : 같은 이름의 노선 생성을 다시 요청하면,
     * then    : 두번째 노선은 생성되지 않는다. (409)
     */
    @DisplayName("지하철 노선 중복 검증")
    @Test
    void validateLineName() {
        // given
        노선_생성(신분당선, 강남역Id, 양재역Id, 강남_양재_거리);

        // when
        ExtractableResponse<Response> duplicatedLineResponse = 노선_생성(신분당선, 강남역Id, 양재역Id, 강남_양재_거리);

        // then
        노선_응답_상태_검증(duplicatedLineResponse, HttpStatus.CONFLICT);
    }
}
