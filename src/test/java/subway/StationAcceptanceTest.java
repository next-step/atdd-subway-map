package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.controller.dto.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        // when
        ExtractableResponse<Response> createResponse = createStation(params);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> findResponse = findStation();
        List<String> stationsNames = findResponse.jsonPath().getList("name", String.class);
        assertThat(stationsNames).hasSize(1)
                .containsExactly("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void selectStation() {
        // given
        Map<String, String> gangNamStationParams = new HashMap<>();
        gangNamStationParams.put("name", "강남역");
        ExtractableResponse<Response> gangnamStationCreateResponse = createStation(gangNamStationParams);
        assertThat(gangnamStationCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // given
        Map<String, String> seollEungStationParams = new HashMap<>();
        seollEungStationParams.put("name", "선릉역");
        ExtractableResponse<Response> seollEungStationCreateResponse = createStation(seollEungStationParams);
        assertThat(seollEungStationCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> findResponse = findStation();
        List<String> stationsNames = findResponse.jsonPath().getList("name", String.class);

        // then
        assertThat(stationsNames).hasSize(2)
                .containsExactly("강남역", "선릉역");
    }


    /**
     * Given N개의 지하철역을 생성하고
     * When 조회할 N개의 지하철역을 선택하면
     * Then N개의 지하철역을 응답 받는다
     */
    @DisplayName("선택한 N개의 지하철역을 조회한다.")
    @Test
    void findStationBy() {
        // given
        Map<String, String> gangNamStationParams = new HashMap<>();
        gangNamStationParams.put("name", "강남역");
        ExtractableResponse<Response> gangnamStationCreateResponse = createStation(gangNamStationParams);
        StationResponse gangnamStationResponse = gangnamStationCreateResponse.as(StationResponse.class);
        assertThat(gangnamStationCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> findResponse = findStationBy(List.of(gangnamStationResponse.getId()));
        List<String> stationsNames = findResponse.jsonPath().getList("name", String.class);

        // then
        assertThat(stationsNames).hasSize(1)
                .containsExactly("강남역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void removeStation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        ExtractableResponse<Response> createResponse = createStation(params);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        StationResponse stationResponse = createResponse.as(StationResponse.class);

        // when
        deleteStation(stationResponse.getId());

        // then
        ExtractableResponse<Response> findResponse = findStation();
        List<String> stationsNames = findResponse.jsonPath().getList("name", String.class);
        assertThat(stationsNames).isEmpty();
    }

    private ExtractableResponse<Response> createStation(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> findStation() {
        return RestAssured.given().log().all()
                .when().get("/stations/all")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> findStationBy(List<Long> ids) {
        RequestSpecification request = RestAssured.given().log().all();

        for (Long id : ids) {
            request.param("ids", id);
        }

        return request
                .when().get("/stations/filter")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteStation(Long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/{id}", id)
                .then().log().all()
                .extract();
    }

}
