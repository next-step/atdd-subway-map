package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.line.presentation.LinePatchRequest;
import subway.line.presentation.LineRequest;
import subway.line.presentation.SectionRequest;
import subway.util.DatabaseCleanup;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;
import static subway.line.LineFixture.*;
import static subway.line.LineRestAssured.*;
import static subway.line.LineRestAssured.지하철_노선_생성_요청;
import static subway.stations.StationRestAssured.*;
import static subway.util.ResponseUtils.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    private Long 논현역_ID;

    private Long 강남역_ID;

    private Long 역삼역_ID;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        논현역_ID = Json_추출(지하철역_생성("논현역")).getLong("id");
        강남역_ID = Json_추출(지하철역_생성("강남역")).getLong("id");
        역삼역_ID = Json_추출(지하철역_생성("역삼역")).getLong("id");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선을_생성한다() {
        // given - 노선 생성
        LineRequest 신분당선 = 신분당선_생성(논현역_ID, 강남역_ID);

        // when
        ExtractableResponse<Response> 지하철_노선_생성_요청_응답 = 지하철_노선_생성_요청(신분당선);

        // then - 노선 목록
        지하철_노선_생성_완료됨(신분당선, 지하철_노선_생성_요청_응답);
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void 지하철_노선_목록을_조회한다() {
        // given
        LineRequest 신분당선 = 신분당선_생성(논현역_ID, 강남역_ID);
        LineRequest 강남_2호선 = 강남2호선_생성(논현역_ID, 강남역_ID);
        지하철_노선_생성_요청(신분당선);
        지하철_노선_생성_요청(강남_2호선);

        // when
        ExtractableResponse<Response> 지하철_노선_목록_조회_요청_응답 = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_조회_완료됨(신분당선, 강남_2호선, 지하철_노선_목록_조회_요청_응답);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void 지하철_노선을_조회한다() {
        // given
        LineRequest 신분당선 = 신분당선_생성(논현역_ID, 강남역_ID);
        ExtractableResponse<Response> 지하철_노선_생성_요청_응답 = 지하철_노선_생성_요청(신분당선);

        // when
        ExtractableResponse<Response> 지하철_노선_조회_요청_응답 = 지하철_노선_조회_요청(Json_추출(지하철_노선_생성_요청_응답).getLong("id"));

        // then
        지하철_노선_조회_완료됨(지하철_노선_생성_요청_응답, 지하철_노선_조회_요청_응답);
    }

    /**
     * When 존재하지 않는 지하철 노선 ID로 노선을 조회하면
     * Then 404 응답을 리턴한다.
     */
    @DisplayName("존재하지 않는 id로 지하철 노선을 조회하면 404 상태코드를 리턴한다")
    @Test
    void 존재하지_않는_id로_지하철_노선을_조회하면_404_상태코드를_리턴한다() {
        // when
        ExtractableResponse<Response> 지하철_노선_조회_요청_응답 = 지하철_노선_조회_요청(2L);

        // then
        assertAll(
                () -> assertThat(응답코드(지하철_노선_조회_요청_응답)).isEqualTo(NOT_FOUND.value())
        );
    }

    @Nested
    @DisplayName("지하철 노선 수정")
    class 지하철_노선_수정 {

        @Nested
        @DisplayName("지하철 노선 수정 실패 테스트")
        class 지하철_노선_수정_실패_테스트 {

            @Test
            @DisplayName("존재하지 않는 노선 ID를 입력하면 404을 리턴한다.")
            void 존재하지_않는_노선ID를_입력하면_404를_리턴한다() {
                // when
                ExtractableResponse<Response> 지하철_노선_수정_요청_응답 = 지하철_노선_수정_요청(1L);

                // then
                assertThat(응답코드(지하철_노선_수정_요청_응답)).isEqualTo(NOT_FOUND.value());
            }

            @ParameterizedTest
            @CsvSource(value = {"'':''", "'':'green'", "'강남2호선':''"}, delimiter = ':')
            @DisplayName("수정할 값을 입력하지 않으면 400을 리턴한다")
            void 수정할_값을_입력하지_않으면_400을_리턴한다(String name, String color) {
                // given
                LineRequest 신분당선 = 신분당선_생성(논현역_ID, 강남역_ID);
                ExtractableResponse<Response> 지하철_노선_생성_요청_응답 = 지하철_노선_생성_요청(신분당선);

                LinePatchRequest 노선_수정_객체 = 노선_수정_객체_생성(name, color);

                // when
                ExtractableResponse<Response> 지하철_노선_수정_요청_응답 = 지하철_노선_수정_요청(노선_수정_객체, Json_추출(지하철_노선_생성_요청_응답).getLong("id"));

                // then
                assertThat(응답코드(지하철_노선_수정_요청_응답)).isEqualTo(BAD_REQUEST.value());
            }
        }

        @Nested
        @DisplayName("지하철 노선 수정 성공 테스트")
        class 지하철_노선_수정_성공_테스트 {
            /**
             * Given 지하철 노선을 생성하고
             * When 생성한 지하철 노선을 수정하면
             * Then 해당 지하철 노선 정보는 수정된다
             */
            @DisplayName("지하철 노선을 수정한다.")
            @Test
            @Order(Integer.MAX_VALUE)
            void 지하철_노선을_수정한다() {
                // given
                LineRequest 신분당선 = 신분당선_생성(논현역_ID, 강남역_ID);

                ExtractableResponse<Response> 지하철_노선_생성_요청_응답 = 지하철_노선_생성_요청(신분당선);
                long 신분당선_ID = Json_추출(지하철_노선_생성_요청_응답).getLong("id");

                // when
                LinePatchRequest 노선_수정_객체 = 노선_수정_객체_생성("강남강남 2호선", "super green");
                ExtractableResponse<Response> 지하철_노선_수정_요청_결과 = 지하철_노선_수정_요청(노선_수정_객체, 신분당선_ID);

                // then
                ExtractableResponse<Response> 지하철_노선_조회_요청_응답 = 지하철_노선_조회_요청(신분당선_ID);

                지하철_노선_수정_완료됨(신분당선_ID, 노선_수정_객체, 지하철_노선_수정_요청_결과, 지하철_노선_조회_요청_응답);
            }
        }
    }

    @Nested
    @DisplayName("지하철 노선 삭제")
    class 지하철_노선_삭제 {

        /**
         * Given 지하철 노선을 생성하고
         * When 생성한 지하철 노선을 삭제하면
         * Then 해당 지하철 노선 정보는 삭제되고, 삭제한 지하철 노선으로 노선 조회 시 404를 반환한다.
         */
        @DisplayName("지하철 노선을 삭제한다.")
        @Test
        void 지하철_노선을_삭제한다() {
            // given
            LineRequest 신분당선 = 신분당선_생성(논현역_ID, 강남역_ID);

            ExtractableResponse<Response> 지하철_노선_생성_요청_결과_응답 = 지하철_노선_생성_요청(신분당선);
            long 신분당선_ID = Json_추출(지하철_노선_생성_요청_결과_응답).getLong("id");

            // when
            ExtractableResponse<Response> 지하철_노선_삭제_요청_응답 = 지하철_노선_삭제_요청(신분당선_ID);

            // then
            지하철_노선_삭제_완료됨(신분당선_ID, 지하철_노선_삭제_요청_응답);
        }

        @DisplayName("존재하지 않는 노선 ID를 입력하면 404을 리턴한다.")
        @Test
        void 존재하지_않는_노선ID를_입력하면_404를_리턴한다() {
            // when
            ExtractableResponse<Response> 지하철_노선_삭제_요청_응답 = 지하철_노선_삭제_요청(1L);

            // then
            assertThat(응답코드(지하철_노선_삭제_요청_응답)).isEqualTo(NOT_FOUND.value());
        }
    }

    @Nested
    @DisplayName("지하철 구간 테스트")
    class 지하철_구간_테스트 {

        /**
         * Given 지하철 노선을 생성하고
         * When 생성한 지하철 노선에 구간을 등록하면
         * Then 지하철 노선을 조회하면 등록된 구간에 역 ID를 확인할 수 있다.
         */
        @DisplayName("지하철 구간 등록에 성공한다")
        @Test
        void 지하철_구간_등록에_성공한다() {
            // given
            long 신분당선_ID = Json_추출(지하철_노선_생성_요청(신분당선_생성(논현역_ID, 강남역_ID))).getLong("id");

            // when
            SectionRequest 강남_역삼_구간 = 구간_등록_객체_생성(강남역_ID, 역삼역_ID, 15L);
            ExtractableResponse<Response> 지하철_구간_등록_응답 = 지하철_구간_등록(신분당선_ID, 강남_역삼_구간);

            // then
            List<Long> 노선에_속한_역_ID = Arrays.asList(논현역_ID, 강남역_ID, 역삼역_ID);
            지하철_구간_생성_완료됨(지하철_구간_등록_응답, 신분당선_ID, 노선에_속한_역_ID);
        }

        @DisplayName("지하철 구간 등록 실패 - 새로운 구간의 상행역은 노선의 하행 종점이어야 한다")
        @Test
        void 지하철_구간_등록_실패__새로운_구간의_상행역은_노선의_하행_종점이어야_한다() {
            // given
            long 신분당선_ID = Json_추출(지하철_노선_생성_요청(신분당선_생성(논현역_ID, 강남역_ID))).getLong("id");

            // when
            SectionRequest 논현_역삼_구간 = 구간_등록_객체_생성(논현역_ID, 역삼역_ID, 15L);
            ExtractableResponse<Response> 지하철_구간_등록_응답 = 지하철_구간_등록(신분당선_ID, 논현_역삼_구간);

            // then
            assertThat(지하철_구간_등록_응답.statusCode()).isEqualTo(BAD_REQUEST.value());
        }

        @DisplayName("지하철 구간 등록 실패 - 새로운 구간의 하행역은 노선의 등록되어 있는 역일 수 없다")
        @Test
        void 지하철_구간_등록_실패__새로운_구간의_하행역은_노선의_등록되어_있는_역일_수_없다() {
            // given
            long 신분당선_ID = Json_추출(지하철_노선_생성_요청(신분당선_생성(논현역_ID, 강남역_ID))).getLong("id");

            // when
            SectionRequest 논현_역삼_구간 = 구간_등록_객체_생성(강남역_ID, 논현역_ID, 15L);
            ExtractableResponse<Response> 지하철_구간_등록_응답 = 지하철_구간_등록(신분당선_ID, 논현_역삼_구간);

            // then
            assertThat(지하철_구간_등록_응답.statusCode()).isEqualTo(BAD_REQUEST.value());
        }
    }

    private void 지하철_구간_생성_완료됨(ExtractableResponse<Response> 지하철_구간_등록_응답, Long 노선_ID, List<Long> stationIds) {
        ExtractableResponse<Response> 지하철_노선_조회_요청_응답 = 지하철_노선_조회_요청(노선_ID);
        assertAll(
                () -> assertThat(지하철_구간_등록_응답.statusCode()).isEqualTo(CREATED.value()),
                () -> assertThat(Json_추출(지하철_구간_등록_응답).getLong("id")).isEqualTo(노선_ID),
                () -> {
                    for (int i = 0; i < stationIds.size(); i++) {
                        assertThat(Json_추출(지하철_노선_조회_요청_응답).getLong("stations.id[" + i + "]")).isEqualTo(stationIds.get(i));
                    }
                }
        );
    }

    private void 지하철_노선_생성_완료됨(LineRequest 신분당선, ExtractableResponse<Response> 지하철_노선_생성_요청_응답) {
        assertAll(
                () -> assertThat(응답코드(지하철_노선_생성_요청_응답)).isEqualTo(CREATED.value()),
                () -> assertThat(Json_추출(지하철_노선_목록_조회_요청()).getList("name")).contains(신분당선.getName())
        );
    }

    private void 지하철_노선_조회_완료됨(ExtractableResponse<Response> 지하철_노선_생성_요청_응답, ExtractableResponse<Response> 지하철_노선_조회_요청_응답) {
        assertAll(
                () -> assertThat(응답코드(지하철_노선_조회_요청_응답)).isEqualTo(OK.value()),
                () -> assertThat(Json_추출(지하철_노선_조회_요청_응답).getString("name")).isEqualTo(Json_추출(지하철_노선_생성_요청_응답).getString("name"))
        );
    }

    private void 지하철_노선_목록_조회_완료됨(LineRequest 신분당선, LineRequest 강남_2호선, ExtractableResponse<Response> 지하철_노선_목록_조회_요청_응답) {
        List<String> 노선_이름 = Json_추출(지하철_노선_목록_조회_요청_응답).getList("name");
        assertAll(
                () -> assertThat(응답코드(지하철_노선_목록_조회_요청_응답)).isEqualTo(OK.value()),
                () -> assertThat(노선_이름).containsExactly(신분당선.getName(), 강남_2호선.getName())
        );
    }

    private void 지하철_노선_수정_완료됨(long 신분당선_ID, LinePatchRequest 노선_수정_객체, ExtractableResponse<Response> 지하철_노선_수정_요청_결과, ExtractableResponse<Response> 지하철_노선_조회_요청_응답) {
        assertAll(
                () -> assertThat(응답코드(지하철_노선_수정_요청_결과)).isEqualTo(OK.value()),
                () -> assertThat(Json_추출(지하철_노선_조회_요청_응답).getLong("id")).isEqualTo(신분당선_ID),
                () -> assertThat(Json_추출(지하철_노선_조회_요청_응답).getString("name")).isEqualTo(노선_수정_객체.getName()),
                () -> assertThat(Json_추출(지하철_노선_조회_요청_응답).getString("color")).isEqualTo(노선_수정_객체.getColor())
        );
    }

    private void 지하철_노선_삭제_완료됨(long 신분당선_ID, ExtractableResponse<Response> 지하철_노선_삭제_요청_응답) {
        assertAll(
                () -> assertThat(응답코드(지하철_노선_삭제_요청_응답)).isEqualTo(NO_CONTENT.value()),
                () -> assertThat(응답코드(지하철_노선_조회_요청(신분당선_ID))).isEqualTo(NOT_FOUND.value())
        );
    }
}
