package subway;

import static org.assertj.core.api.Assertions.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTest {

    private static final String GANGNAM_STATION = "강남역";
    private static final String JAMSIL_STATION = "잠실역";
    private static final String SAMSUNG_STATION = "삼성역";
    private static final String SEOUL_FOREST_STATION = "서울숲";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = createStation(GANGNAM_STATION);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = getAllStationNames();
        assertThat(stationNames).containsAnyOf(GANGNAM_STATION);
    }

    private ExtractableResponse<Response> createStation(String name) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);

        return RestAssured
            .given()
                .log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post(URI.create("/stations"))
            .then()
                .log().all()
            .extract();
    }

    private List<String> getAllStationNames() {
        return RestAssured
            .given()
                .log().all()
            .when()
                .get("/stations")
            .then()
                .log().all()
            .extract()
                .jsonPath().getList("name", String.class);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        // given
        List<String> newStationNames = List.of(JAMSIL_STATION, SAMSUNG_STATION);
        newStationNames.forEach(this::createStation);

        // when
        List<String> stationNames = getAllStationNames();

        // then
        assertThat(stationNames).containsAll(newStationNames);
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
        ExtractableResponse<Response> createResponse = createStation(SEOUL_FOREST_STATION);
        long stationId = createResponse.body().jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> deleteResponse = deleteStation(stationId);

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        List<String> stationNames = getAllStationNames();
        assertThat(stationNames).doesNotContain(SEOUL_FOREST_STATION);
    }

    private ExtractableResponse<Response> deleteStation(long stationId) {
        return RestAssured
            .given()
                .log().all()
            .when()
                .delete("/stations/{id}", stationId)
            .then()
                .log().all()
            .extract();
    }

}
