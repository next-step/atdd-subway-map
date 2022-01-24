package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청("강남역");

        // then
        지하철역_생성_완료(response);
        assertThat(response.header("Location")).isNotBlank();
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
        String 강남역 = "강남역";
        String 역삼역 = "역삼역";
        지하철역_생성_요청(강남역);
        지하철역_생성_요청(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_조회_요청("/stations");

        지하철역_조회_완료(response);
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).contains(강남역, 역삼역);
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
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청("강남역");

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철역_삭제_요청(uri);

        // then
        지하철역_삭제_완료(response);
    }

    /**
     * Scenario: 중복이름으로 지하철역 생성
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철역 생성")
    @Test
    void createStationWithDuplicateName() {
        // given
        String 강남역 = "강남역";
        지하철역_생성_요청(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        지하철역_생성_실패(response);
    }

    private ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_조회_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_삭제_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    private void 지하철역_응답_상태_검증(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    private void 지하철역_생성_완료(ExtractableResponse<Response> response) {
        지하철역_응답_상태_검증(response, HttpStatus.CREATED);
    }

    private void 지하철역_조회_완료(ExtractableResponse<Response> response) {
        지하철역_응답_상태_검증(response, HttpStatus.OK);
    }

    private void 지하철역_삭제_완료(ExtractableResponse<Response> response) {
        지하철역_응답_상태_검증(response, HttpStatus.NO_CONTENT);
    }

    private void 지하철역_생성_실패(ExtractableResponse<Response> response) {
        지하철역_응답_상태_검증(response, HttpStatus.BAD_REQUEST);
    }
}
