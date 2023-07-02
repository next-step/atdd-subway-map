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

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStationTest() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> getStationRes = getStations();
        List<String> stationNames = getStationRes.jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void selectStationsTest() {
        // given
        createStation("강남역");
        createStation("역삼역");

        // when
        ExtractableResponse<Response> response = getStations();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        List<String> stationNames = response.jsonPath().getList("name", String.class);

        assertThat(stationNames.size()).isEqualTo(2);
    }



    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStationTest() {
        // given
        ExtractableResponse<Response> gangnamStationCreationRes = createStation("강남역");
        ExtractableResponse<Response> yeoksamStationCreationRes = createStation("역삼역");

        String createdId = gangnamStationCreationRes.jsonPath().getString("id");

        // when
        RestAssured
            .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .delete(String.format("/stations/%s", createdId))
            .then().log().all()
            .extract();

        // then
        ExtractableResponse<Response> getRes = getStations();
        String deletedStation = gangnamStationCreationRes.jsonPath().getString("name");

        assertThat(getRes.jsonPath().getList("name")).doesNotContain(deletedStation);
    }

    private static ExtractableResponse<Response> createStation(String stationName) {
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
        return response;
    }

    private static ExtractableResponse<Response> getStations() {
        ExtractableResponse<Response> getRes = RestAssured
            .given().log().all()
            .when()
                .get("/stations")
            .then().log().all()
            .extract();
        return getRes;
    }

}