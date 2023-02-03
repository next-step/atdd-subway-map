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
    static ExtractableResponse<Response> 지하철역_생성(Map<String, String> params) {
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    static ExtractableResponse<Response> 지하철역_조회() {
        return RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract();
    }

    static void 지하철역_삭제(Long id) {
        RestAssured.given().log().all()
            .when().delete("/stations/{id}", id)
            .then().log().all();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void 지하철을_생성하면_목록_조회_시_생성한_역을_찾을_수_있다() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response = 지하철역_생성(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> retrieveResponse = 지하철역_조회();

        List<String> stationNames = retrieveResponse.jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 2개를 조회한다.")
    @Test
    void 지하철역을_2개_생성하고_목록을_조회하면_2개가_조회된다() {
        // Given
        Map<String, String> firstParams = new HashMap<>();
        firstParams.put("name", "강남역");
        Map<String, String> secondParams = new HashMap<>();
        secondParams.put("name", "역삼역");

        지하철역_생성(firstParams);
        지하철역_생성(secondParams);

        // When
        ExtractableResponse<Response> response = 지하철역_조회();

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
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void 지하철역을_생성하고_삭제하면_목록_조회_시_찾을_수_없다() {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        Long stationId = 지하철역_생성(params).jsonPath().getLong("id");

        // When
        지하철역_삭제(stationId);

        // Then
        ExtractableResponse<Response> retrieveResponse = 지하철역_조회();

        List<Long> ids = retrieveResponse.jsonPath().getList("id", Long.class);
        assertThat(ids).doesNotContain(stationId);
    }
}
