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

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    public static final String KANGNAM_STATION = "강남역";
    public static final String YEOKSAM_STATION = "역삼역";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> createStationResponse = 지하철역_생성(KANGNAM_STATION);

        // then
        assertThat(createStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = 지하철역_전체_조회().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(KANGNAM_STATION);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 전체 조회한다.")
    @Test
    void getStations() {
        // given
        지하철역_생성(KANGNAM_STATION);
        지하철역_생성(YEOKSAM_STATION);

        // when
        ExtractableResponse<Response> response = 지하철역_전체_조회();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        List<String> stationsNames = response.jsonPath().getList("name", String.class);
        assertThat(stationsNames).containsExactly(KANGNAM_STATION, YEOKSAM_STATION);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        Long stationId = 지하철역_생성(KANGNAM_STATION).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철역_삭제(stationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        List<Long> findStationsIds = 지하철역_전체_조회().jsonPath().getList("id", Long.class);
        assertThat(findStationsIds).doesNotContain(stationId);
    }


    private ExtractableResponse<Response> 지하철역_생성(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_삭제(Long stationId) {
        return RestAssured.given().log().all()
                .when().delete("/stations/{id}", stationId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_전체_조회() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }
}
