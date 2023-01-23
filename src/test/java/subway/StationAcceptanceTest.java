package subway;

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
class StationAcceptanceTest extends RestMethods {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("입력받은 지하철역을 생성한다.")
    @Test
    void createStationTest() {
        // given
        createStation("강남역");

        // when
        final ExtractableResponse<Response> response = get("/stations");
        final List<String> stationNames = response.jsonPath()
                .getList("name", String.class);

        // then
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("두개의 지하철역을 입력받아 생성하고, 목록을 조회하면 입력한 두 지하철역이 존재해야 한다.")
    @Test
    void requestAndResponseForSubwayStationTest() {
        // given
        createStation("인덕원역");
        createStation("안양역");

        // when
        ExtractableResponse<Response> response = get("/stations");
        List<String> stationNames = response.jsonPath()
                .getList("name", String.class);

        // then
        assertThat(stationNames).containsAnyOf("인덕원역", "안양역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @Test
    @DisplayName("지하철역을 생성한뒤 생성한 지하철역을 생성하면 처음에 생성한 지하철역은 목록에 없어야 한다")
    void createAndDeleteAfterNotFoundStation() {
        // given
        final Integer id = createStation("안양역")
                .jsonPath()
                .get("id");

        // when
        final ExtractableResponse<Response> response = delete("/station" + id);

        // then
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * 지하철을 입력받아 /station 에 post 요청하는 편의 메서드
     * @param name
     * @return response
     */
    ExtractableResponse<Response> createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return post("/stations", params);
    }

}