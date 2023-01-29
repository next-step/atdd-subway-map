package subway;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    private static final String STATIONS_URL = "/stations";
    private static final String TEST_STATION1_NAME = "지하철역1";
    private static final String TEST_STATION2_NAME = "지하철역2";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = createStation(TEST_STATION1_NAME);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = getStations()
            .jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(TEST_STATION1_NAME);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 2개를 생성한다.")
    @Test
    void createTwoStations() {
        // given
        createStation(TEST_STATION1_NAME);
        createStation(TEST_STATION2_NAME);

        // when
        ExtractableResponse<Response> response = getStations();

        // then
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).containsAll(List.of(TEST_STATION1_NAME, TEST_STATION2_NAME));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        long stationId = createStation(TEST_STATION1_NAME).jsonPath().getLong("id");

        // when
        deleteStation(stationId);

        // then
        List<String> stationNames = getStations().jsonPath().getList("name", String.class);
        assertThat(stationNames).isEmpty();
    }

    private ExtractableResponse<Response> createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(STATIONS_URL)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> getStations() {
        return RestAssured.given().log().all()
            .when().get(STATIONS_URL)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> deleteStation(long stationId) {
        return RestAssured.given().log().all()
            .when().delete(STATIONS_URL + "/" + stationId)
            .then().log().all()
            .extract();
    }
}
