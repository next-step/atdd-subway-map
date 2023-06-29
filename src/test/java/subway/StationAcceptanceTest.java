package subway;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class StationAcceptanceTest {

    private static final String BASE_URL = "/stations";

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        final String name = "강남역";

        // required
        requiredStationsNotExist(name);

        // when
        final ExtractableResponse<Response> response = createStation(name);

        // then
        assertStatusCodeEqualTo(response, HttpStatus.CREATED);
        assertStationCreated(name);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void showStations() {
        final List<String> stationNames = List.of("강남역", "역삼역", "선릉역");

        // required
        requiredStationsNotExist(stationNames);

        // given
        stationNames.forEach(name ->
                RestAssured.given().log().all()
                        .body(Map.of("name", name))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post(BASE_URL)
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
        );

        // when
        final ExtractableResponse<Response> response = getAllStations();

        // then
        assertStatusCodeEqualTo(response, HttpStatus.OK);
        assertStationNamesContainsExactly(response, stationNames);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        final String name = "강남역";

        // required
        requiredStationsNotExist(name);

        // given
        createStation(name);
        final Long stationId = getStationId(name);

        // when
        final ExtractableResponse<Response> response = deleteStation(stationId);

        // then
        assertStatusCodeEqualTo(response, HttpStatus.NO_CONTENT);
        assertStationsEmpty();
    }

    private ExtractableResponse<Response> deleteStation(final Long stationId) {
        return RestAssured.given().log().all()
                .when().delete("/stations/" + stationId)
                .then()
                .extract();
    }

    private Long getStationId(final String name) {
        final List<StationResponse> responses =
                RestAssured.given().log().all()
                        .when().get(BASE_URL)
                        .then()
                        .extract()
                        .jsonPath().getList(".", StationResponse.class);

        assertThat(responses).hasSize(1);

        final Optional<StationResponse> response = responses.stream()
                .filter(it -> it.getName().equals(name))
                .findAny();

        assertThat(response).isNotNull();

        return response.get().getId();
    }


    private ExtractableResponse<Response> getAllStations() {
        return RestAssured.given().log().all()
                .when().get(BASE_URL)
                .then()
                .extract();
    }

    private void assertStationNamesContainsExactly(
            final ExtractableResponse<Response> response,
            final List<String> expectedNames
    ) {
        final List<String> actualNames = response.jsonPath().getList("name", String.class);
        assertThat(actualNames).isEqualTo(expectedNames);
    }

    private void assertStationsEmpty() {
        final List<StationResponse> stations =
                RestAssured.given().log().all()
                        .when().get(BASE_URL)
                        .then()
                        .extract()
                        .jsonPath().getList(".", StationResponse.class);

        assertThat(stations).isEmpty();
    }

    private void requiredStationsNotExist(final List<String> expectedNames) {
        final List<String> actualNames =
                RestAssured.given().log().all()
                        .when().get(BASE_URL)
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);

        if (!actualNames.isEmpty()) {
            assertThat(actualNames).doesNotContainSequence(expectedNames);
        }
    }

    private void requiredStationsNotExist(final String... expectedNames) {
        requiredStationsNotExist(List.of(expectedNames));
    }

    private ExtractableResponse<Response> createStation(final String name) {
        return RestAssured.given().log().all()
                .body(Map.of("name", name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(BASE_URL)
                .then().log().all()
                .extract();
    }

    private void assertStationCreated(final String expectedName) {
        final List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get(BASE_URL)
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf(expectedName);
    }

    private void assertStatusCodeEqualTo(ExtractableResponse<Response> response, HttpStatus expected) {
        assertThat(response.statusCode()).isEqualTo(expected.value());
    }
}