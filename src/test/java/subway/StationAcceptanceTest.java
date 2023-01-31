package subway;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

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
    @Test
    void retrieveStations() {
        // Given
        Map<String, String> firstParams = new HashMap<>();
        firstParams.put("name", "강남역");
        Map<String, String> secondParams = new HashMap<>();
        secondParams.put("name", "역삼역");

        createStation(firstParams);
        createStation(secondParams);

        // When
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();

        // Then
        List<String> stations = response.jsonPath()
            .getList("$");
        assertThat(stations).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    void deleteStation() {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        Long stationId = createStation(params).jsonPath().getLong("id");
        System.out.println(stationId);

        // When
        RestAssured.given().log().all()
            .when().delete("/stations/{id}", stationId)
            .then().log().all();

        // Then
        ExtractableResponse<Response> retrieveResponse =
            RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();

        List<String> result = retrieveResponse.jsonPath().getList("$");
        assertThat(result).isEmpty();
    }

    ExtractableResponse<Response> createStation(Map<String, String> params) {
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }
}
