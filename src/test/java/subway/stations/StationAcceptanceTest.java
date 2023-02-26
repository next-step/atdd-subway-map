package subway.stations;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.util.DatabaseCleanup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setUp() {
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
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
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
    void searchStations() {
        // given
        Map<String, String> station1 = new HashMap<>();
        station1.put("name", "강남역");

        Map<String, String> station2 = new HashMap<>();
        station2.put("name", "신논현역");

        createStationResponse(station1);
        createStationResponse(station2);

        // when
        ExtractableResponse<Response> response = searchStationResponse();
        List<String> stationNames = response.jsonPath().getList("name", String.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        assertThat(stationNames).containsOnly("강남역", "신논현역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStations() {
        // given
        Map<String, String> station = new HashMap<>();
        station.put("name", "강남역");

        ExtractableResponse<Response> request = createStationResponse(station);
        Long id = request.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all().extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> searchResponse = searchStationResponse();
        List<Object> list = searchResponse.jsonPath().getList("id");
        assertThat(list.size()).isEqualTo(0);
    }

    private static ExtractableResponse<Response> createStationResponse(Map<String, String> param) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().post("/stations")
                .then().log().all().extract();
    }

    private static ExtractableResponse<Response> searchStationResponse() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all().extract();
    }

}
