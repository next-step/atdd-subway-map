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

    private static final String GANGNAM_STATION_NAME = "강남역";
    private static final String YEOKSAM_STATION_NAME = "역삼역";

    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // given
        Map<String, String> params = createGangnamStation();

        // when
        ExtractableResponse<Response> response = callCreateStation(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철역 생성
     * When 같은 이름으로 지하철역 생성
     * Then 400 status code를 응답한다.
     */
    @DisplayName("중복 이름의 지하철역을 생성하면 실패한다")
    @Test
    void createStation_duplicate_fail() {
        // given
        Map<String, String> params = createGangnamStation();
        callCreateStation(params);

        // when
        ExtractableResponse<Response> response = callCreateStation(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
        Map<String, String> gangnamStationParams = createGangnamStation();
        callCreateStation(gangnamStationParams);

        Map<String, String> yeoksamStationParams = createYeoksamStation();
        callCreateStation(yeoksamStationParams);

        // when
        ExtractableResponse<Response> response = callFindAllStation();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).contains(GANGNAM_STATION_NAME, YEOKSAM_STATION_NAME);
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
        Map<String, String> gangnamStationParams = createGangnamStation();
        ExtractableResponse<Response> createResponse = callCreateStation(gangnamStationParams);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = callDeleteStation(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> callCreateStation(Map<String, String> StationParams) {
        return RestAssured.given().log().all()
            .body(StationParams)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> callFindAllStation() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when()
            .get("/stations")
            .then().log().all()
            .extract();
        return response;
    }

    private ExtractableResponse<Response> callDeleteStation(String uri) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when()
            .delete(uri)
            .then().log().all()
            .extract();
        return response;
    }

    private Map<String, String> createGangnamStation() {
        Map<String, String> result = new HashMap();
        result.put("name", GANGNAM_STATION_NAME);

        return result;
    }

    private Map<String, String> createYeoksamStation() {
        Map<String, String> result = new HashMap();
        result.put("name", YEOKSAM_STATION_NAME);

        return result;
    }

}
