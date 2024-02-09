package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.station.StationResponse;
import utils.StationManager;

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
        ExtractableResponse<Response> response = StationManager.save("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        response = StationManager.findAll();
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     * 인수 테스트란, End to End Test 인데, Controller -> Service -> Repository 까지 테스트 흐름이 움직이기 때문에 통합 테스트와 비슷한 성격을 띄는 것 같은데, 맞나...?
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void findAllStations() {
        // Given
        StationManager.save("지하철역이름");
        StationManager.save("새로운지하철역이름");
        StationManager.save("또다른지하철역이름");

        // 조회
        ExtractableResponse<Response> response = StationManager.findAll();

        List<StationResponse> stationResponses = response.jsonPath().getList(".", StationResponse.class);
        assertThat(stationResponses.size()).isEqualTo(3);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // Given
        StationManager.save("지하철역이름");
        StationManager.save("새로운지하철역이름");
        StationManager.save("또다른지하철역이름");

        // When
        StationManager.delete(1L);

        // then
        ExtractableResponse<Response> response = StationManager.findAll();

        List<StationResponse> stationResponses = response.jsonPath().getList(".", StationResponse.class);
        assertThat(stationResponses.size()).isEqualTo(2);
    }
}
