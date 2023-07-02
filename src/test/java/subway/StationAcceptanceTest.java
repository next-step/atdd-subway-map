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
        final String stationName = "강남역";
        ExtractableResponse<Response> response = createStation(stationName);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = getStationNames();
        assertThat(stationNames).containsAnyOf("강남역");
    }

    private static List<String> getStationNames() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }

    private static ExtractableResponse<Response> createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록 조회 인수 테스트")
    @Test
    void selectStationTest() {
        // given
        final String station1 = "사가정역";
        final String station2 = "삼성역";
        ExtractableResponse<Response> createStation1 = createStation(station1);
        ExtractableResponse<Response> createStation2 = createStation(station2);

        assertThat(createStation1.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createStation2.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<String> expectedStations = List.of(station1, station2);

        // when
        List<String> actualStations = getStationNames();

        // then
        assertThat(expectedStations.size()).isEqualTo(actualStations.size());
        assertThat(expectedStations).isEqualTo(actualStations);

    }
    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 삭제 테스트")
    @Test
    void deleteStationTest() {
        // given
        final String station = "삼성역";
        ExtractableResponse<Response> createStation = createStation(station);
        assertThat(createStation.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Long stationId = createStation.body().jsonPath().getObject("id", Long.class);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("id", stationId)
                .when().delete("/stations/{id}")
                .then().log().all()
                .extract();

        // then
        assertThat(getStationNames()).doesNotContain("stations");
    }
}