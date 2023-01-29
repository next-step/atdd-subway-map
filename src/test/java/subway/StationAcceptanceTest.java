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

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // When 지하철역을 생성하면
        ExtractableResponse<Response> createResponse = 지하철역_생성("강남역");

        // Then 지하철역이 생성된다
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다.
        ExtractableResponse<Response> findResponse = 지하철역_목록_조회();
        List<String> stationNames = findResponse.jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void findAllStations() {
        // Given 2개의 지하철역을 생성하고
        지하철역_생성("양재역");
        지하철역_생성("교대역");

        // When 지하철 목록을 조회하면
        ExtractableResponse<Response> response = 지하철역_목록_조회();
        List<String> stationNames = response.jsonPath().getList("name", String.class);

        // Then 2개의 지하철역을 응답받는다.
        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).containsExactly("양재역", "교대역");
    }

    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // Given 지하철역을 생성하고
        ExtractableResponse<Response> createResponse = 지하철역_생성("잠실역");

        // When 그 지하철역을 삭제하면
        Long id = createResponse.jsonPath().getLong("id");
        ExtractableResponse<Response> deleteResponse = 지하철역_삭제(id);
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다.
        ExtractableResponse<Response> findResponse = 지하철역_목록_조회();
        List<String> stationNames = findResponse.jsonPath().getList("name", String.class);
        assertThat(stationNames).doesNotContain("잠실역");
    }


    private ExtractableResponse<Response> 지하철역_목록_조회() {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_생성(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_삭제(Long id){
        return given()
                .when().delete("/stations/{id}", id)
                .then().log().all()
                .extract();
    }
}