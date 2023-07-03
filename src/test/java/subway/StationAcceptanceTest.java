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

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    @BeforeEach
    public void init() {
        List<Long> ids = callApiToGetStations().jsonPath().getList("id", Long.class);
        ids.forEach(StationAcceptanceTest::callApiToDeleteStations);
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
        String stationName = "사당역";
        ExtractableResponse<Response> response = callApiToCreateStation(stationName);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> stationsGetResponse = callApiToGetStations();

        List<String> stationNames = stationsGetResponse.jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(stationName);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록 조회을 조회한다.")
    @Test
    void getStations() {
        // given
        String stationName1 = "강남역";
        String stationName2 = "잠실역";

        callApiToCreateStation(stationName1);
        callApiToCreateStation(stationName2);

        // when
        ExtractableResponse<Response> response = callApiToGetStations();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames.size()).isEqualTo(2);
        assertThat(stationNames).containsAll(List.of(stationName1, stationName2));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStations() {
        // given
        String stationName1 = "홍대입구역";
        String stationName2 = "건대입구역";

        callApiToCreateStation(stationName1);
        callApiToCreateStation(stationName2);

        Long deletionTargetId = callApiToGetStations().jsonPath().getList("id", Long.class).get(0);

        // when
        ExtractableResponse<Response> deletionResponse = callApiToDeleteStations(deletionTargetId);

        // then
        assertThat(deletionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        List<Long> ids = callApiToGetStations().jsonPath().getList("id", Long.class);
        assertThat(ids.size()).isEqualTo(1);
        assertThat(ids).doesNotContain(deletionTargetId);
    }



    private static ExtractableResponse<Response> callApiToGetStations() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }
    private static ExtractableResponse<Response> callApiToDeleteStations(Long stationId) {
        return RestAssured.given().log().all().pathParam("stationId", stationId)
                .when().delete("/stations/{stationId}")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> callApiToCreateStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

}