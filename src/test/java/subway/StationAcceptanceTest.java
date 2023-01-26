package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
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
        // When
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response =
                given()
                    .log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations")
                .then()
                    .log().all()
                    .extract();

        // Then
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.CREATED.value());

        // Then
        List<String> stationNames =
                given()
                    .log().all()
                .when()
                    .get("/stations")
                .then()
                    .log().all()
                    .extract().jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("지하철역 목록을 조회한다.")
    void searchStation() {
        // Given
        List<String> givenStationNames = List.of("강남역", "선릉역");
        givenStationNames.forEach(this::createStation);

        // When
        ExtractableResponse<Response> response =
            given()
                .log().all()
            .when()
                .get("/stations")
            .then()
                .log().all()
                .extract();

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
        String givenStationName = "강남역";
        String location = createStation(givenStationName);

        // When
        ExtractableResponse<Response> response =
            given()
                .log().all()
            .when()
                .delete(location)
            .then()
                .log().all()
                .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // Then
        assertThat(findAllStationNames()).doesNotContain(givenStationName);
    }

    private String createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        ExtractableResponse<Response> response =
            given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post("/stations")
            .then()
                .log().all()
                .extract();

        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.CREATED.value());

        return response.header(HttpHeaders.LOCATION);
    }

    private List<String> findAllStationNames() {
        ExtractableResponse<Response> response =
            given()
                .log().all()
            .when()
                .get("/stations")
            .then()
                .log().all()
                .extract();
        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.OK.value());

        return response.jsonPath().getList("name");
    }
}
