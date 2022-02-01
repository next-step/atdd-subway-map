package nextstep.subway.acceptance;

import org.junit.jupiter.api.DisplayName;

@DisplayName("지하철 구간 관리 기능")
class SectionAcceptanceTest extends AcceptanceTest {
//    Line 신분당선;
//    Station 강남역;
//    Station 양재역;
//
//    @BeforeEach
//    void init(){
//        신분당선 = 지하철노선_생성("신분당선","bg-red-600");
//    }
//
//    /**
//     * When 지하철 구간 생성을 요청 하면
//     * Then 지하철 구간 생성이 성공한다.
//     */
//    @DisplayName("지하철 구간 등록")
//    @Test
//    void createStation() {
//        // when
//        ExtractableResponse<Response> createResponse = 지하철역_생성();
//
//        // then
//        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
//        assertThat(createResponse.header("Location")).isNotBlank();
//    }
//
//    /**
//     * Given 지하철 구간 생성을 요청 하고
//     * When 지하철 구간 제거를 요청 하면
//     * Then 지하철 구간 제거가 성공한다.
//     */
//    @DisplayName("지하철 구간 제거")
//    @Test
//    void getStations() {
//        /// given
//        String 역삼역 = "역삼역";
//        String 강남역 = "강남역";
//        지하철역_생성(강남역);
//        지하철역_생성(역삼역);
//
//        // when
//        ExtractableResponse<Response> readResponse = 지하철역_목록_조회();
//
//        assertThat(readResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        List<String> stationNames = readResponse.jsonPath().getList("name");
//        assertThat(stationNames).contains(강남역, 역삼역);
//    }


}
