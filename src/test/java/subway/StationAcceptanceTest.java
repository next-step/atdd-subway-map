package subway;

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

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest{

    private final String 강남역 = "강남역";
    private final String 역삼역 = "역삼역";


    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성(강남역);

        // then
        ExtractableResponse<Response> getStationRes = 지하철역_조회();
        역_생성_여부_검증(response, getStationRes);
    }


    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void selectStations() {
        // given
        지하철역_생성(강남역);
        지하철역_생성(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_조회();

        // then
        지하철_역_개수_검증(response, 2);
    }

    private static void 지하철_역_개수_검증(ExtractableResponse<Response> response, int targetCount) {
        List<String> stationNames = response.jsonPath().getList("name", String.class);

        assertThat(stationNames.size()).isEqualTo(targetCount);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> gangnamStationCreationRes = 지하철역_생성(강남역);
        지하철역_생성(역삼역);

        String stationUrl = gangnamStationCreationRes.response().getHeader("Location");

        // when
        지하철역_삭제(stationUrl);

        // then
        역_삭제_여부_검증(gangnamStationCreationRes, 지하철역_조회());
    }

    private void 역_생성_여부_검증(ExtractableResponse<Response> creationRes, ExtractableResponse<Response> getStationRes) {
        String 생성된_역 = creationRes.jsonPath().getString("name");
        List<String> stationNames = getStationRes.jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf(생성된_역);
    }

    private static void 역_삭제_여부_검증(ExtractableResponse<Response> creationRes, ExtractableResponse<Response> getStationRes) {
        String 삭제된_역 = creationRes.jsonPath().getString("name");

        assertThat(getStationRes.jsonPath().getList("name")).doesNotContain(삭제된_역);
    }

    private static void 지하철역_삭제(String stationUrl) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(stationUrl)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private static ExtractableResponse<Response> 지하철역_생성(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }

    private static ExtractableResponse<Response> 지하철역_조회() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }
}