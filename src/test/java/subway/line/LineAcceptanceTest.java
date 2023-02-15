package subway.line;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.line.presentation.LinePatchRequest;
import subway.line.presentation.LineRequest;
import subway.stations.StationRestAssured;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/*
* TODO
* 중복된 로직 분리하기
*   - jsonPath
*   - 테스트에 필요한 값 세팅 분리(LineRequest, LinePatchRequest ...)
*   - 가독성을 위해 조금 더 나은 변수 네이밍 및 메서드 네이밍
* 전체 테스트를 실행 했을때 성공하게 하기
* 코드 정렬
* */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    private Long 논현역_id;

    private Long 강남역_id;

    @BeforeEach
    void 역_생성() {
        // given - 역 생성
        Map<String, String> 논현역 = new HashMap<>();
        논현역.put("name", "논현역");
        논현역_id = StationRestAssured.getStationId(논현역);

        Map<String, String> 강남역 = new HashMap<>();
        강남역.put("name", "강남역");
        강남역_id = StationRestAssured.getStationId(강남역);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선을_생성한다() {
        // given - 노선 생성
        LineRequest 신분당선 = new LineRequest(
                "신분당선",
                "red",
                논현역_id,
                강남역_id,
                10L);

        // when
        ExtractableResponse<Response> 지하철_노선_생성_요청_응답 = LineRestAssured.지하철_노선_생성_요청(신분당선);

        // then - 노선 목록
        assertAll(
                () -> assertThat(지하철_노선_생성_요청_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> {
                    ExtractableResponse<Response> 지하철_노선_목록_조회_요청_응답 = LineRestAssured.지하철_노선_목록_조회_요청();
                    assertThat(지하철_노선_목록_조회_요청_응답.jsonPath().getList("name")).contains(신분당선.getName());
                }
        );
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
        LineRequest 신분당선 = new LineRequest(
                "신분당선",
                "red",
                논현역_id,
                강남역_id,
                10L);

        LineRequest 강남_2호선 = new LineRequest(
                "강남 2호선",
                "green",
                논현역_id,
                강남역_id,
                20L);

        LineRestAssured.지하철_노선_생성_요청(신분당선);
        LineRestAssured.지하철_노선_생성_요청(강남_2호선);

        // when
        ExtractableResponse<Response> 지하철_노선_목록_조회_요청_응답 = LineRestAssured.지하철_노선_목록_조회_요청();

        // then
        List<String> 노선_이름 = 지하철_노선_목록_조회_요청_응답.jsonPath().getList("name");
        assertAll(
                () -> assertThat(지하철_노선_목록_조회_요청_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(노선_이름).containsExactly(신분당선.getName(), 강남_2호선.getName())
        );
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
        LineRequest 신분당선 = new LineRequest(
                "신분당선",
                "red",
                논현역_id,
                강남역_id,
                10L);

        ExtractableResponse<Response> 지하철_노선_생성_요청_응답 = LineRestAssured.지하철_노선_생성_요청(신분당선);

        // when
        JsonPath createLineJson = 지하철_노선_생성_요청_응답.jsonPath();
        ExtractableResponse<Response> readLineResponse = LineRestAssured.지하철_노선_조회_요청(createLineJson.getLong("id"));

        // then
        JsonPath responseJson = readLineResponse.jsonPath();
        assertAll(
                () -> assertThat(readLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(responseJson.getString("name")).isEqualTo(createLineJson.getString("name"))
        );
    }

    /**
     * When 존재하지 않는 지하철 노선 ID로 노선을 조회하면
     * Then 404 응답을 리턴한다.
     */
    @DisplayName("존재하지 않는 id로 지하철 노선을 조회하면 404 상태코드를 리턴한다")
    @Test
    void 존재하지_않는_id로_지하철_노선을_조회하면_404_상태코드를_리턴한다() {
        // when
        ExtractableResponse<Response> 지하철_노선_조회_요청_응답 = LineRestAssured.지하철_노선_조회_요청(2L);

        // then
        assertAll(
                () -> assertThat(지하철_노선_조회_요청_응답.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
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
                ExtractableResponse<Response> response = LineRestAssured.지하철_노선_수정_요청(1L);

                // then
                assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
            }

            @ParameterizedTest
            @CsvSource(value = {"'':''", "'':'green'", "'강남2호선':''"}, delimiter = ':')
            @DisplayName("수정할 값을 입력하지 않으면 400을 리턴한다")
            void 수정할_값을_입력하지_않으면_400을_리턴한다(String name, String color) {
                // given
                LineRequest 신분당선 = new LineRequest(
                        "신분당선",
                        "red",
                        논현역_id,
                        강남역_id,
                        10L);

                ExtractableResponse<Response> 지하철_노선_생성_요청_응답 = LineRestAssured.지하철_노선_생성_요청(신분당선);
                LinePatchRequest updateParam = new LinePatchRequest(name, color);

                // when
                ExtractableResponse<Response> 지하철_노선_수정_요청_응답 = LineRestAssured.지하철_노선_수정_요청(updateParam, 지하철_노선_생성_요청_응답.jsonPath().getLong("id"));

                // then
                assertThat(지하철_노선_수정_요청_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
                LineRequest 신분당선 = new LineRequest(
                        "신분당선",
                        "red",
                        논현역_id,
                        강남역_id,
                        10L);

                ExtractableResponse<Response> 지하철_노선_생성_요청_응답 = LineRestAssured.지하철_노선_생성_요청(신분당선);

                // when
                LinePatchRequest 강남_2호선 = new LinePatchRequest("강남강남 2호선", "super green");
                ExtractableResponse<Response> 지하철_노선_수정_요청_결과 = LineRestAssured.지하철_노선_수정_요청(강남_2호선, 지하철_노선_생성_요청_응답.jsonPath().getLong("id"));

                // then
                assertAll(
                        () -> assertThat(지하철_노선_수정_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value())
                );
            }
        }
    }

    @Nested
    @DisplayName("지하철 노선 삭제")
    class 지하철_노선_삭제 {

        /**
         * Given 지하철 노선을 생성하고
         * When 생성한 지하철 노선을 삭제하면
         * Then 해당 지하철 노선 정보는 삭제된다
         */
        @DisplayName("지하철 노선을 삭제한다.")
        @Test
        void 지하철_노선을_삭제한다() {
            // given
            LineRequest 신분당선 = new LineRequest(
                    "신분당선",
                    "red",
                    논현역_id,
                    강남역_id,
                    10L);

            ExtractableResponse<Response> 지하철_노선_생성_요청_결과 = LineRestAssured.지하철_노선_생성_요청(신분당선);
            long lineId = 지하철_노선_생성_요청_결과.jsonPath().getLong("id");

            // when
            ExtractableResponse<Response> 지하철_노선_삭제_요청_결과 = LineRestAssured.지하철_노선_삭제_요청(lineId);

            // then
            assertThat(지하철_노선_삭제_요청_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        @DisplayName("존재하지 않는 노선 ID를 입력하면 404을 리턴한다.")
        @Test
        void 존재하지_않는_노선ID를_입력하면_404를_리턴한다() {
            // when
            ExtractableResponse<Response> 지하철_노선_삭제_요청_결과 = LineRestAssured.지하철_노선_삭제_요청(1L);

            // then
            assertThat(지하철_노선_삭제_요청_결과.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }
    }
}
