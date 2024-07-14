package subway.station;

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
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
        지하철역_생성("강남역");

        // then
        List<String> stationNames = 지하철역_목록조회();
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void showStations() {
        // given
        지하철역_생성("강남역");
        지하철역_생성("망원역");

        // when
        List<String> stationNames = 지하철역_목록조회();

        // then
        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).containsExactlyInAnyOrder("강남역", "망원역");

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
        ExtractableResponse<Response> response = 지하철역_생성("강남역");

        // when
        String location = response.header("Location");
        String stationId = location.substring(location.lastIndexOf("/") + 1);
        지하철역_삭제(stationId);

        // then
        List<String> stationNames = 지하철역_목록조회();
        assertThat(stationNames).hasSize(0);
        assertThat(stationNames).doesNotContain("강남역");
    }

    private static ExtractableResponse<Response> 지하철역_생성(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }

    private static List<String> 지하철역_목록조회() {
        return RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract().jsonPath().getList("name", String.class);
    }

    private void 지하철역_삭제(String stationId) {
        RestAssured.given().log().all()
            .when().delete("/stations/" + stationId)
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }


}