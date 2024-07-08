package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    @BeforeEach
    void setUp() {
        deleteAllStations();
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
        addStation("강남역");

        // then
        List<String> stationNames = getStationNames();
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void showStations() {
        // given
        addStation("강남역");
        addStation("망원역");

        // when
        List<String> stationNames = getStationNames();

        // then
        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).containsExactly("강남역", "망원역");

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
        ExtractableResponse<Response> response = addStation("강남역");

        // when
        deleteStation(response.body().jsonPath().getString("id"));

        // then
        List<String> stationNames = getStationNames();
        assertThat(stationNames).hasSize(0);
        assertThat(stationNames).doesNotContain("강남역");
    }

    private static ExtractableResponse<Response> addStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }

    private static List<String> getStationNames() {
        return RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract().jsonPath().getList("name", String.class);
    }

    private void deleteStation(String stationId) {
        RestAssured.given().log().all()
            .when().delete("/stations/" + stationId)
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private void deleteAllStations() {
        List<String> stationIds = RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract().jsonPath().getList("id", String.class);

        for (String stationId : stationIds) {
            RestAssured.given().log().all()
                .when().delete("/stations/" + stationId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
        }
    }


}