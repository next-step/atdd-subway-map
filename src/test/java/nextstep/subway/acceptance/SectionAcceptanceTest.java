package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.testsupport.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.support.LineRequest.지하철노선_목록조회_요청;
import static nextstep.subway.acceptance.support.LineRequest.지하철노선_생성_요청후_식별자반환;
import static nextstep.subway.acceptance.support.SectionRequest.구간_생성_요청;
import static nextstep.subway.acceptance.support.SectionRequest.구간_삭제_요청;
import static nextstep.subway.acceptance.support.StationRequest.지하철역_생성_요청후_식별자_반환;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("구간관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @Nested
    class 구간등록기능 {
        /**
         * Given A,B,C 라는 이름을 가진 3개의 역을 생성한다.
         * Given 노선(A역은 상행역, B역은 하행역)을 생성한다.
         * When 새로운 구간(B역은 상행역 C역은 하행역)을 노선에 등록하고
         * Then 등록된 구간의 상행역, 하행역을 확인한다.
         */
        @Test
        void 새로운_구간_등록() {
            // given
            long 지하철역A = 지하철역_생성_요청후_식별자_반환("A");
            long 지하철역B = 지하철역_생성_요청후_식별자_반환("B");
            long 지하철역C = 지하철역_생성_요청후_식별자_반환("C");

            // given
            long 지하철노선 = 지하철노선_생성_요청후_식별자반환("신분당선", "bg-red-600", 지하철역A, 지하철역B, 10);

            // when
            final ExtractableResponse<Response> 지하철구간_생성_응답 = 구간_생성_요청(지하철노선, 지하철역B, 지하철역C, 10);

            // then
            등록된_구간의_상행역_하행역_확인(지하철역B, 지하철역C, 지하철구간_생성_응답);
        }

        /**
         * Given A,B 라는 이름을 가진 2개의 역을 생성한다.
         * Given 노선(A역은 상행역, B역은 하행역)을 생성한다.
         * When 새로운 구간(B역은 상행역 A역은 하행역)을 노선에 등록한다.
         * Then 새로운 구간의 하행역이 노선에 존재해서 등록할 수 없다.
         */
        @Test
        void 새로운_구간의_하행역이_노선에_이미_등록되어있는경우() {
            // given
            long 지하철역A = 지하철역_생성_요청후_식별자_반환("A");
            long 지하철역B = 지하철역_생성_요청후_식별자_반환("B");

            // given
            long 지하철노선 = 지하철노선_생성_요청후_식별자반환("신분당선", "bg-red-600", 지하철역A, 지하철역B, 10);

            // when
            final ExtractableResponse<Response> 지하철구간_생성_응답 = 구간_생성_요청(지하철노선, 지하철역B, 지하철역A, 10);

            // then
            에러발생_확인(2001, 지하철구간_생성_응답);
        }

        /**
         * Given A,B,C,D 라는 이름을 가진 4개의 역을 생성한다.
         * Given 노선(A역은 상행역, B역은 하행역)을 생성한다.
         * When 새로운 구간(C역은 상행역 D역은 하행역)을 노선에 등록한다.
         * Then 노선의 하행종점역과 구간의 상행역이 일치하지 않아 등록할 수 없다.
         */
        @Test
        void 새로운_구간의_상행역이_노선의_하행역이_아닌_경우() {
            // given
            long 지하철역A = 지하철역_생성_요청후_식별자_반환("A");
            long 지하철역B = 지하철역_생성_요청후_식별자_반환("B");
            long 지하철역C = 지하철역_생성_요청후_식별자_반환("C");
            long 지하철역D = 지하철역_생성_요청후_식별자_반환("D");

            // given
            long 지하철노선 = 지하철노선_생성_요청후_식별자반환("신분당선", "bg-red-600", 지하철역A, 지하철역B, 10);

            // when
            final ExtractableResponse<Response> 지하철구간_생성_응답 = 구간_생성_요청(지하철노선, 지하철역D, 지하철역C, 10);

            // then
            에러발생_확인(4000, 지하철구간_생성_응답);
        }

        private void 등록된_구간의_상행역_하행역_확인(final long bId, final long cId, final ExtractableResponse<Response> response) {
            assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.jsonPath().getLong("upStationId")).isEqualTo(bId),
                () -> assertThat(response.jsonPath().getLong("downStationId")).isEqualTo(cId)
                     );
        }
    }

    @Nested
    class 구간제거기능 {
        /**
         * Given A,B,C 라는 이름을 가진 3개의 지하철 역을 생성하고
         * Given 노선(A역 상행역, B역은 하행역)을 생성하고
         * Given 새로운 구간 (B역은 상행역 D역은 하행역)을 노선에 등록한다.
         * When 마지막 구간을 제거하고
         * Then 노선 목록을 조회하여 지하철역이 제거된것을 확인한다.
         */
        @Test
        void 구간_제거_성공(){
            // given
            long 지하철역A = 지하철역_생성_요청후_식별자_반환("A");
            long 지하철역B = 지하철역_생성_요청후_식별자_반환("B");
            long 지하철역C = 지하철역_생성_요청후_식별자_반환("C");

            // given
            long 지하철노선 = 지하철노선_생성_요청후_식별자반환("신분당선", "bg-red-600", 지하철역A, 지하철역B, 10);

            // given
            구간_생성_요청(지하철노선, 지하철역B, 지하철역C, 10);

            // when
            구간_삭제_요청(지하철노선, 지하철역C);

            // then
            지하철목록_조회후_지하철역제거_확인("C");
        }

        /**
         * Given A,B 라는 이름을 가진 2개의 지하철 역을 생성하고
         * Given 노선(A역 상행역, B역은 하행역)을 생성하고
         * When 하나만 존재하는 구간을 제거하면
         * Then 에러를 반환한다.
         */
        @Test
        void 구간_한개인_경우_에러반환(){
            // given
            long 지하철역A = 지하철역_생성_요청후_식별자_반환("A");
            long 지하철역B = 지하철역_생성_요청후_식별자_반환("B");

            // given
            long 지하철노선 = 지하철노선_생성_요청후_식별자반환("신분당선", "bg-red-600", 지하철역A, 지하철역B, 10);

            // when
            final ExtractableResponse<Response> 지하철구간_삭제_응답 = 구간_삭제_요청(지하철노선, 지하철역B);

            // then
            에러발생_확인(4001, 지하철구간_삭제_응답);
        }

        /**
         * Given A,B,C 라는 이름을 가진 3개의 지하철 역을 생성하고
         * Given 노선(A역 상행역, B역은 하행역)을 생성하고
         * Given 새로운 구간 (B역은 상행역 D역은 하행역)을 노선에 등록한다.
         * When 처음 구간을 삭제하면
         * Then 에러를 반환한다.
         */
        @Test
        void 마지막_구간을_제거하지않는_경우_에러반환(){
            // given
            long 지하철역A = 지하철역_생성_요청후_식별자_반환("A");
            long 지하철역B = 지하철역_생성_요청후_식별자_반환("B");
            long 지하철역C = 지하철역_생성_요청후_식별자_반환("C");

            // given
            long 지하철노선 = 지하철노선_생성_요청후_식별자반환("신분당선", "bg-red-600", 지하철역A, 지하철역B, 10);

            // given
            구간_생성_요청(지하철노선, 지하철역B, 지하철역C, 10);

            // when
            final ExtractableResponse<Response> 지하철구간_삭제_응답 = 구간_삭제_요청(지하철노선, 지하철역A);

            // then
            에러발생_확인(4001, 지하철구간_삭제_응답);
        }
    }

    private void 지하철목록_조회후_지하철역제거_확인(String... names) {
        List<String> stationName = 지하철노선_목록조회_요청().jsonPath().getList("stations.name", String.class);
        assertThat(stationName).doesNotContain(names);
    }

    private void 에러발생_확인(final int code, final ExtractableResponse<Response> response) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getLong("code")).isEqualTo(code)
                 );
    }
}
