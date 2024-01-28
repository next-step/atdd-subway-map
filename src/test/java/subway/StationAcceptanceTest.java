package subway;

import io.restassured.RestAssured;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import subway.fixture.StationFixture;
import subway.station.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(value = "/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStationSuccess() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        final var response = RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();

        final var stationNames = StationFixture.getStations().stream()
            .map(StationResponse::getName)
            .collect(Collectors.toList());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStationsSuccess() {
        // given
        final var createdStations = List.of(
            StationFixture.createStation("선릉역"),
            StationFixture.createStation("청계산입구역")
        );

        // when
        final var response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/stations")
            .then().log().all()
            .extract();

        final var stationIds = response.jsonPath().getList("id", Long.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        createdStations.forEach(
            createdStation -> assertThat(createdStation.getId()).isIn(stationIds)
        );
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStationSuccess() {
        // given
        final var deletedStation = StationFixture.createStation("삼성역");

        // when
        final var response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/stations/{id}", deletedStation.getId())
            .then().log().all()
            .extract();

        final var remainingStationIds = StationFixture.getStations().stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        assertThat(deletedStation.getId()).isNotIn(remainingStationIds);
    }

}