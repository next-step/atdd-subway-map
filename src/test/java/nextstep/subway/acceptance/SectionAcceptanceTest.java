package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.utils.AssuredRequest;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 구간 관리 기능")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SectionAcceptanceTest extends AcceptanceTest {

    private static final String LINE_END_POINT = "/lines";
    private static final String STATION_END_POINT = "/stations";

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철역 생성을 요청 하고
     * Given 새로운 지하철역 생성을 요청 하고
     * When 지하철 구간 생성을 요청 하면
     * Then 지하철 구간 생성이 성공한다.
     */
    @DisplayName("지하철 구간 생성")
    @Test
    @Order(1)
    void createSection() {
        // given
        샘플_지하철역_생성_요청();
        String searchUri = 샘플1_지하철_노선_생성_요청();

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "2");
        params.put("downStationId", "3");
        params.put("distance", "30");

        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(searchUri + "/sections", params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철역 생성을 요청 하고
     * Given 새로운 지하철역 생성을 요청 하고
     * Given 지하철 구간 생성을 요청 하고
     * When 지하철역 목록 조회를 요청 하면
     * Then 두 지하철역이 포함된 지하철역 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록(line stations) 조회")
    @Test
    @Order(3)
    void getLineStation() {
        /// given
        샘플_지하철역_생성_요청();
        String searchUri = 샘플1_지하철_노선_생성_요청();

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "2");
        params.put("downStationId", "3");
        params.put("distance", "30");
        지하철_구간_생성_요청(searchUri + "/sections", params);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> {
                    List<LineResponse> responseList = response.jsonPath().getList("", LineResponse.class);
                    assertThat(responseList.size()).isEqualTo(1);
                }
        );
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * Given 구간 생성을 요청 하고
     * When 구간 삭제를 요청 하면
     * Then 생성한 구간 삭제가 성공한다.
     */
    @DisplayName("지하철 구간 삭제")
    @Test
    @Order(5)
    void deleteSection() {
        /// given
        샘플_지하철역_생성_요청();
        String searchUri = 샘플1_지하철_노선_생성_요청();

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "2");
        params.put("downStationId", "3");
        params.put("distance", "30");
        지하철_구간_생성_요청(searchUri + "/sections", params);

        // when
        String uri = searchUri + "/sections?stationId=3";
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    @DisplayName("지하철 구간 생성 에러 - 등록할 상행역은 현재 등록되어있는 하행")
    @Test
    @Order(7)
    void createSectionError1() {
        // given
        샘플_지하철역_생성_요청();
        String searchUri = 샘플1_지하철_노선_생성_요청();

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "3");
        params.put("downStationId", "4");
        params.put("distance", "30");

        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(searchUri + "/sections", params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 구간 생성 에러 - 하행역은 현재 등록되어있는 역일 수 없다")
    @Test
    @Order(9)
    void createSectionError2() {
        // given
        샘플_지하철역_생성_요청();
        String searchUri = 샘플1_지하철_노선_생성_요청();

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "2");
        params.put("downStationId", "1");
        params.put("distance", "30");

        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(searchUri + "/sections", params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 구간 삭제 - 노선에 등록된 마지막 역(하행 종점역)만 제거")
    @Test
    @Order(11)
    void deleteSectionError1() {
        /// given
        샘플_지하철역_생성_요청();
        String searchUri = 샘플1_지하철_노선_생성_요청();

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "2");
        params.put("downStationId", "3");
        params.put("distance", "30");
        지하철_구간_생성_요청(searchUri + "/sections", params);

        // when
        String uri = STATION_END_POINT + "/1";
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @DisplayName("지하철 구간 삭제 - 구간이 1개인 경우 삭제할 수 없다")
    @Test
    @Order(13)
    void deleteSectionError2() {
        /// given
        샘플_지하철역_생성_요청();
        String searchUri = 샘플1_지하철_노선_생성_요청();

        // when
        String uri = searchUri + "/sections?stationId=1";
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역 삭제 - 구간에서 사용중이면 삭제할 수 없다")
    @Test
    @Order(15)
    void deleteStationError1() {
        /// given
        샘플_지하철역_생성_요청();
        String searchUri = 샘플1_지하철_노선_생성_요청();

        // when
        String uri = searchUri + "/sections?stationId=1";
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }



    private String 샘플1_지하철_노선_생성_요청() {
        LineRequest lineRequest = new LineRequest("2호선", "연두색", 1L, 2L, 30);
        ExtractableResponse<Response> givenResponse = 지하철_노선_생성_요청(lineRequest);
        return givenResponse.header("Location");
    }

    private void 샘플_지하철역_생성_요청() {
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "사당역");
        지하철역_생성_요청(params1);
        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "강남역");
        지하철역_생성_요청(params2);
        Map<String, String> params3 = new HashMap<>();
        params3.put("name", "잠실역");
        지하철역_생성_요청(params3);
        Map<String, String> params4 = new HashMap<>();
        params4.put("name", "왕심리");
        지하철역_생성_요청(params4);
    }


    private ExtractableResponse<Response> 지하철_구간_생성_요청(String uri, Map<String, String> map) {
        return AssuredRequest.doCreate(uri, map);
    }

    private ExtractableResponse<Response> 지하철_구간_삭제_요청(String uri) {
        return AssuredRequest.doDelete(uri);
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest lineRequest) {
        return AssuredRequest.doCreate(LINE_END_POINT, lineRequest);
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return AssuredRequest.doFind(LINE_END_POINT);
    }

    private ExtractableResponse<Response> 지하철역_생성_요청(Map<String, String> map) {
        return AssuredRequest.doCreate(STATION_END_POINT, map);
    }
}
