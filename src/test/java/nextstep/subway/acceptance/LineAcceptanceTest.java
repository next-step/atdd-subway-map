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

    private static final String LINE_NAME_01 = "신분당선";
    private static final String LINE_COLOR_01 = "bg-red-600";
    private static final String LINE_NAME_02 = "2호선";
    private static final String LINE_COLOR_02 = "bg-green-600";

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> createResponse =
                LineSteps.지하철_노선_생성_요청(LINE_NAME_01, LINE_COLOR_01);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();
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
        LineSteps.지하철_노선_생성_요청(LINE_NAME_01, LINE_COLOR_01);
        LineSteps.지하철_노선_생성_요청(LINE_NAME_02, LINE_COLOR_02);

        // when
        ExtractableResponse<Response> getResponse = LineSteps.지하철_노선_조회_요청();

        // then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = getResponse.jsonPath().getList("name");
        assertThat(lineNames).contains(LINE_NAME_01, LINE_NAME_02);
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
        ExtractableResponse<Response> createResponse =
                LineSteps.지하철_노선_생성_요청(LINE_NAME_01, LINE_COLOR_01);

        // when
        ExtractableResponse<Response> getResponse =
                LineSteps.지하철_노선_조회_요청(createResponse.header("Location"));

        // then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.jsonPath().getString("name")).isEqualTo(LINE_NAME_01);
        assertThat(getResponse.jsonPath().getString("color")).isEqualTo(LINE_COLOR_01);
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
        ExtractableResponse<Response> createResponse =
                LineSteps.지하철_노선_생성_요청(LINE_NAME_01, LINE_COLOR_01);

        // when
        ExtractableResponse<Response> updateResponse =
                LineSteps.지하철_노선_수정_요청(createResponse.header("Location")
                                            , LINE_NAME_02, LINE_COLOR_02);

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updateResponse.jsonPath().getString("name")).isEqualTo(LINE_NAME_02);
        assertThat(updateResponse.jsonPath().getString("color")).isEqualTo(LINE_COLOR_02);
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
        ExtractableResponse<Response> createResponse =
                LineSteps.지하철_노선_생성_요청(LINE_NAME_01, LINE_COLOR_01);

        // when
        ExtractableResponse<Response> deleteResponse =
                LineSteps.지하철_노선_삭제_요청(createResponse.header("Location"));

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void duplicateLine() {
        // given
        LineSteps.지하철_노선_생성_요청(LINE_NAME_01, LINE_COLOR_01);

        // when
        ExtractableResponse<Response> duplicateResponse =
                LineSteps.지하철_노선_생성_요청(LINE_NAME_01, LINE_COLOR_01);

        // then
        assertThat(duplicateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
