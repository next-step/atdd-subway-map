package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SubwayLineAcceptanceTest {

    @BeforeEach
    void setUp() {
        createStation("지하철역");
        createStation("새로운지하철역");
        createStation("또다른지하철역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createSubwayLine() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> subwayLines = getSubwayLines();
        assertThat(subwayLines).containsAnyOf("신분당선");
    }



    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void showStations() {
        //given
        createStation("강남역");
        createStation("성수역");

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        //then
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames.size()).isEqualTo(2);
        //then
        assertThat(stationNames).containsAnyOf("강남역", "성수역");
    }



    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 제거")
    @Test
    void deleteStation() {
        //given
        createStation("강남역");

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/lines/1")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        //then
        List<String> stationNames = getSubwayLines();
        assertThat(stationNames).doesNotContain("강남역");
    }

    private void createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all();
    }

    private List<String> getSubwayLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }


}