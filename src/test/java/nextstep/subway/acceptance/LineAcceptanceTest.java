package nextstep.subway.acceptance;

import nextstep.subway.acceptance.rest.BaseCrudStep;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private final String LINE_PATH = "/lines";

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        var params = giveMeLineRequest("신분당선", "bg-red-600");

        // when
        var response = BaseCrudStep.createResponse(LINE_PATH, params);

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
        var params1 = giveMeLineRequest("신분당선", "bg-red-600");
        var createResponse1 = BaseCrudStep.createResponse(LINE_PATH, params1);

        var params2 = giveMeLineRequest("2호선", "bg-green-600");
        var createResponse2 = BaseCrudStep.createResponse(LINE_PATH, params2);

        // when
        var response = BaseCrudStep.readResponse(LINE_PATH);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        var lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).contains(params1.get("name"), params2.get("name"));
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
        var params = giveMeLineRequest("신분당선", "bg-red-600");
        var createResponse = BaseCrudStep.createResponse(LINE_PATH, params);

        // when
        var uri = createResponse.header("Location");
        var response = BaseCrudStep.readResponse(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

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
        var params = giveMeLineRequest("신분당선", "bg-red-600");
        var createResponse = BaseCrudStep.createResponse(LINE_PATH, params);

        // when
        var modifyParams = giveMeLineRequest("구분당선", "bg-blue-600");

        var uri = createResponse.header("Location");
        var response = BaseCrudStep.updateResponse(uri, modifyParams);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        var lineName = response.jsonPath().getString("name");
        assertThat(lineName).isEqualTo(modifyParams.get("name"));
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
        var params = giveMeLineRequest("신분당선", "bg-red-600");
        var createResponse = BaseCrudStep.createResponse(LINE_PATH, params);

        // when
        var uri = createResponse.header("Location");
        var response = BaseCrudStep.deleteResponse(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복 이름으로 지하철 노선 생성")
    @Test
    void createLineWithDuplicateName() {
        // given
        var params = giveMeLineRequest("신분당선", "bg-red-600");
        var createResponse = BaseCrudStep.createResponse(LINE_PATH, params);

        // when
        var response = BaseCrudStep.createResponse(LINE_PATH, params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    private Map<String, String> giveMeLineRequest(
            String name,
            String color
    ) {
        return Map.of(
                "name", name,
                "color", color
        );
    }
}
