package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.createLineRequest;
import static nextstep.subway.acceptance.LineSteps.deleteLineRequest;
import static nextstep.subway.acceptance.LineSteps.getLineRequest;
import static nextstep.subway.acceptance.LineSteps.getLinesRequest;
import static nextstep.subway.acceptance.LineSteps.updateLineRequest;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = createLineRequest("신분당선", "bg-red-600");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
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
        String name1 = "신분당선";
        String color1 = "bg-red-600";
        createLineRequest(name1, color1);

        String name2 = "2호선";
        String color2 = "bg-green-600";
        createLineRequest(name2, color2);

        // when
        ExtractableResponse<Response> response = getLinesRequest();
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).contains(name1, name2);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        String name = "신분당선";
        String color = "bg-red-600";
        ExtractableResponse<Response> createResponse = createLineRequest(name, color);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = getLineRequest(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).contains(name);
        assertThat(response.jsonPath().getString("color")).contains(color);
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
        String name = "신분당선";
        String color = "bg-red-600";
        ExtractableResponse<Response> createResponse = createLineRequest(name, color);

        // when
        String newName = "구분당선";
        String newColor = "bg-blue-600";
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = updateLineRequest(uri, newName, newColor);

        // then
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
        // given
        String name = "신분당선";
        String color = "bg-red-600";
        ExtractableResponse<Response> createResponse = createLineRequest(name, color);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = deleteLineRequest(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void createDuplicatedLine() {
        // given
        String name = "신분당선";
        String color = "bg-red-600";
        createLineRequest(name, color);

        // when
        ExtractableResponse<Response> response = createLineRequest(name, color);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

}
