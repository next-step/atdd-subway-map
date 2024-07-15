package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        createStation("강남역");

        // then
        List<String> stationNames = getStationNames();

        // then
        assertThat(stationNames).containsExactlyInAnyOrder("강남역");

    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */

    @DisplayName("지하철역을 생성하고 목록을 조회하면 응답받는다.")
    @Test
    void getStations() {
        // given
        createStation("강남역");
        createStation("역삼역");

        // when
        List<String> stationNames = getStationNames();

        // then
        assertThat(stationNames).containsExactlyInAnyOrder("강남역", "역삼역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 생성하고 그 역을 삭제하면 목록 조회시 찾을 수 없다.")
    @Test
    void deleteStation() {
        // given
        long stationId = createStation("강남역");

        // when
        deleteStation(stationId);

        // then
        List<String> stationNames = getStationNames();
        assertThat(stationNames).doesNotContain("강남역");

    }


    static long createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract();

        return response.jsonPath().getLong("id");
    }

    private List<String> getStationNames() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }

    private static void deleteStation(long stationId) {
        RestAssured.given().log().all()
                .when().delete("/stations/" + stationId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }


}