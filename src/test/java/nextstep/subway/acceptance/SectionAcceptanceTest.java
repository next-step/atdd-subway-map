package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.section.application.dto.SectionLineResponse;
import nextstep.subway.section.application.manager.LineData;
import nextstep.subway.utils.AssuredRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철역 관리 기능")
class SectionAcceptanceTest extends AcceptanceTest {

    private static final String END_POINT = "/stations";
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
    void createSection() {
        // given
        LineRequest lineRequest = 노선_요청_정보_샘플1();
        ExtractableResponse<Response> givenResponse = 지하철_노선_생성_요청(lineRequest);
        String searchUri = givenResponse.header("Location");

        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "사당역");
        지하철역_생성_요청(params1);
        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "신도림역");
        지하철역_생성_요청(params2);

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "1");
        params.put("downStationId", "2");
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
    @DisplayName("지하철 노선 목록(역포함) 조회")
    @Test
    void getLineStation() {
        /// given
        LineRequest lineRequest = 노선_요청_정보_샘플1();
        ExtractableResponse<Response> givenResponse = 지하철_노선_생성_요청(lineRequest);
        String searchUri = givenResponse.header("Location");

        LineRequest lineRequest2 = 노선_요청_정보_샘플2();
        지하철_노선_생성_요청(lineRequest2);

        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "사당역");
        지하철역_생성_요청(params1);
        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "신도림역");
        지하철역_생성_요청(params2);

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "30");
        지하철_구간_생성_요청(searchUri + "/sections", params);

        List<LineRequest> requestList = Arrays.asList(lineRequest, lineRequest2);
        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> {
                    List<SectionLineResponse> responseList = response.jsonPath().getList("", SectionLineResponse.class);
                    assertThat(responseList.size()).isEqualTo(requestList.size());
                    assertThat(responseList.stream().map(SectionLineResponse::getName).collect(Collectors.toList()))
                            .isEqualTo(requestList.stream().map(LineRequest::getName).collect(Collectors.toList()));
                }
        );
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 생성한 지하철역 삭제를 요청 하면
     * Then 생성한 지하철역 삭제가 성공한다.
     */
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {
        /// given
        LineRequest lineRequest = 노선_요청_정보_샘플1();
        ExtractableResponse<Response> givenResponse = 지하철_노선_생성_요청(lineRequest);
        String searchUri = givenResponse.header("Location");

        LineRequest lineRequest2 = 노선_요청_정보_샘플2();
        지하철_노선_생성_요청(lineRequest2);

        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "사당역");
        지하철역_생성_요청(params1);
        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "신도림역");
        지하철역_생성_요청(params2);

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "30");
        지하철_구간_생성_요청("/sections", params);

        // when
        String uri = searchUri + "/sections?stationId=2";
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 지하철_구간_생성_요청(String uri, Map<String, String> map) {
        return AssuredRequest.doCreate(uri, map);
    }

    private ExtractableResponse<Response> 지하철_구간_삭제_요청(String uri) {
        return AssuredRequest.doDelete(uri);
    }


    private LineRequest 노선_요청_정보_샘플1() {
        return new LineRequest("3호선", "주황색");
    }
    private LineRequest 노선_요청_정보_샘플2() {
        return new LineRequest("4호선", "녹색");
    }
    private ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest lineRequest) {
        return AssuredRequest.doCreate(LINE_END_POINT, lineRequest);
    }
    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return AssuredRequest.doFind(LINE_END_POINT);
    }

    private Map 역_요청_정보_샘플1() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "신도림역");
        return params;
    }
    private Map 역_요청_정보_샘플2() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "사당역");
        return params;
    }
    private ExtractableResponse<Response> 지하철역_생성_요청(Map<String, String> map) {
        return AssuredRequest.doCreate(STATION_END_POINT, map);
    }

}
