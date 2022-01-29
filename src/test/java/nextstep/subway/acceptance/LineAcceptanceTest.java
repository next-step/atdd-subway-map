package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.acceptance.LineFixture.*;
import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationFixture.*;
import static nextstep.subway.acceptance.StationSteps.지하철_역_생성_되어있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * 🥕 Given 지하철역(상행) 생성을 요청 하고
     * 🥕 And 새로운 지하철역(하행) 생성을 요청 하고
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        final String 강남역_번호 = 지하철_역_생성_되어있음(강남역);
        final String 양재역_번호 = 지하철_역_생성_되어있음(양재역);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성을_요청한다(신분당선, 빨강색, 강남역_번호, 양재역_번호, 강남_양재_거리);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank(),
                () -> assertThat(response.header("Date")).isNotBlank(),
                () -> assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE),
                () -> assertThat(response.body().jsonPath().get("id").equals(1)).isTrue(),
                () -> assertThat(response.body().jsonPath().get(LINE_NAME).equals(신분당선)).isTrue(),
                () -> assertThat(response.body().jsonPath().get(LINE_COLOR).equals(빨강색)).isTrue()
        );
    }

    /**
     * 🥕 Scenario: 지하철 역이 없는 상태에서 지하철 노선 생성
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철역이 없을 때, 지하철 노선 생성")
    @Test
    void createLineExcludeStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성을_요청한다(신분당선, 빨강색, "1", "2", 강남_양재_거리);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("empty station occurred"))
        );
    }

    /**
     * When 공백 이름을 가진 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("지하철 노선 이름 공백")
    @Test
    void createBlankLineName() {
        // given
        final String 강남역_번호 = 지하철_역_생성_되어있음(강남역);
        final String 양재역_번호 = 지하철_역_생성_되어있음(양재역);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_생성을_요청한다("  ", 빨강색, 강남역_번호, 양재역_번호, 강남_양재_거리);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("blank line name occurred"))
        );
    }

    /**
     * When 공백 색깔을 가진 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("지하철 노선 색깔 공백")
    @Test
    void createBlankLineColor() {
        // given
        final String 강남역_번호 = 지하철_역_생성_되어있음(강남역);
        final String 양재역_번호 = 지하철_역_생성_되어있음(양재역);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_생성을_요청한다(신분당선, "  ", 강남역_번호, 양재역_번호, 강남_양재_거리);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("blank line color occurred"))
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선 이름 중복")
    @Test
    void createDuplicateLineName() {
        // given
        final String 강남역_번호 = 지하철_역_생성_되어있음(강남역);
        final String 양재역_번호 = 지하철_역_생성_되어있음(양재역);
        final String 사당역_번호 = 지하철_역_생성_되어있음(사당역);
        final String 낙성대역_번호 = 지하철_역_생성_되어있음(낙성대역);

        지하철_노선_생성을_요청한다(신분당선, 빨강색, 강남역_번호, 양재역_번호, 강남_양재_거리);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_생성을_요청한다(신분당선, 초록색, 사당역_번호, 낙성대역_번호, 사당_낙성대_거리);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("duplicate line name occurred"))
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 색깔로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선 색깔 중복")
    @Test
    void createDuplicateLineColor() {
        // given
        final String 강남역_번호 = 지하철_역_생성_되어있음(강남역);
        final String 양재역_번호 = 지하철_역_생성_되어있음(양재역);
        final String 사당역_번호 = 지하철_역_생성_되어있음(사당역);
        final String 낙성대역_번호 = 지하철_역_생성_되어있음(낙성대역);

        지하철_노선_생성을_요청한다(신분당선, 빨강색, 강남역_번호, 양재역_번호, 강남_양재_거리);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_생성을_요청한다(이호선, 빨강색, 사당역_번호, 낙성대역_번호, 사당_낙성대_거리);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("duplicate line color occurred"))
        );
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
        final String 강남역_번호 = 지하철_역_생성_되어있음(강남역);
        final String 양재역_번호 = 지하철_역_생성_되어있음(양재역);
        final String 사당역_번호 = 지하철_역_생성_되어있음(사당역);
        final String 낙성대역_번호 = 지하철_역_생성_되어있음(낙성대역);

        지하철_노선_생성을_요청한다(신분당선, 빨강색, 강남역_번호, 양재역_번호, 강남_양재_거리);
        지하철_노선_생성을_요청한다(이호선, 초록색, 사당역_번호, 낙성대역_번호, 사당_낙성대_거리);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_목록_조회를_요청한다();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE),
                () -> assertThat(response.header("Date")).isNotBlank(),
                () -> assertThat(response.jsonPath().getList("id")).contains(1, 2),
                () -> assertThat(response.jsonPath().getList(LINE_NAME)).contains(신분당선, 이호선),
                () -> assertThat(response.jsonPath().getList(LINE_COLOR)).contains(빨강색, 초록색)
        );
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
        final String 강남역_번호 = 지하철_역_생성_되어있음(강남역);
        final String 양재역_번호 = 지하철_역_생성_되어있음(양재역);

        final String 신분당선_번호 = 지하철_노선이_생성되어_있음(신분당선, 빨강색, 강남역_번호, 양재역_번호, 강남_양재_거리);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_조회를_요청한다(신분당선_번호);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE),
                () -> assertThat(response.header("Date")).isNotBlank(),
                () -> assertThat(response.body().jsonPath().get("id").equals(1)).isTrue(),
                () -> assertThat(response.body().jsonPath().get(LINE_NAME).equals(신분당선)).isTrue(),
                () -> assertThat(response.body().jsonPath().get(LINE_COLOR).equals(빨강색)).isTrue(),
                () -> assertThat(response.jsonPath().getList("stations.name")).contains(강남역, 양재역)
        );
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
        final String 강남역_번호 = 지하철_역_생성_되어있음(강남역);
        final String 양재역_번호 = 지하철_역_생성_되어있음(양재역);

        final String 신분당선_번호 = 지하철_노선이_생성되어_있음(신분당선, 빨강색, 강남역_번호, 양재역_번호, 강남_양재_거리);

        // when
        final ExtractableResponse<Response> updateResponse = 지하철_노선_변경을_요청한다(신분당선_번호, 구분당선, 파랑색);

        // then
        assertAll(
                () -> assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(updateResponse.header("Date")).isNotBlank()
        );
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
        final String 강남역_번호 = 지하철_역_생성_되어있음(강남역);
        final String 양재역_번호 = 지하철_역_생성_되어있음(양재역);

        final String 신분당선_번호 = 지하철_노선이_생성되어_있음(신분당선, 빨강색, 강남역_번호, 양재역_번호, 강남_양재_거리);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_삭제를_요청한다(신분당선_번호);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(response.header("Date")).isNotBlank()
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선에 새로운 구간 추가 요청을 하면
     * Then 지하철 노선에 새로운 구간이 등록된다.
     */
    @DisplayName("지하철 노선 구간 등록")
    @Test
    void addSection() {
        // given
        final String 강남역_번호 = 지하철_역_생성_되어있음(강남역);
        final String 양재역_번호 = 지하철_역_생성_되어있음(양재역);
        final String 양재시민의숲역_번호 = 지하철_역_생성_되어있음(양재시민의숲역);

        final String 신분당선_번호 = 지하철_노선이_생성되어_있음(신분당선, 빨강색, 강남역_번호, 양재역_번호, 강남_양재_거리);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_구간_등록을_요청한다(신분당선_번호, 양재역_번호, 양재시민의숲역_번호, 양재_양재시민의숲_거리);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 하행 종점이 아닌 상행역으로 새로운 구간 등록 요청을 하면
     * Then 지하철 노선에 새로운 구간 추가가 실패한다.
     */
    @DisplayName("하행 종점이 아닌 상행역으로 지하철 노선 구간 등록")
    @Test
    void addSectionInvalidUpStation() {
        // given
        final String 강남역_번호 = 지하철_역_생성_되어있음(강남역);
        final String 양재역_번호 = 지하철_역_생성_되어있음(양재역);
        final String 양재시민의숲역_번호 = 지하철_역_생성_되어있음(양재시민의숲역);

        final String 신분당선_번호 = 지하철_노선이_생성되어_있음(신분당선, 빨강색, 강남역_번호, 양재역_번호, 강남_양재_거리);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_구간_등록을_요청한다(신분당선_번호, 강남역_번호, 양재시민의숲역_번호, 양재_양재시민의숲_거리);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("invalid add station"))
        );
    }

    /***
     * Given 지하철 노선 생성을 요청 하고
     * And 지하철 노선 구간을 등록하고
     * When 지하철 노선 마지막 구간 제거 요청을 하면
     * Then 지하철 노선 구간이 제거된다.
     */
    @DisplayName("지하철 노선 구간 제거")
    @Test
    void removeLine() {
        // given
        final String 강남역_번호 = 지하철_역_생성_되어있음(강남역);
        final String 양재역_번호 = 지하철_역_생성_되어있음(양재역);
        final String 양재시민의숲역_번호 = 지하철_역_생성_되어있음(양재시민의숲역);

        final String 신분당선_번호 = 지하철_노선이_생성되어_있음(신분당선, 빨강색, 강남역_번호, 양재역_번호, 강남_양재_거리);
        지하철_노선_구간_등록을_요청한다(신분당선_번호, 양재역_번호, 양재시민의숲역_번호, 양재_양재시민의숲_거리);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_구간을_삭제_요청한다(신분당선_번호, 양재시민의숲역_번호);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(response.header("Date")).isNotBlank()
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * And 지하철 노선 구간을 등록하고
     * And 새로운 지하철 노선 구간을 등록하고
     * When 지하철 노선 중간 구간 제거 요청을 하면
     * Then 지하철 노선 구간 제거에 실패한다.
     */
    @DisplayName("지하철 노선 중간 구간 제거")
    @Test
    void removeLineInvalidMiddleStation() {
        // given
        final String 강남역_번호 = 지하철_역_생성_되어있음(강남역);
        final String 양재역_번호 = 지하철_역_생성_되어있음(양재역);
        final String 양재시민의숲역_번호 = 지하철_역_생성_되어있음(양재시민의숲역);

        final String 신분당선_번호 = 지하철_노선이_생성되어_있음(신분당선, 빨강색, 강남역_번호, 양재역_번호, 강남_양재_거리);
        지하철_노선_구간_등록을_요청한다(신분당선_번호, 양재역_번호, 양재시민의숲역_번호, 양재_양재시민의숲_거리);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_구간을_삭제_요청한다(신분당선_번호, 양재역_번호);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("invalid remove section"))
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선 구간 제거 요청을 하면
     * Then 지하철 노선 구간 제거에 실패한다.
     */
    @DisplayName("지하철 노선 단일 구간일 때 구간 제거")
    @Test
    void removeLineInvalidOnlyOneSection() {
        // given
        final String 강남역_번호 = 지하철_역_생성_되어있음(강남역);
        final String 양재역_번호 = 지하철_역_생성_되어있음(양재역);

        final String 신분당선_번호 = 지하철_노선이_생성되어_있음(신분당선, 빨강색, 강남역_번호, 양재역_번호, 강남_양재_거리);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_구간을_삭제_요청한다(신분당선_번호, 양재역_번호);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("invalid remove section"))
        );
    }
}
