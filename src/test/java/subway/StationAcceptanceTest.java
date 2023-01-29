package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    private static final String STATION_NAME = "name";

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
                RestAssured.given()
                        .log()
                        .all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/stations")
                        .then()
                        .log()
                        .all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                RestAssured.given()
                        .log()
                        .all()
                        .when()
                        .get("/stations")
                        .then()
                        .log()
                        .all()
                        .extract()
                        .jsonPath()
                        .getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void testGetStations() {
        // given
        String 강남역 = "강남역";
        String 서울대입구역 = "서울대입구역";

        insertStation(강남역);
        insertStation(서울대입구역);

        // when
        List<String> stationNames = getStations();

        // then
        assertAll(
                () -> assertThat(stationNames.size()).isEqualTo(2),
                () -> assertTrue(stationNames.containsAll(List.of(서울대입구역, 강남역)))
        );
    }


    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void testDeleteStation() {
        String 서울대입구역 = "서울대입구역";
        insertStation(서울대입구역);

        // when
        RestAssured
                .given()
                .when()
                    .delete("/stations/{id}", 1)
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

        List<String> stations = getStations();

        // then
        assertFalse(stations.contains(서울대입구역));
    }

    private List<String> getStations() {
        return RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/stations")
                .then()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .jsonPath()
                .getList(STATION_NAME, String.class);
    }

    private void insertStation(String station) {
        Map<String, String> param = Map.of(STATION_NAME, station);

        RestAssured
                .given()
                    .body(param)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations");
    }
}