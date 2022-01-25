package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.acceptance.StationSteps.*;
import static nextstep.subway.utils.RestAssuredRequest.delete;
import static nextstep.subway.utils.RestAssuredRequest.get;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {
    private static final String STATIONS_PATH = "/stations";

    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        지하철역_생성됨(response);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * Given 새로운 지하철역 생성을 요청 하고
     * When 지하철역 목록 조회를 요청 하면
     * Then 두 지하철역이 포함된 지하철역 목록을 응답받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void getStations() {
        /// given
        지하철역_등록되어_있음(강남역);
        지하철역_등록되어_있음(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_목록_조회_요청();

        // then
        지하철역_목록_조회됨(response, 강남역, 역삼역);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 생성한 지하철역 삭제를 요청 하면
     * Then 생성한 지하철역 삭제가 성공한다.
     */
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = 지하철역_등록되어_있음(강남역);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철역_삭제_요청(uri);

        // then
        지하철역_삭제됨(response);
    }

    /**
     * Scenario: 중복이름으로 지하철역 생성
     * Given 지하철역 생성을 요청하고
     * When 같은 이름으로 지하철역 생성을 요청하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("지하철역 중복이름 생성")
    @Test
    void createStationWithDuplicateName() {
        // given
        지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        지하철역_이름_중복됨(response);
    }

    private ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return get(STATIONS_PATH);
    }

    private ExtractableResponse<Response> 지하철역_삭제_요청(String uri) {
        return delete(uri);
    }

    private ExtractableResponse<Response> 지하철역_등록되어_있음(Map<String, String> params) {
        ExtractableResponse<Response> response = 지하철역_생성_요청(params);
        지하철역_생성됨(response);

        return response;
    }

    private void 지하철역_생성됨(ExtractableResponse<Response> response) {
        응답_요청_확인(response, HttpStatus.CREATED);
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 지하철역_목록_조회됨(ExtractableResponse<Response> response, Map<String, String> ... params) {
        응답_요청_확인(response, HttpStatus.OK);
        List<String> stationNames = response.jsonPath().getList("name");
        List<String> names = Arrays.stream(params)
                .map(param -> param.get("name"))
                .collect(Collectors.toList());
        assertThat(stationNames).isEqualTo(names);
    }

    private void 지하철역_삭제됨(ExtractableResponse<Response> response) {
        응답_요청_확인(response, HttpStatus.NO_CONTENT);
    }

    private void 지하철역_이름_중복됨(ExtractableResponse<Response> response) {
        응답_요청_확인(response, HttpStatus.CONFLICT);
    }

    private void 응답_요청_확인(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
}
