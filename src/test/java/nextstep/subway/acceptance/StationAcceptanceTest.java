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

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends BaseAcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStationTest() {
        // when
        Map<String, String> params = getRequestBody("강남역");
        ExtractableResponse<Response> createdStation = createStation(params);

        // then
        assertThat(createdStation.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = getStations().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }


    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStationsTest() {
        //given
        Map<String, String> requestBody1 = getRequestBody("가양역");
        Map<String, String> requestBody2 = getRequestBody("증미역");

        //when
        createStation(requestBody1);
        createStation(requestBody2);

        //then
        ExtractableResponse<Response> stations = getStations();
        List<String> stationNames = stations.jsonPath().getList("name", String.class);

        assertThat(stations.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationNames)
            .hasSize(2)
            .containsAnyOf("가양역", "증미역");

    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStationTest() {
        //given
        Map<String, String> requestBody = getRequestBody("등촌역");
        ExtractableResponse<Response> createdStation = createStation(requestBody);

        //when
        int deleteId = createdStation.jsonPath().getInt("id");
        ExtractableResponse<Response> response = deleteStation(deleteId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        ExtractableResponse<Response> stations = getStations();
        List<String> stationNames = getStations().jsonPath().getList("name", String.class);

        assertThat(stations.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationNames).doesNotContain("등촌역");
    }


    private Map<String, String> getRequestBody(String station) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", station);
        return requestBody;
    }

    private ExtractableResponse<Response> createStation(Map<String, String> params) {
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> getStations() {
        return RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> deleteStation(int deleteId) {
        return RestAssured.given().log().all()
            .when().delete("/stations/{id}", deleteId)
            .then().log().all()
            .extract();
    }

}
