package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.steps.LineSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        String name = "신분당선";
        String color = "bg-red-600";
        ExtractableResponse<Response> response = LineSteps.executeLineCreateRequest(name, color);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(name);
        assertThat(response.jsonPath().getString("color")).isEqualTo(color);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복 이름 지하철 노선 생성")
    @Test
    void createDuplicatedLine() {
        String name = "신분당선";
        String color = "bg-red-600";
        LineSteps.executeLineCreateRequest(name, color);

        ExtractableResponse<Response> response = LineSteps.executeLineCreateRequest(name, color);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
        String lineName1 = "신분당선";
        String lineColor1 = "bg-red-600";
        LineSteps.executeLineCreateRequest(lineName1, lineColor1);

        String lineName2 = "2호선";
        String lineColor2 = "bg-green-600";
        LineSteps.executeLineCreateRequest(lineName2, lineColor2);

        ExtractableResponse<Response> response = LineSteps.executeLineListGetRequest();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).contains(lineName1, lineName2);

        List<String> lineColors = response.jsonPath().getList("color");
        assertThat(lineColors).contains(lineColor1, lineColor2);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        String name = "신분당선";
        String color = "bg-red-600";
        ExtractableResponse<Response> createdResponse = LineSteps.executeLineCreateRequest(name, color);

        Long createdId = createdResponse.jsonPath().getLong("id");
        ExtractableResponse<Response> response = LineSteps.executeLineGetRequest(createdId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(name);
        assertThat(response.jsonPath().getString("color")).isEqualTo(color);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        String name = "신분당선";
        String color = "bg-red-600";
        ExtractableResponse<Response> createdResponse = LineSteps.executeLineCreateRequest(name, color);
        Long createdId = createdResponse.jsonPath().getLong("id");

        String updateName = "구분당선";
        String updateColor = "bg-blue-600";
        ExtractableResponse<Response> response = LineSteps.executeLineUpdateRequest(createdId, updateName, updateColor);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        String name = "신분당선";
        String color = "bg-red-600";
        ExtractableResponse<Response> createdResponse = LineSteps.executeLineCreateRequest(name, color);

        Long createdId = createdResponse.jsonPath().getLong("id");
        ExtractableResponse<Response> response = LineSteps.executeLineDeleteRequest(createdId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
