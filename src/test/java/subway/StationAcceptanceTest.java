package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.web.response.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends BaseAcceptance {

    private static final String 강남역 = "강남역";
    private static final String 논현역 = "논현역";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void 지하철역을_생성하고_확인한다() {
        // when
        지하철역을_생성하고_확인한다(강남역);

        // then
        지하철_목록_조회_시_생성한_역을_찾을_수_있다(강남역);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록조회 할 수 있다")
    @Test
    void getStations() {
        // Given
        지하철역을_생성하고_확인한다(강남역);
        지하철역을_생성하고_확인한다(논현역);

        // When
        ExtractableResponse<Response> response = 지하철_목록_조회();

        // then
        생성한_지하철_목록을_응답_받는다(response, 강남역, 논현역);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제 할 수 있다")
    @Test
    void deleteStation() {
        // Given
        StationResponse 강남역_응답 = 지하철역을_생성하고_확인한다(강남역);

        // When
        ExtractableResponse<Response> deleteResponse = 지하철역을_삭제한다(강남역_응답);

        // then
        지하철역_목록_조회_시_생성한_역을_찾을_수_없다(deleteResponse);
    }

    private static void 지하철역_목록_조회_시_생성한_역을_찾을_수_없다(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> response = 지하철_목록_조회();
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationNames).isEmpty();
    }

    private static ExtractableResponse<Response> 지하철역을_삭제한다(StationResponse stationResponse) {
        return RestAssured.given().spec(REQUEST_SPEC).log().all()
            .pathParam("stationId", stationResponse.getId())
            .when().delete("/stations/{stationId}")
            .then().log().all()
            .extract();
    }

    private static void 생성한_지하철_목록을_응답_받는다(ExtractableResponse<Response> response, String upStation, String downStation) {
        List<String> stationNames = 지하철_역_조회(response);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationNames).containsExactlyInAnyOrder(upStation, downStation);
    }

    private static StationResponse 지하철역을_생성하고_확인한다(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .body(params)
            .when().post("/stations")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(StationResponse.class);
    }

    private static void 지하철_목록_조회_시_생성한_역을_찾을_수_있다(String name) {
        ExtractableResponse<Response> response = 지하철_목록_조회();
        List<String> stationNames = 지하철_역_조회(response);
        assertThat(stationNames).containsAnyOf(name);
    }

    private static List<String> 지하철_역_조회(ExtractableResponse<Response> stations) {
        return stations.jsonPath().getList("name", String.class);
    }

    private static ExtractableResponse<Response> 지하철_목록_조회() {
        return RestAssured.given().spec(REQUEST_SPEC).log().all()
            .when().get("/stations")
            .then().log().all()
            .extract();
    }

}
