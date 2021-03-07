package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private Map<String, String> createParams(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return params;
    }

    private ExtractableResponse<Response> createInstanceBeforeTest(String name, String color) {
        Map<String, String> params = createParams(name, color);

        return postRequest("/lines", params);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        Map<String, String> params = createParams("2호선", "green");

        // when
        ExtractableResponse<Response> response = postRequest("/lines", params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = createInstanceBeforeTest("2호선", "green");
        ExtractableResponse<Response> createResponse2 = createInstanceBeforeTest("3호선", "orange");

        // when
        ExtractableResponse<Response> response = getRequest("/lines");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> expectedLineIds = Arrays.asList(createResponse1,createResponse2).stream()
                .map(result-> Long.parseLong(result.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(result -> result.getId())
                .collect(Collectors.toList());

        assertEquals(expectedLineIds, resultLineIds);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse1 = createInstanceBeforeTest("2호선", "green");
        Long createResponseId = Long.parseLong(createResponse1.header("Location").split("/")[2]);

        // when
        ExtractableResponse<Response> response = getRequest("/lines/"+createResponseId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse1 = createInstanceBeforeTest("2호선", "green");
        Long createResponseId = Long.parseLong(createResponse1.header("Location").split("/")[2]);

        Map<String, String> params = createParams("2호선", "orange");

        // when
        ExtractableResponse<Response> response = putRequest("/lines/"+createResponseId, params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse1 = createInstanceBeforeTest("2호선", "green");
        String uri = createResponse1.header("Location");

        // when
        ExtractableResponse<Response> response = deleteRequest(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("기존에 존재하는 노선 이름으로 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        Map<String, String> params = createParams("2호선", "green");
        postRequest("/lines", params);

        // when
        ExtractableResponse<Response> response = postRequest("/lines", params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
