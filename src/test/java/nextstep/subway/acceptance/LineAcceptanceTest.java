package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    private static final String FIRST_NAME = "신분당선";
    private static final String FIRST_COLOR = "bg-red-700";

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {

        // when
        ExtractableResponse<Response> response = LineSteps.post(FIRST_NAME, FIRST_COLOR);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // when
        LineSteps.post(FIRST_NAME, FIRST_COLOR);

        ExtractableResponse<Response> response = LineSteps.get();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).contains(FIRST_NAME);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        String name = "2호선";
        String color = "bg-green-600";

        LineSteps.post(FIRST_NAME, FIRST_COLOR);
        LineSteps.post(name, color);

        // when
        ExtractableResponse<Response> response = LineSteps.get();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).contains(FIRST_NAME, name);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createLine = LineSteps.post(FIRST_NAME, FIRST_COLOR);

        // when
        ExtractableResponse<Response> response = LineSteps.put(createLine.header("Location"), "구분당선", "bg-red-600");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createLine = LineSteps.post(FIRST_NAME, FIRST_COLOR);

        // when
        ExtractableResponse<Response> response = LineSteps.delete(createLine.header("Location"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 이름 중복 확인")
    @Test
    void duplicateLineName() {
        // given
        LineSteps.post(FIRST_NAME, FIRST_COLOR);

        // when
        ExtractableResponse<Response> response = LineSteps.post(FIRST_NAME, FIRST_COLOR);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
