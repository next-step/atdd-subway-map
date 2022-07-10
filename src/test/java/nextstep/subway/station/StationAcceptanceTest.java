package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StationAcceptanceTest {

    private static final String GANGNAM_STATION = "강남역";
    private static final String SINDORIM_STATION = "신도림역";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

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
        // when
        final ExtractableResponse<Response> creationResponse = 지하철역_생성(GANGNAM_STATION);

        // then
        assertThat(creationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        final List<String> stationNames = 지하철역명_조회();
        assertThat(stationNames).containsExactly(GANGNAM_STATION);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStation() {
        // given 2개의 지하철역 생성
        지하철역_생성(GANGNAM_STATION);
        지하철역_생성(SINDORIM_STATION);

        // when 지하철역 목록 조회
        final ExtractableResponse<Response> stationsResponse = 지하철역_조회();
        final List<String> stationNames = 지하철역명_조회(stationsResponse);

        // then 지하철역 응답 확인
        Assertions.assertAll(
                () -> assertThat(stationsResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationNames).isEqualTo(List.of(GANGNAM_STATION, SINDORIM_STATION))
        );
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given 지하철역 생성
        final ExtractableResponse<Response> creationResponse = 지하철역_생성(GANGNAM_STATION);

        // when 지하철역 삭제
        final Long id = creationResponse.jsonPath().getObject(KEY_ID, Long.class);
        final ExtractableResponse<Response> deletionResponse = 지하철역_삭제(id);

        // then 지하철역 삭제 확인
        final List<String> stationNames = 지하철역명_조회();
        Assertions.assertAll(
                () -> assertThat(deletionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(stationNames).isEmpty()
        );
    }

    private ExtractableResponse<Response> 지하철역_생성(final String stationName) {
        return RestAssured.given().log().all()
                .body(Map.of(KEY_NAME, stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_조회() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private List<String> 지하철역명_조회(final ExtractableResponse<Response> response) {
        return response.jsonPath().getList(KEY_NAME, String.class);
    }

    private List<String> 지하철역명_조회() {
        final ExtractableResponse<Response> stationResponse = 지하철역_조회();
        return 지하철역명_조회(stationResponse);
    }

    private ExtractableResponse<Response> 지하철역_삭제(final Long id) {
        return RestAssured.given().log().all()
                .when().delete("/stations/{id}", id)
                .then().log().all()
                .extract();
    }

}
