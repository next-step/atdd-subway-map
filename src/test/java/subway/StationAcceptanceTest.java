package subway;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StationAcceptanceTest {
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
    @Test
    void 생성된_지하철역_조회_테스트() {

        // given
        List<String> stations = List.of("강남역", "역삼역");
        stations.forEach(StationAcceptanceTest::createOneStation);

        // when
        List<String> stationNames = getStationNames();

        // then
        assertAll(
            () -> assertThat(stationNames).containsAll(stations),
            () -> assertThat(stationNames).containsExactly("강남역", "역삼역"),
            () -> assertEquals(stationNames.size(), stations.size())
        );
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @Test
    void 지하철역_제거_테스트() {

        // given
        String stations = "강남역";
        Map<String, String> params = new HashMap<>();
        ExtractableResponse<Response> oneStation = createOneStation(stations);
        Long stationId = oneStation.body().jsonPath().getLong("id");

        // when
        Response response = deleteStationById(stationId);
        List<String> stationNames = getStationNames();

        // then
        assertAll(
            () -> assertThat(stationNames).doesNotContain(stations),
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
        );
    }

    public static ExtractableResponse<Response> createOneStation(String station) {
        Map<String, String> params = new HashMap<>();
        params.put("name", station);
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    private static List<String> getStationNames() {
        return RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract().jsonPath().getList("name", String.class);
    }

    private static Response deleteStationById(Long stationId) {
        return RestAssured
            .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/stations/" + stationId);
    }
}