package subway;

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

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        //given
        String stationName = "강남역";

        // when
        ExtractableResponse<Response> response = createStationsApiCall(stationName);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = showStationsApiCall().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(stationName);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("생성한 지하철역 목록 조회")
    @Test
    void showStations() {
        //given
        String stationNameA = "성수역";
        createStationsApiCall(stationNameA);
        String stationNameB = "잠실역";
        createStationsApiCall(stationNameB);

        //when
        ExtractableResponse<Response> response = showStationsApiCall();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name", String.class))
                .containsExactly(stationNameA, stationNameB);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("생성한 지하철역 삭제")
    @Test
    void deleteStation() {
        //given
        String stationName = "언주역";
        createStationsApiCall(stationName);

        //when
        ExtractableResponse<Response> response = given().log().all()
                .pathParam("id", 1L)
                .when().delete("/stations/{id}")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        List<String> stationsNames = showStationsApiCall().jsonPath().getList("name", String.class);
        assertThat(stationsNames).isEmpty();
    }

    private ExtractableResponse<Response> createStationsApiCall(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> showStationsApiCall() {
        return given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }
}