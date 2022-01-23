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
        // given
        Map<String, String> params = 파라미터_생성("강남역");

        // when
        ExtractableResponse<Response> response = 지하철역_생성_API(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     * @see nextstep.subway.ui.StationController#createStation
     */
    @Test
    void 지하철역_이름_중복_생성_방지_테스트() {
        // given
        Map<String, String> params = 파라미터_생성("강남역");
        지하철역_생성_API(params);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_API(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
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
        Map<String, String> params1 = 파라미터_생성(강남역);
        지하철역_생성_API(params1);

        String 역삼역 = "역삼역";
        Map<String, String> params2 = 파라미터_생성(역삼역);
        지하철역_생성_API(params2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_전체_리스트_조회_API();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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
        Map<String, String> params = 파라미터_생성("강남역");
        ExtractableResponse<Response> createResponse = 지하철역_생성_API(params);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철_노선_삭제_API(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    Map<String, String> 파라미터_생성(String 지하철역명) {
        Map<String, String> params = new HashMap<>();
        params.put("name", 지하철역명);

        return params;
    }

    ExtractableResponse<Response> 지하철역_생성_API(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> 지하철_노선_전체_리스트_조회_API() {
        return RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> 지하철_노선_삭제_API(String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }
}
