package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

    private static final String STATION_NAME_KEY = "name";
    private static final String STATION_ID_KEY = "id";
    private static final String STATION_BASE_URI = "/stations";

    @LocalServerPort
    int port;

    @DisplayName("RestAssured 요청 포트 번호를 설정합니다.")
    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @DisplayName("각 테스트 이후 모든 지하철역 정보를 삭제합니다.")
    @AfterEach
    void cleanUp() {
        allStations().jsonPath().getList(STATION_ID_KEY, Long.class)
            .stream()
            .forEach(this::deleteStation);
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
        ExtractableResponse<Response> response = insertStation("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = allStations().jsonPath().getList(STATION_NAME_KEY, String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void readStationList() {
        // given
        String station1 = "강남역";
        String station2 = "양재역";
        Stream.of(station1, station2).forEach(StationAcceptanceTest::insertStation);

        // when
        ExtractableResponse<Response> response = allStations();

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getList(STATION_NAME_KEY, String.class)).containsExactly(station1, station2),
            () -> assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE)
        );
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        Long savedStationId = insertStation("강남역").jsonPath().getLong(STATION_ID_KEY);

        // when
        ExtractableResponse<Response> response = deleteStation(savedStationId);

        // then
        assertAll(
            () -> assertThat(response.body().asString()).isBlank(),
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
            () -> assertThat(allStations().jsonPath().getList(STATION_ID_KEY, Long.class)).doesNotContain(savedStationId)
        );
    }

    public static ExtractableResponse<Response> insertStation(String name) {
        return RestAssured.given().log().all()
            .body(Map.of(STATION_NAME_KEY, name)).contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(STATION_BASE_URI)
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
    }

    private ExtractableResponse<Response> allStations() {
        return RestAssured.given().log().all()
            .when().get(STATION_BASE_URI)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    private ExtractableResponse<Response> deleteStation(Long id) {
        return RestAssured.given().log().all()
            .when().delete("/stations/{id}", id)
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .extract();
    }

}
