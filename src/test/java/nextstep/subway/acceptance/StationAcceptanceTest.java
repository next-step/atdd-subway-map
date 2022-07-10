package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    public static final String CLEAN_UP_TABLE = "station";

    @Autowired
    private CleanUpSchema cleanUpSchema;

    @Override
    protected void preprocessing() {
        cleanUpSchema.execute(CLEAN_UP_TABLE);
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = saveStation(Map.of("name", "강남역"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = getStationNames(findStations());
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        saveStation(Map.of("name", "낙성대역"));
        saveStation(Map.of("name", "구로디지털단지역"));

        // when
        ExtractableResponse<Response> response = findStations();

        // then
        List<String> names = getStationNames(response);
        assertThat(names).isEqualTo(List.of("낙성대역", "구로디지털단지역"));
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
        ExtractableResponse<Response> saveResponse = saveStation(Map.of("name", "서울대입구역"));

        // when
        Integer id = saveResponse.body().jsonPath().get("id");
        ExtractableResponse<Response> deleteResponse = RestAssured
                .given().log().all()
                .when().delete("/stations/{id}", id)
                .then().log().all()
                .extract();
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        List<String> names = getStationNames(findStations());
        assertThat(names.contains("서울대입구역")).isFalse();
    }

    /**
     * 지하철역 생성 API 호출
     * @param params
     * @return
     */
    private ExtractableResponse<Response> saveStation(Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    /**
     * 지하철역 목록 조회 API 호출
     * @return
     */
    private ExtractableResponse<Response> findStations() {
        return RestAssured
                .given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    /**
     * Response에서 지하철역 이름 목록 추출
     * @param response
     * @return
     */
    private List<String> getStationNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name", String.class);
    }

}