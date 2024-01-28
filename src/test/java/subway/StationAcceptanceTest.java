package subway;

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
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = createStationByName("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());

        // then
        List<String> stationNames = getNamesFromStation();
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철 역을 조회한다.")
    @Test
    void getStations() {
        // given
        createStationByName("오이도역");
        createStationByName("정왕역");

        // when
        List<String> actual = getNamesFromStation();

        // then
        assertThat(actual).containsExactlyInAnyOrder("오이도역", "정왕역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철 역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> station = createStationByName("오이도역");

        // when
        ExtractableResponse<Response> deleteStationResponse =
                RestAssured.given().log().all()
                        .pathParam("id", station.jsonPath().get("id"))
                        .when().delete("/stations/{id}")
                        .then().log().all()
                        .extract();

        // then
        assertThat(deleteStationResponse.statusCode()).isEqualTo(NO_CONTENT.value());
        assertThat(getNamesFromStation()).doesNotContain("오이도역");
    }

    private static ExtractableResponse<Response> createStationByName(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();
        return response;
    }

    private static List<String> getNamesFromStation() {
        List<String> result = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
        return result;
    }

}