package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
        // given
        String color = "bg-red-600";
        String name = "신분당선";

        // when
        ExtractableResponse<Response> response = LineTestStep.지하철_노선을_생성한다(color, name);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        Integer responseIntegerId = response.jsonPath().get("id");
        Long createdId = responseIntegerId.longValue();
        assertThat(createdId).isGreaterThan(0L);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다
     */
    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void createDuplicatedNameLine() {
        // given
        String color = "bg-red-600";
        String name = "신분당선";
        LineTestStep.지하철_노선을_생성한다(color, name);

        // when
        ExtractableResponse<Response> response = LineTestStep.지하철_노선을_생성한다(color, name);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
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
        String 이호선_색 = "bg-green-600";
        String 이호선_이름 = "2호선";
        LineTestStep.지하철_노선을_생성한다(이호선_색, 이호선_이름);

        String 신분당선_색 = "bg-red-600";
        String 신분당선_이름 = "신분당선";
        LineTestStep.지하철_노선을_생성한다(신분당선_색, 신분당선_이름);

        // when
        ExtractableResponse<Response> response = LineTestStep.지하철_노선_목록을_조회한다();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineColors = response.jsonPath().getList("color");
        assertThat(lineColors).containsExactly(이호선_색, 신분당선_색);
        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).containsExactly(이호선_이름, 신분당선_이름);
        List<List<Object>> lineStationsList = response.jsonPath().getList("stations");
        assertThat(lineStationsList).hasSizeGreaterThan(0);
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
        String 신분당선_색 = "bg-red-600";
        String 신분당선_이름 = "신분당선";
        Long 신분당선_생성_아이디 = LineTestStep.지하철_노선_생성한_후_아이디_추출하기(신분당선_색, 신분당선_이름);

        // when
        ExtractableResponse<Response> response = LineTestStep.지하철_노선을_조회한다(신분당선_생성_아이디);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Integer receivedId = response.jsonPath().get("id");
        assertThat(receivedId.longValue()).isEqualTo(신분당선_생성_아이디);
        String receivedColor = response.jsonPath().get("color");
        assertThat(receivedColor).isEqualTo(신분당선_색);
        String receivedName = response.jsonPath().get("name");
        assertThat(receivedName).isEqualTo(신분당선_이름);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     * Then 수정된 지하철 노선 정보 조회를 요청하여 수정되었는지 확인한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        String 신분당선_색 = "bg-red-600";
        String 신분당선_이름 = "신분당선";
        Long 신분당선_생성_아이디 = LineTestStep.지하철_노선_생성한_후_아이디_추출하기(신분당선_색, 신분당선_이름);

        String 수정_색 = "bg-red-400";
        String 수정_이름 = "신분당선_연장";

        // when
        ExtractableResponse<Response> response = LineTestStep.지하철_노선을_수정한다(신분당선_생성_아이디, 수정_색, 수정_이름);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> updatedResponse = LineTestStep.지하철_노선을_조회한다(신분당선_생성_아이디);
        String receivedColor = updatedResponse.jsonPath().get("color");
        assertThat(receivedColor).isEqualTo(수정_색);
        String receivedName = updatedResponse.jsonPath().get("name");
        assertThat(receivedName).isEqualTo(수정_이름);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     * Then 지하철 노선 목록 조회를 하면 empty list가 온다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        String 신분당선_색 = "bg-red-600";
        String 신분당선_이름 = "신분당선";
        Long 신분당선_생성_아이디 = LineTestStep.지하철_노선_생성한_후_아이디_추출하기(신분당선_색, 신분당선_이름);

        // when
        ExtractableResponse<Response> response = LineTestStep.지하철_노선을_삭제한다(신분당선_생성_아이디);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> linesResponse = LineTestStep.지하철_노선_목록을_조회한다();
        List<Object> lines = linesResponse.jsonPath().getList("$");
        assertThat(lines).hasSize(0);
    }
}
