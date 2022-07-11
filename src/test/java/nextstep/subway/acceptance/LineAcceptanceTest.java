package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철노선을 생성하면
     * Then 지하철노선이 생성된다
     * Then 지하철노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        // when
        createStations();
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "3");
        params.put("distance", "10");
        ExtractableResponse<Response> response = createLines(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(getList("name", "/lines").get(0)).isEqualTo("2호선");
    }

    private void createStations() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        create(params, "/stations");
        params.put("name", "역삼역");
        create(params, "/stations");
        params.put("name", "선릉역");
        create(params, "/stations");
        params.put("name", "양재역");
        create(params, "/stations");
    }

    private ExtractableResponse<Response> createLines(Map params) {
        ExtractableResponse<Response> response = create(params, "/lines");
        return response;
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void getLines() {
        // given
        createStations();
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "3");
        params.put("distance", "10");
        createLines(params);
        params.put("name", "신분당선");
        params.put("color", "bg-green-300");
        params.put("upStationId", "1");
        params.put("downStationId", "4");
        params.put("distance", "3");
        createLines(params);

        // then
        assertThat(getList("name", "/lines")).containsExactly("2호선", "신분당선");
    }
}
