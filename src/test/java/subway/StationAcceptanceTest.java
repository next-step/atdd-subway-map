package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    @Autowired
    StationRepository stationRepository;

    @AfterEach
    void tearDown() {
        stationRepository.deleteAll();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStationTest() {
        // when
        ExtractableResponse<Response> response = createStation("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = getResponseStationNames();
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void showStationsTest() {
        // Given
        createStations("강남역", "서초역");

        // When
        List<String> responseStationNames = getResponseStationNames();

        // Then
        assertEquals(2, responseStationNames.size());
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 삭제한다.")
    @Test
    public void deleteStationTest() {
        // Given
        createStations("강남역", "서초역", "신촌역");

        // When
        Long stationId = getIdByStatioName("서초역");
        deleteStation(stationId);

        // Then
        List<String> responseStationNames = getResponseStationNames();
        assertThat(responseStationNames).containsOnly("신촌역", "강남역");
    }

    private void createStations(String... stationNames) {
        for (String stationName : stationNames) {
            createStation(stationName);
        }
    }
    private ExtractableResponse<Response> createStation(String stationName) {
        Map<String, String> params = Map.of("name", stationName);

        return given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations")
                .then().log().all()
                    .extract();
    }

    private void deleteStation(Long stationId) {
        given()
                .pathParam("id", stationId)
        .when()
                .delete("/stations/{id}");
    }

    private List<String> getResponseStationNames() {
        return when()
                    .get("/stations")
                .then()
                    .extract().jsonPath().getList("name", String.class);
    }

    private Long getIdByStatioName(String stationName) {
        return stationRepository.findAll().stream()
                .filter(entry -> entry.getName().equals(stationName))
                .findFirst()
                .orElseThrow()
                .getId();
    }
}