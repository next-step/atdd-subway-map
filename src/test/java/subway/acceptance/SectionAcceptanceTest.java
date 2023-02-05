package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.fixture.FieldFixture.노선_내_역_이름_목록;
import static subway.common.fixture.FieldFixture.식별자_아이디;
import static subway.common.fixture.LineFixture.이호선;
import static subway.common.fixture.SectionFixture.선릉역_구간;
import static subway.common.fixture.SectionFixture.역삼역_구간;
import static subway.common.fixture.StationFixture.강남역;
import static subway.common.fixture.StationFixture.선릉역;
import static subway.common.fixture.StationFixture.역삼역;
import static subway.common.util.JsonPathUtil.리스트로_데이터_추출;
import static subway.common.util.JsonPathUtil.문자열로_데이터_추출;

// 계층 구조를 이용한 BDD 스타일
@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    // 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
    // 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
    // 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 지하철_구간_생성 {

        private String 상행_종점역_id;
        private String 하행_종점역_id;
        private String 생성된_노선_id;

        @BeforeEach
        void setUp() {
            상행_종점역_id = 문자열로_데이터_추출(지하철역_생성_요청(강남역.요청_데이터_생성()), 식별자_아이디);
            하행_종점역_id = 문자열로_데이터_추출(지하철역_생성_요청(역삼역.요청_데이터_생성()), 식별자_아이디);
            생성된_노선_id =
                    문자열로_데이터_추출(지하철_노선_생성_요청(이호선.생성_요청_데이터_생성(상행_종점역_id, 하행_종점역_id)), 식별자_아이디);
        }

        /**
         * When 새로운 역으로 해당 노선에 구간 등록을 하면
         * Then 지하철 노선 단건 조회 시 노선 내 3개의 지하철 역이 조회된다
         */
        @Nested
        @DisplayName("유효한 구간 등록 정보가 주어지면")
        class Context_with_valid_section {

            @BeforeEach
            void setUp() {
                // when
                String 선릉역_id = 문자열로_데이터_추출(지하철역_생성_요청(선릉역.요청_데이터_생성()), 식별자_아이디);
                지하철_구간_생성_요청(생성된_노선_id, 선릉역_구간.요청_데이터_생성(하행_종점역_id, 선릉역_id));
            }

            @Test
            @DisplayName("지하철 노선 단건 조회 시 노선 내 3개의 지하철 역이 조회된다")
            void it_created_section() throws Exception {
                // then
                ExtractableResponse<Response> 지하철_노선_단건_조회_결과 = 지하철_노선_단건_조회_요청(생성된_노선_id);

                assertThat(리스트로_데이터_추출(지하철_노선_단건_조회_결과, 노선_내_역_이름_목록))
                        .hasSize(3);
            }
        }


        /**
         * When 새로운 구간의 상행역이 하행 종점역이 아니라면 (상행역을 상행 종점역으로 설정)
         * Then 구간 등록에 실패한다
         */
        @Nested
        @DisplayName("새로운 구간의 상행역이 해당 노선에 등록되어 있는 하행 종점역이 아닐 경우")
        class Context_with_invalid_upstation_of_new_section {

            @Test
            @DisplayName("409 에러 코드를 응답한다")
            void it_responses_409() throws Exception {
                // when
                String 선릉역_id = 문자열로_데이터_추출(지하철역_생성_요청(선릉역.요청_데이터_생성()), 식별자_아이디);
                ExtractableResponse<Response> 지하철_구간_생성_결과 =
                        지하철_구간_생성_요청(생성된_노선_id, 선릉역_구간.요청_데이터_생성(상행_종점역_id, 선릉역_id));

                // then
                assertThat(지하철_구간_생성_결과.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
            }
        }

        /**
         * When 새로운 구간의 하행역이 이미 노선에 등록되어 있다면 (하행역을 상행 종점역으로 설정)
         * Then 구간 등록에 실패한다
         */
        @Nested
        @DisplayName("새로운 구간의 하행역이 해당 노선에 등록되어 있는 역일 경우")
        class Context_with_already_registered_station {

            @Test
            @DisplayName("409 에러 코드를 응답한다")
            void it_responses_409() throws Exception {
                // when
                ExtractableResponse<Response> 지하철_구간_생성_결과 =
                        지하철_구간_생성_요청(생성된_노선_id, 역삼역_구간.요청_데이터_생성(하행_종점역_id, 상행_종점역_id));

                // then
                assertThat(지하철_구간_생성_결과.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
            }
        }
    }


    // 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
    // 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
    // 새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.
    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 지하철_구간_삭제 {

        private String 생성된_노선_id;
        private String 새로운_하행역_id;

        @BeforeEach
        void setUp() {
            // given
            String 상행_종점역_id = 문자열로_데이터_추출(지하철역_생성_요청(강남역.요청_데이터_생성()), 식별자_아이디);
            String 하행_종점역_id = 문자열로_데이터_추출(지하철역_생성_요청(역삼역.요청_데이터_생성()), 식별자_아이디);
            생성된_노선_id =
                    문자열로_데이터_추출(지하철_노선_생성_요청(이호선.생성_요청_데이터_생성(상행_종점역_id, 하행_종점역_id)), 식별자_아이디);

            새로운_하행역_id = 문자열로_데이터_추출(지하철역_생성_요청(선릉역.요청_데이터_생성()), 식별자_아이디);
            지하철_구간_생성_요청(생성된_노선_id, 선릉역_구간.요청_데이터_생성(하행_종점역_id, 새로운_하행역_id));
        }

        /**
         * Given 지하철 노선을 생성하고
         * and 구간을 추가하고
         * When 노선의 하행 종점역을 삭제하면
         * Then 지하철 노선 단건 조회 시 노선 내 2개의 지하철 역이 조회된다
         */
        @Nested
        @DisplayName("노선의 하행 종점역을 삭제하면")
        class Context_with_valid_section {

            @DisplayName("해당 지하철 구간 정보는 삭제된다")
            @Test
            void it_deleted_section() {
                // when
                지하철_구간_삭제_요청(생성된_노선_id, 새로운_하행역_id);

                // then
                ExtractableResponse<Response> 지하철_노선_단건_조회_결과 = 지하철_노선_단건_조회_요청(생성된_노선_id);
                assertThat(리스트로_데이터_추출(지하철_노선_단건_조회_결과, 노선_내_역_이름_목록))
                        .hasSize(2);
            }
        }
    }
}
