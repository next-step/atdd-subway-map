package subway.station;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class StationAcceptanceTest {

    public static final String GANGNAM_STATION = "강남역";
    public static final String SEOLLEUNG_STATION = "선릉역";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // When
        final String givenStationName = GANGNAM_STATION;
        createStation(givenStationName);

        // Then
        List<String> stationNames = findAllStationNames();

        assertThat(stationNames)
            .containsAnyOf(givenStationName);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("지하철역 목록을 조회한다.")
    void searchStations() {
        // Given
        List<String> givenStationNames = List.of(GANGNAM_STATION, SEOLLEUNG_STATION);
        givenStationNames.forEach(StationAcceptanceTest::createStation);

        // When
        ExtractableResponse<Response> response = findAllStations();

        // Then
        List<String> stationsNames = response.jsonPath().getList("name", String.class);
        assertThat(stationsNames)
            .hasSize(2)
            .containsAll(givenStationNames);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    @DisplayName("지하철역을 삭제한다.")
    void deleteStation() {
        // Given
        String givenStationName = GANGNAM_STATION;
        Long id = createStation(givenStationName);

        // When
        ExtractableResponse<Response> response = deleteStation(id);

        // Then
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.NO_CONTENT.value());

        // Then
        assertThat(findAllStationNames())
            .doesNotContain(givenStationName);
    }

    public static Long createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        ExtractableResponse<Response> response =
            given()
                .body(params)
                .contentType(ContentType.JSON)
            .when()
                .post("/stations")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return response.jsonPath().getLong("id");
    }

    public static List<String> findAllStationNames() {
        return findAllStations()
            .jsonPath().getList("name");
    }

    public static ExtractableResponse<Response> findAllStations() {
        return RestAssured.
            given()
            .when()
                .get("/stations")
            .then()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> deleteStation(Long stationId) {
        return RestAssured.
            given()
            .when()
                .delete("/stations/{id}", stationId)
            .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }
}
