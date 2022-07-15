package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.AcceptanceTestBase.assertStatusCode;
import static nextstep.subway.acceptance.ResponseParser.*;
import static nextstep.subway.line.LineRestAssuredTestSource.*;
import static nextstep.subway.station.StationRestAssuredTestSource.역을생성함;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
@AcceptanceTest
public class LineAcceptanceTest {

    private Long 상행역;
    private Long 하행역;

    @BeforeEach
    void setUp() {
        상행역 = 역을생성함("상행역");
        하행역 = 역을생성함("하행역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        final String 신분당선 = "신분당선";

        // when
        final ExtractableResponse<Response> 노선생성결과 = 노선생성(신분당선, 상행역, 하행역);

        // then
        노선생성에성공함(노선생성결과);
        노선목록에노선이등록됨(신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        final String 신분당선 = "신분당선";
        final Long id = 노선ID조회(노선생성(신분당선, 상행역, 하행역));

        // when
        final ExtractableResponse<Response> 노선조회결과 = 노선조회(id);

        // then
        노선조회에성공함(노선조회결과);
        노선이존재함(노선조회결과, 신분당선);
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */

    @DisplayName("지하철 노선 목을 조회한다.")
    @Test
    void getLines() {
        // given
        final String 신분당선 = "신분당선";
        노선생성(신분당선, 상행역, 하행역);

        final String 신신분당선 = "신신분당선";
        노선생성(신신분당선, 상행역, 하행역);

        // when
        final ExtractableResponse<Response> 노선목록조회결과 = 노선목록조회();

        // then
        노선조회에성공함(노선목록조회결과);
        노선목록에노선이존재함(노선목록조회결과, 신분당선);
        노선목록에노선이존재함(노선목록조회결과, 신신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void modifyLines() {
        // given
        final String 신분당선 = "신분당선";
        final Long id = 노선ID조회(노선생성(신분당선, 상행역, 하행역));

        final String 신신분당선 = "신신분당선";

        // when
        final ExtractableResponse<Response> 노선수정결과 = 노선수정(id, 신신분당선, 상행역, 하행역);

        // then
        노선수정에성공함(노선수정결과);

        final ExtractableResponse<Response> 노선목록조회결과 = 노선목록조회();
        노선목록에노선이존재함(노선목록조회결과, 신신분당선);
        노선목록에노선이존재하지않음(노선목록조회결과, 신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        final String 신분당선 = "신분당선";
        final Long id = 노선ID조회(노선생성(신분당선, 상행역, 하행역));

        // when
        final ExtractableResponse<Response> 노선삭제결과 = 노선삭제(id);

        // then
        노선삭제에성공함(노선삭제결과);

        final ExtractableResponse<Response> 노선목록조회결과 = 노선목록조회();
        노선목록에노선이존재하지않음(노선목록조회결과, 신분당선);
    }


    private ListAssert<String> 노선목록에노선이등록됨(final String 노선) {
        return assertThat(getNamesFromResponse(노선목록조회())).contains(노선);
    }

    private void 노선생성에성공함(final ExtractableResponse<Response> createLineResponse) {
        assertStatusCode(createLineResponse, HttpStatus.CREATED);
    }

    private void 노선이존재함(final ExtractableResponse<Response> response, final String 노선) {
        assertThat(getNameFromResponse(response)).contains(노선);
    }

    private void 노선목록에노선이존재함(final ExtractableResponse<Response> response, final String 노선) {
        assertThat(getNamesFromResponse(response)).contains(노선);
    }

    private void 노선목록에노선이존재하지않음(final ExtractableResponse<Response> response, final String 노선) {
        assertThat(getNamesFromResponse(response)).doesNotContain(노선);
    }

    private void 노선조회에성공함(final ExtractableResponse<Response> response) {
        assertStatusCode(response, HttpStatus.OK);
    }

    private void 노선수정에성공함(final ExtractableResponse<Response> response) {
        assertStatusCode(response, HttpStatus.OK);
    }

    private Long 노선ID조회(final ExtractableResponse<Response> 노선생성결과) {
        return getIdFromResponse(노선생성결과);
    }

    private void 노선삭제에성공함(final ExtractableResponse<Response> response) {
        assertStatusCode(response, HttpStatus.NO_CONTENT);
    }

}
