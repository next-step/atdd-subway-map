package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.acceptance.StaticMethodUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void clearRepository(){
        databaseCleanup.execute();
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
        String stationName = "강남역";
        ExtractableResponse<Response> response = createStationWithName(stationName);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = getAllStations().jsonPath()
                .getList("name", String.class);
        assertThat(stationNames).containsAnyOf(stationName);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // Given
        List<String> stationNames = List.of("강남역", "서울대입구역");
        List<Long> createdStationIds = stationNames.stream()
                .map(name -> extractIdInResponse(createStationWithName(name)))
                .collect(Collectors.toList());

        // When
        ExtractableResponse<Response> getAllStationsResponse = getAllStations();

        // Then
        List<Long> getAllStationsIds = extractIdsInListTypeResponse(getAllStationsResponse);
        assertThat(getAllStationsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getAllStationsIds.size()).isEqualTo(createdStationIds.size());
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // Given
        String stationName = "강남역";
        Long createdStationId = extractIdInResponse(createStationWithName(stationName));

        // When
        ExtractableResponse<Response> deleteResponse = deleteStationWithId(createdStationId);

        // Then
        List<Long> ids = extractIdsInListTypeResponse(getAllStations());
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(ids).doesNotContain(createdStationId);
    }

    private ExtractableResponse<Response> getAllStations() {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .get("/stations")
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> deleteStationWithId(Long createdStationId) {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/stations/{id}", createdStationId)
                .then()
                .log()
                .all()
                .extract();
    }
}