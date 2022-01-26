package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static nextstep.subway.fixture.TLine.*;
import static nextstep.subway.steps.LineSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private static final String 노선_이름 = "name";
    private static final String 노선_색 = "color";

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.header(HttpHeaders.LOCATION)).isNotBlank();
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
        지하철_노선_생성_요청(신분당선);
        지하철_노선_생성_요청(_2호선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList(노선_이름);
        assertThat(lineNames).containsExactly(신분당선.이름, _2호선.이름);
        List<String> lineColors = response.jsonPath().getList(노선_색);
        assertThat(lineColors).containsExactly(신분당선.색, _2호선.색);
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
        지하철_노선_생성_요청(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String name = response.jsonPath().get(노선_이름);
        assertThat(name).isEqualTo(신분당선.이름);
        String color = response.jsonPath().get(노선_색);
        assertThat(color).isEqualTo(신분당선.색);
    }

    /**
     * When 없는 지하철 노선 조회를 요청 하면
     * Then 404 응답을 받는다
     */
    @DisplayName("지하철 노선 조회 - 없는 노선")
    @Test
    void getInvalidLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(999L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정 - 존재 하는 경우 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(구분당선);

        // when
        String uri = createResponse.header(HttpHeaders.LOCATION);
        ExtractableResponse<Response> response = 지하철_노선_변경_요청(uri, 신분당선);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * When 없는 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선이 생성이 성공한다.
     */
    @DisplayName("지하철 노선 수정 - 없는 경우 NOT FOUND")
    @Test
    void updateNotExistLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_변경_요청("/lines/1", 구분당선);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
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
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(구분당선);

        // when
        String uri = createResponse.header(HttpHeaders.LOCATION);
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void duplicatedStationName() {
        // given
        지하철_노선_생성_요청(구분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(구분당선);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

}
