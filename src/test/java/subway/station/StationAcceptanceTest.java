package subway.station;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.station.fixture.StationFixtures;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DirtiesContext
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response =
            given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
            given().log().all()
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
    @DirtiesContext
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStationList() {
        // given
        List<String> stations = List.of("강남역", "판교역");
        for (String station : stations) {
            StationFixtures.createSubwayStation(station);
        }

        // when
        ExtractableResponse<Response> response =
            given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        List<String> stationNames = response.body().jsonPath().getList("name", String.class);

        for (String station : stations) {
            assertThat(stationNames).containsAnyOf(station);
        }
        assertThat(stationNames.size()).isEqualTo(stations.size());
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DirtiesContext
    @DisplayName("지하철역을 제거한다.")
    @Test
    void removeStation() {
        // given
        List<String> stations = List.of("강남역", "판교역");
        for (String station : stations) {
            StationFixtures.createSubwayStation(station);
        }

        // when
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", 1)
            .when().delete("/stations/{id}")
            .then().log().all();

        // then
        ExtractableResponse<Response> response = given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/stations")
            .then().log().all().extract();

        List<String> stationList = response.body().jsonPath().getList("name", String.class);
        assertThat(stationList).containsOnly("판교역");
    }
}