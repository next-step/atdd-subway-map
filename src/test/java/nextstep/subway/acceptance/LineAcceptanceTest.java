package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 2개의 지하철 노선을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철노선 목록을 조회한다.")
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

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선을 응답 받는다
     */
    // TODO: 지하철 노선 조회 인수 테스트 메서드 생성
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void getLine() {
        // given
        createStations();
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "3");
        params.put("distance", "10");
        createLines(params);

        // then
        assertThat(getLine("name", "1")).isEqualTo("2호선");

        // given
        params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-green-300");
        params.put("upStationId", "1");
        params.put("downStationId", "4");
        params.put("distance", "3");
        createLines(params);

        // then
        assertAll(() -> {
            assertThat(getLine("name", "1")).isEqualTo("2호선");
            assertThat(getLine("name", "2")).isEqualTo("신분당선");
        });
    }

    private String getLine(String variable, String id) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract().jsonPath().get(variable);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 수정된 지하철 노선을 응답 받는다
     */
    // TODO: 지하철 노선 수정 인수 테스트 메서드 생성
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        createStations();
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "3");
        params.put("distance", "10");
        createLines(params);

        // when
        params.put("name", "2호선-임시");
        params.put("color", "bg-blue-600");
        updateLine(params, "1");

        // then
        assertThat(getLine("name", "1")).isEqualTo("2호선-임시");
    }

    private void updateLine(Map params, String id) {
        update(params, "/lines/", id);
    }

    /**
     * Given 지하철노선을 생성하고
     * When 그 지하철노선을 삭제하면
     * Then 지하철노선 목록 조회 시 삭제한 노선을 찾을 수 없다
     */
    // TODO: 지하철노선 제거 인수 테스트 메서드 생성
    @DisplayName("지하철노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        createStations();
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "3");
        params.put("distance", "10");
        ExtractableResponse<Response> response = createLines(params);
        String createdId = response.jsonPath().getString("id");
        assertThat(getLine("name", createdId)).isEqualTo("2호선");

        // when
        deleteLine(createdId);

        // then
        assertThat(getList("name", "/lines")).hasSize(0);
    }

    private void deleteLine(String id) {
        delete("/lines/", id);
    }
}
