package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.linestep.LineRequestStep.노선_생성;
import static nextstep.subway.acceptance.sectionstep.SectionRequestStep.*;
import static nextstep.subway.acceptance.stationstep.StationRequestStep.역_생성;
import static nextstep.subway.acceptance.testenum.TestLine.신분당선;

/**
 * Feature     : 노선의 지하철역 구간 관리
 * Backgound   : 노선이 있어야하고, 지하철역이 최소 2개 이상 존재해야한다.
 */
@DisplayName("노선의 지하철역 구간 관리")
public class SectionAcceptanceTest extends AcceptanceTest {
    private Long 강남역Id;
    private Long 양재역Id;
    private int 강남_양재_거리;
    private Long 신분당선Id;

    @BeforeEach
    void 노선과_지하철역_미리_생성() {
        강남역Id = extractId(역_생성("강남역"));
        양재역Id = extractId(역_생성("양재역"));
        강남_양재_거리 = 10;
        신분당선Id = extractId(노선_생성(신분당선, 강남역Id, 양재역Id, 강남_양재_거리));
    }

    private Long extractId(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getLong("id");
    }

    /**
     * Scenario: 생성된 노선과 지하철역들을 통해 구간을 등록한다.
     * when    : 구간 등록을 요청하면
     * then    : 구간이 등록된다.
     */
    @DisplayName("구간을 등록한다.")
    @Test
    void 구간_등록() {
        // given
        Long 역삼역 = extractId(역_생성("역삼역"));
        int 양재_역삼_거리 = 8;

        // when
        ExtractableResponse<Response> postResponse = 구간_생성_요청(양재역Id, 역삼역, 양재_역삼_거리, 신분당선Id);

        // then
        응답_상태_검증(postResponse, HttpStatus.CREATED);
        구간_개수_검증(신분당선Id, 2);
    }

    /**
     * Scenario: 생성된 구간을 삭제한다.
     * given   : 새로운 하행선을 추가하여 구간을 생성한다.
     * when    : 새로운 하행선에 대한 구간 삭제 요청을 하면
     * then    : 구간이 삭제된다.
     */
    @DisplayName("구간을 삭제한다.")
    @Test
    void 구간_삭제() {
        // given
        Long 역삼역 = extractId(역_생성("역삼역"));
        int 양재_역삼_거리 = 5;
        구간_생성_요청(양재역Id, 역삼역, 양재_역삼_거리, 신분당선Id);

        // when
        ExtractableResponse<Response> deleteResponse = 구간_삭제_요청(신분당선Id, 역삼역);

        // then
        응답_상태_검증(deleteResponse, HttpStatus.NO_CONTENT);
        구간_개수_검증(신분당선Id, 1);
    }

    /**
     * Scenario: 생성된 구간을 조회한다.
     * given   : 새로운 하행선을 추가하여 구간을 생성을 요청한다.
     * when    : 해당 노선에 대해 구간 조회를 요청하면
     * then    : 구간이 조회된다.
     */
    @DisplayName("노선의 모든 구간을 조회한다.")
    @Test
    void 모든_구간_조회() {
        // given
        Long 역삼역 = extractId(역_생성("역삼역"));
        int 양재_역삼_거리 = 5;
        구간_생성_요청(양재역Id, 역삼역, 양재_역삼_거리, 신분당선Id);

        // when
        ExtractableResponse<Response> getResponse = 노선의_구간_조회_요청(신분당선Id);

        // then
        응답_상태_검증(getResponse, HttpStatus.OK);
        구간_개수_검증(신분당선Id, 2);
    }

    /**
     * Scenario: 구간 등록시 존재하는 역을 하행역으로 등록하면 예외가 발생한다.
     * when    : 강남역을 하행역으로 등록하여 구간을 생성하면
     * then    : 400 에러가 반환된다.
     */
    @DisplayName("구간 등록시 존재하는 역을 하행역으로 등록할 수 없다.")
    @Test
    void 존재하는_역_하행으로_등록() {
        // when
        ExtractableResponse<Response> postResponse = 구간_생성_요청(양재역Id, 강남역Id, 강남_양재_거리, 신분당선Id);

        // then
        응답_상태_검증(postResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Scenario: 구간 등록시 상행역이 기존 구간의 하행역이 아니면 예외가 발생한다.
     * given   : 새로운 역을 생성하고
     * when    : 새로운 역을 상행역으로 등록하여 구간을 생성하면
     * then    : 400 에러가 반환된다.
     */
    @DisplayName("구간 등록시 상행역이 기존 구간의 하행역이 아니면 등록할 수 없다.")
    @Test
    void 기존의_하행역이_아닌역을_상행역으로_등록() {
        // given
        Long 역삼역 = extractId(역_생성("역삼역"));
        Long 선릉역 = extractId(역_생성("선릉역"));
        int 역삼_선릉_거리 = 3;

        // when
        ExtractableResponse<Response> postResponse = 구간_생성_요청(역삼역, 선릉역, 역삼_선릉_거리, 신분당선Id);

        // then
        응답_상태_검증(postResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Scenario: 구간 등록시 거리가 1보다 작으면 예외를 발생한다.
     * given   : 새로운 역을 생성하고
     * when    : 새로운 역을 구간에 등록하는데 거리가 0이면
     * then    : 400 에러가 반환된다.
     */
    @DisplayName("구간 등록시 거리가 1보다 작으면 등록할 수 없다.")
    @Test
    void 거리를_비정상으로_입력() {
        // given
        Long 선릉역Id = extractId(역_생성("선릉역"));
        int 양재_선릉_거리 = 0;

        // when
        ExtractableResponse<Response> postResponse = 구간_생성_요청(양재역Id, 선릉역Id, 양재_선릉_거리, 신분당선Id);

        // then
        응답_상태_검증(postResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Scenario: 구간 삭제시 입력한 역이 최하행역이 아니면 예외를 발생한다
     * given   : 새로운 역을 생성하고 구간을 생성한다.
     * when    : 최하행역이 아닌 다른 역에 대해 구간 삭제를 요청하면
     * then    : 400 에러가 반환된다.
     */
    @DisplayName("구간을 삭제할때 최하행역이 아니면 구간 삭제가 불가하다.")
    @Test
    void 최하행역이_아닌_역_구간_삭제() {
        // given
        Long 선릉역Id = extractId(역_생성("선릉역"));
        int 양재_선릉_거리 = 5;
        구간_생성_요청(양재역Id, 선릉역Id, 양재_선릉_거리, 신분당선Id);

        // when
        ExtractableResponse<Response> deleteResponse = 구간_삭제_요청(신분당선Id, 양재역Id);

        // then
        응답_상태_검증(deleteResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Scenario: 구간이 1개만 존재한다면 구간 삭제가 불가능하다.
     * when    : 구간을 삭제 요청을 하면
     * then    : 구간 삭제가 이루어지지 않는다. (400 에러)
     */
    @DisplayName("구간이 1개만 존재한다면 구간 삭제가 불가능하다.")
    @Test
    void 단일_구간_존재시_삭제_불가() {
        // when
        ExtractableResponse<Response> deleteResponse = 구간_삭제_요청(신분당선Id, 양재역Id);

        // then
        응답_상태_검증(deleteResponse, HttpStatus.BAD_REQUEST);
    }
}
