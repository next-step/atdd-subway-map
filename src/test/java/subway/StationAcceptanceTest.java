package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import subway.station.StationResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StationAcceptanceTest {

    private final String[] STATION_NAMES = {"강남역", "약삼역"};
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = saveStation(STATION_NAMES[0]);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(getStationName()).containsAnyOf(STATION_NAMES[0]);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void searchStation() {
        //given
        Arrays.stream(STATION_NAMES)
            .forEach(this::saveStation);

        //when
        List<String> stations = getStationName();

        //then
        assertThat(stations).hasSize(STATION_NAMES.length);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void removeStation() {
        //given
        ExtractableResponse<Response> response = saveStation(STATION_NAMES[0]);
        StationResponse stationResponse = response.jsonPath().getObject("", StationResponse.class);

        //when
        RestAssured.given().log().all()
            .when().delete("/stations/" + stationResponse.getId())
            .then();

        //then
        assertThat(getStationName()).doesNotContain(STATION_NAMES[0]);
    }

    private ExtractableResponse<Response> saveStation(String name) {

        Map<String, String> params = new HashMap<>();
        params.put("name", name);

       return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private List<String> getStationName() {
        return RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract().jsonPath().getList("name", String.class);
    }
}