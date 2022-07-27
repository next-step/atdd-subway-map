package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.line.LineRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 3L, 10L);
        ExtractableResponse<Response> response = createLines(dto);

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

    private ExtractableResponse<Response> createLines(LineRequest dto) {
        ExtractableResponse<Response> response = create(dto, "/lines");
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
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 3L, 10L);
        createLines(dto);
        dto = new LineRequest("신분당선", "bg-green-300", 1L, 4L, 3L);
        createLines(dto);

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
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 3L, 10L);
        createLines(dto);

        // then
        assertThat(getLine("name", "1")).isEqualTo("2호선");

        // given
        dto = new LineRequest("신분당선", "bg-green-300", 1L, 4L, 3L);
        createLines(dto);

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
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 3L, 10L);
        createLines(dto);

        // when
        Map<String, String> params = new HashMap<>();
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
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 3L, 10L);
        ExtractableResponse<Response> response = createLines(dto);
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

    /**
     * Given 추가할 구간을 생성하고
     * When 지하철노선에 구간을 추가하면
     * Then 지하철노선 마지막에 구간에 등록된다
     */
    // TODO: 구간 등록 인수 테스트 메서드 생성
    @DisplayName("구간을 등록한다.")
    @Test
    void addSection() {
        // given


        // when


        // then

    }

    /**
     * When 지하철노선에 구간을 삭제하면
     * Then 지하철노선 마지막 구간이 삭제된다
     */
    // TODO: 구간 삭제 인수 테스트 메서드 생성
    @DisplayName("구간을 삭제한다.")
    @Test
    void deleteSection() {
    }
}
