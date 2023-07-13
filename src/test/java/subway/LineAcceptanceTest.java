package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.RestAssuredWrapper.*;

class LineAcceptanceTest extends AcceptanceTestBase {
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // When: 지하철 노선을 생성하면
        ExtractableResponse<Response> postResponse = post("/lines", new LineRequest("신분당선"));
        assertThat(postResponse.statusCode()).isEqualTo(HttpStatus.SC_CREATED);

        // Then: 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
        ExtractableResponse<Response> getResponse = get("/lines");
        List<String> lineNames = getResponse.jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선");
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // Given: 2개의 지하철 노선을 생성하고
        LineRequest sinBundangLine = new LineRequest("신분당선");
        post("/lines", sinBundangLine);
        LineRequest incheonSubwayLine1 = new LineRequest("인천지하철 1호선");
        post("/lines", incheonSubwayLine1);

        // When: 지하철 노선 목록을 조회하면
        ExtractableResponse<Response> getResponse = get("/lines");

        // Then: 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
        List<String> lineNames = getResponse.jsonPath().getList("name", String.class);
        assertThat(lineNames).containsExactly(sinBundangLine.getName(), incheonSubwayLine1.getName());
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // Given: 지하철 노선을 생성하고
        LineRequest line = new LineRequest("신분당선");
        ExtractableResponse<Response> postResponse = post("/lines", line);

        // When: 생성한 지하철 노선을 조회하면
        Long id = postResponse.as(LineResponse.class).getId();
        ExtractableResponse<Response> getResponse = get(String.format("lines/%s", id));

        // Then: 생성한 지하철 노선의 정보를 응답받을 수 있다
        String lineName = getResponse.jsonPath().get("name");
        assertThat(lineName).isEqualTo(line.getName());
    }

    @DisplayName("지하철 노선 수정")
    @Test
    void modifyLine() {
        // Given: 지하철 노선을 생성하고
        LineRequest line = new LineRequest("신분당선");
        ExtractableResponse<Response> postResponse = post("/lines", line);

        // When: 생성한 지하철 노선을 수정하면
        Long id = postResponse.as(LineResponse.class).getId();
        ModifyLineRequest modifyLineRequest = new ModifyLineRequest("인천지하철 1호선");
        ExtractableResponse<Response> putResponse = put(String.format("lines/%s", id), modifyLineRequest);

        // Then: 해당 지하철 노선 정보는 수정된다
        String lineName = putResponse.as(ModifyLineResponse.class).getName();
        assertThat(lineName).isEqualTo("인천지하철 1호선");
    }

    @DisplayName("지하철 노선 삭제")
    void deleteLine() {
        // Given: 지하철 노선을 생성하고
        LineRequest line = new LineRequest("신분당선");
        ExtractableResponse<Response> postResponse = post("/lines", line);

        // When: 생성한 지하철 노선을 삭제하면
        Long id = postResponse.as(LineResponse.class).getId();
        ExtractableResponse<Response> deleteResponse = delete(String.format("lines/%s", id));
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);

        // Then: 해당 지하철 노선 정보는 삭제된다
        ExtractableResponse<Response> getResponse = get("/lines");
        List<String> lineNames = getResponse.jsonPath().getList("name", String.class);
        assertThat(lineNames).doesNotContain("신분당선");
    }
}
