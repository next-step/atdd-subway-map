package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    StationRepository stationRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        stationRepository.deleteAll();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        List<String> stationNames = List.of("강남역");

        // when
        createStationsAndValidate(stationNames);

        // then
        getStationsAndValidateExistence(stationNames);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        List<String> stationNames = List.of("강남역", "역삼역");

        // when
        createStationsAndValidate(stationNames);

        // then
        getStationsAndValidateExistence(stationNames);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        List<String> stationNames = List.of("강남역");
        List<Long> stationIds = createStationsAndGetIds(stationNames);

        // when
        deleteStationsAndValidate(stationIds);

        // then
        getStationsAndValidateAbsence(stationNames);
    }

    private List<Long> createStationsAndGetIds(List<String> stationNames) {
        return createStationsAndValidate(stationNames).stream()
                .map(response -> response.jsonPath()
                        .getLong("id"))
                .collect(Collectors.toList());
    }

    private void deleteStationsAndValidate(List<Long> stationIds) {
        stationIds.stream()
                .map(id -> RestAssured.given().log().all()
                        .when().delete("/stations/{id}", id)
                        .then().log().all()
                        .extract())
                .forEach(response -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()));
    }

    private void getStationsAndValidateExistence(List<String> stationNames) {
        assertThat(fetchStationsAndGetNames()).hasSize(stationNames.size())
                .containsExactly(stationNames.toArray(String[]::new));
    }

    private void getStationsAndValidateAbsence(List<String> stationNames) {
        assertThat(fetchStationsAndGetNames()).doesNotContain(stationNames.toArray(String[]::new));
    }

    private List<String> fetchStationsAndGetNames() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath()
                .getList("name", String.class);
    }

    private List<ExtractableResponse<Response>> createStationsAndValidate(List<String> stationNames) {
        return stationNames.stream()
                .map(name -> RestAssured.given().log().all()
                        .body(Collections.singletonMap("name", name))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract())
                .peek(response -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()))
                .collect(Collectors.toList());
    }

}