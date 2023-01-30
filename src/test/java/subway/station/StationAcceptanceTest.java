package subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
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
        ExtractableResponse<Response> response = saveStation("강남역");

        // then
        assertSuccessCreate(response);

        // then
        List<String> stationNames = assertSuccessShow(showStationsResponse());
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("지하철역 목록 조회")
    void showStations() {
        // given
        saveStation("강남역");
        saveStation("역삼역");

        // when
        List<String> stationNames = assertSuccessShow(showStationsResponse());

        // then
        assertThat(stationNames.size()).isEqualTo(2);
        assertThat(stationNames).containsExactly("강남역", "역삼역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    @DisplayName("지하철역 제거")
    void deleteStation() {
        // given
        Long id = assertSuccessCreate(saveStation("강남역"));

        // when
        assertSuccessDelete(deleteStationResponse(id));

        // then
        List<String> stationNames = assertSuccessShow(showStationsResponse());
        assertThat(stationNames.size()).isEqualTo(0);
    }

    private ExtractableResponse<Response> showStationsResponse() {
        return RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private static List<String> assertSuccessShow(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(OK.value());
        return response.jsonPath().getList("name", String.class);
    }

    private ExtractableResponse<Response> saveStation(String stationName) {
        Map<String, String> param = new HashMap<>();
        param.put("name", stationName);

        return createStationResponse(param);
    }

    private ExtractableResponse<Response> createStationResponse(Map<String, String> params) {
        return RestAssured
                .given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private Long assertSuccessCreate(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        return response.body().jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> deleteStationResponse(Long id) {
        return RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract();
    }

    private void assertSuccessDelete(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }
}
