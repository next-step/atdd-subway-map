package subway;

import common.RestAssuredExecutorService;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
    void buildStation() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        ExtractableResponse<Response> stationBuiltResponse = RestAssuredExecutorService.postForResponseByBody("/stations", params);

        // then
        assertThat(stationBuiltResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> stationsResponse = RestAssuredExecutorService.getForResponse("/stations");
        List<String> stationNames = stationsResponse.jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(params.get("name"));
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다")
    @Test
    void getStatins() {
        // Given
        StationRequest firstRequest = new StationRequest("사당역");
        RestAssuredExecutorService.postForResponseByBody("/stations", firstRequest);

        StationRequest secondRequest = new StationRequest("동작역");
        RestAssuredExecutorService.postForResponseByBody("/stations", secondRequest);

        // When
        ExtractableResponse<Response> stationsResponse = RestAssuredExecutorService.getForResponse("/stations");
        int responseSize = stationsResponse.body().jsonPath().getList("name", String.class).size();

        // Then
        assertThat(responseSize).isEqualTo(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역이 삭제된다(해당 부분이 추가 되어야 하지 않을까?)
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역 제거한다")
    @Test
    void closeStatin() {
        // Given
        StationRequest request = new StationRequest("사당역");
        ExtractableResponse<Response> stationBuiltResponse =
                RestAssuredExecutorService.postForResponseByBody("/stations", request);

        // When
        Map<String, Object> pathParameter = new HashMap<>();
        pathParameter.put("id", 1);
        ExtractableResponse<Response> stationRemovedResponse =
                RestAssuredExecutorService.deleteForResponse("/stations/{id}", pathParameter);

        // Then
        assertThat(stationRemovedResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // Then
        ExtractableResponse<Response> stationsResponse = RestAssuredExecutorService.getForResponse("/stations");
        List<String> stationNames = stationsResponse.body().jsonPath().getList("name", String.class);

        assertThat(stationNames).doesNotContain(stationBuiltResponse.body().jsonPath().getString("name"));
    }
}