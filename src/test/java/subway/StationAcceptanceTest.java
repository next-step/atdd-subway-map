package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    public static final String STATION_NAME_KEY = "name";
    public static final String STATION_ID_KEY = "id";
    public static final String STATION_BASE_URI = "/stations";

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
        Stream.of("강남역", "양재역").forEach(this::insertStation);

        // when
        ExtractableResponse<Response> response = allStations();

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getList(STATION_NAME_KEY, String.class)).containsExactly("강남역", "양재역"),
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
        ExtractableResponse<Response> saved = insertStation("강남역");

        // when
        ExtractableResponse<Response> response = deleteStation(saved.jsonPath().getLong(STATION_ID_KEY));

        // then
        assertAll(
            () -> assertThat(response.body().asString()).isBlank(),
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
        );
    }

    private ExtractableResponse<Response> insertStation(String name) {
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
            .when().delete(STATION_BASE_URI + "/" + id)
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .extract();
    }

}
