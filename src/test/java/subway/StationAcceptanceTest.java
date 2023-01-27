package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        String stationName = "강남역";

        createStationResponse(stationName);

        // then
        List<String> stationNames = findStationsResponse()
                .jsonPath()
                .getList("name", String.class);

        assertThat(stationNames).containsAnyOf(stationName);
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void findStations() {
        // given
        String[] expectedStationNames = {"강남역", "양재역"};

        createStationResponses(expectedStationNames);

        // when
        ExtractableResponse<Response> response = findStationsResponse();

        // Then
        List<String> stationNames = response
                .jsonPath()
                .getList("name", String.class);

        Assertions.assertAll(
                () -> assertThat(stationNames.size()).isEqualTo(expectedStationNames.length),
                () -> assertThat(stationNames).contains(expectedStationNames)
        );
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        Long deleteStationId = 1L;

        createStationResponses("강남역", "양재역");

        // when
        deleteStationResponseBy(deleteStationId);

        // Then
        List<Long> stationIds = findStationsResponse()
                .jsonPath()
                .getList("id", Long.class);

        assertThat(stationIds).doesNotContain(deleteStationId);
    }

    private ExtractableResponse<Response> deleteStationResponseBy(Long id) {
        return givenLog()
                .when()
                .delete("/stations/{id}", id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
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

    private List<ExtractableResponse<Response>> createStationResponses(final String... stationNames) {
        return Arrays.stream(stationNames)
                .map(this::createStationResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    private ExtractableResponse<Response> createStationResponse(final String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return givenLog()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private RequestSpecification givenLog() {
        return RestAssured
                .given().log().all();
    }
}
