package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        stationRepository.truncateTableStation();
    }

    /**
     * When 지하철역을 생성하면 Then 지하철역이 생성된다 Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response =
                givenLog()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = findStationsResponse()
                .jsonPath()
                .getList("name", String.class);

        assertThat(stationNames).containsAnyOf("강남역");
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void findStations() {
        // given
        createPersistenceStationBy("강남역", "양재역");

        // when
        ExtractableResponse<Response> response = findStationsResponse();

        // Then
        List<StationResponse> responseResult = response
                .jsonPath()
                .getList("$", StationResponse.class);

        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(responseResult.size()).isEqualTo(2)
        );
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        String NotContainStationName = "강남역";
        createPersistenceStationBy(NotContainStationName);
        String ContainStationName = "양재역";
        createPersistenceStationBy(ContainStationName);

        // when
        ExtractableResponse<Response> deleteStationResponse = deleteStationResponseBy(1L);

        // Then
        List<String> stationsNameList = findStationsResponse()
                .jsonPath()
                .getList("name", String.class);

        Assertions.assertAll(
                () -> assertThat(deleteStationResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(stationsNameList).doesNotContain(NotContainStationName),
                () -> assertThat(stationsNameList).contains(ContainStationName)
        );
    }

    private ExtractableResponse<Response> deleteStationResponseBy(Long id) {
        return givenLog()
                .when()
                .delete("/stations/{id}", id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> findStationsResponse() {
        return givenLog()
                .when()
                .get("/stations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private static void createPersistenceStationBy(String... stationNames) {
        for (String stationName : stationNames) {
            Map<String, String> params = new HashMap<>();
            params.put("name", stationName);

            RestAssured.given()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/stations");
        }
    }

    private RequestSpecification givenLog() {
        return RestAssured
                .given().log().all();
    }
}
