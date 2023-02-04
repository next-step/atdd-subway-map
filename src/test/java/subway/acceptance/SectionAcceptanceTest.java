package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.fixture.FieldFixture.노선_내_역_이름_목록;
import static subway.common.fixture.FieldFixture.식별자_아이디;
import static subway.common.fixture.LineFixture.이호선;
import static subway.common.fixture.StationFixture.강남역;
import static subway.common.fixture.StationFixture.범계역;
import static subway.common.fixture.StationFixture.서울대입구역;
import static subway.common.util.JsonPathUtil.리스트로_데이터_추출;
import static subway.common.util.JsonPathUtil.문자열로_데이터_추출;
import static subway.common.util.RestAssuredBuilder.기본_헤더값_설정;

// 계층 구조를 이용한 BDD 스타일
@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    // 지하철 노선에 구간을 등록하는 기능을 구현
    // 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
    // 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
    // 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 지하철_구간_생성 {

        private String 강남역_id;
        private String 서울대입구역_id;

        @BeforeEach
        void setUp() {
            강남역_id = 문자열로_데이터_추출(지하철역_생성_요청(강남역.요청_데이터_생성()), 식별자_아이디);
            서울대입구역_id = 문자열로_데이터_추출(지하철역_생성_요청(서울대입구역.요청_데이터_생성()), 식별자_아이디);
        }

        /**
         * Given 1개의 지하철 노선을 생성하고
         * and 1개의 구간 등록용 지하철 역을 생성하고
         * When 생성한 역으로 해당 노선에 구간 등록을 하면
         * Then 지하철 노선 단건 조회 시 노선 내 3개의 지하철 역이 조회된다
         */
        @Nested
        @DisplayName("유효한 구간 등록 정보가 주어지면")
        class Context_with_valid_section {
            private String 생성된_노선_id;
            private String 범계역_id;

            @BeforeEach
            void setUp() {
                // given
                범계역_id = 문자열로_데이터_추출(지하철역_생성_요청(범계역.요청_데이터_생성()), 식별자_아이디);
                생성된_노선_id =
                        문자열로_데이터_추출(지하철_노선_생성_요청(이호선.생성_요청_데이터_생성(강남역_id, 서울대입구역_id)), 식별자_아이디);

                Map<String, String> params = new HashMap<>();
                params.put("upStationId", 서울대입구역_id);
                params.put("downStationId", 범계역_id);
                params.put("distance", "10");

                // when
                given(기본_헤더값_설정()).log().all()
                        .body(params)
                        .when().post(SECTION_BASE_URL, 생성된_노선_id)
                        .then().log().all()
                        .extract();
            }

            @Test
            @DisplayName("지하철 노선 단건 조회 시 노선 내 3개의 지하철 역이 조회된다")
            void it_created_section() throws Exception {
                // then
                ExtractableResponse<Response> 지하철_노선_단건_조회_결과 = 지하철_노선_단건_조회_요청(생성된_노선_id);

                assertThat(리스트로_데이터_추출(지하철_노선_단건_조회_결과, 노선_내_역_이름_목록))
                        .hasSize(3)
                        .contains(강남역.역_이름(), 서울대입구역.역_이름(), 범계역.역_이름());
            }
        }
    }
}
