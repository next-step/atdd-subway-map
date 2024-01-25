package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

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
        final ExtractableResponse<Response> response = createStation("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        final StationResponse stationResponse = response.as(StationResponse.class);
        assertThat(stationResponse.getName()).isEqualTo("강남역");

        // then
        final ExtractableResponse<Response> stationsResponse = getStations();
        final List<String> stationNames = stationsResponse.jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 3개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void fetchStationsTest() {
        // given
        final List<String> targetStations = List.of("지하철역이름", "새로운지하철역이름", "또다른지하철역이름");
        targetStations.forEach(this::createStation);

        // when
        final ExtractableResponse<Response> response = getStations();

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            final List<String> stationNames = response.jsonPath().getList("name", String.class);
            softly.assertThat(stationNames).containsAll(List.of("지하철역이름", "새로운지하철역이름", "또다른지하철역이름"));
        });
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStationTest() {
        // given
        final List<String> targetStations = List.of("지하철역이름", "새로운지하철역이름", "또다른지하철역이름");
        targetStations.forEach(this::createStation);

        // when
        final ExtractableResponse<Response> response = deleteStationById(1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            final ExtractableResponse<Response> stationsResponse = getStations();
            final List<String> stationNames = stationsResponse.jsonPath().getList("name", String.class);
            softly.assertThat(stationNames).doesNotContain("지하철역이름");
        });
    }

    private ExtractableResponse<Response> getStations() {
        return RestAssured
                .given()
                .when().get("/stations")
                .then().extract();
    }

    private ExtractableResponse<Response> createStation(final String stationName) {
        return RestAssured
                .given()
                .body(Map.of("name", stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().extract();
    }

    private ExtractableResponse<Response> deleteStationById(final Long id) {
        return RestAssured
                .given().pathParam("id", id)
                .when().delete("/stations/{id}")
                .then().extract();
    }

}
