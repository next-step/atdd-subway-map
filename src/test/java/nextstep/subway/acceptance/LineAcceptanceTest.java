package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;

import static nextstep.subway.utils.LineUtils.*;
import static nextstep.subway.utils.ResponseUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        final Map<String, String> line1 = 지하철_노선_데이터_생성("1호선", "blue darken-4");

        // when
        final ExtractableResponse<Response> response = 지하철_노선_생성요청(line1);

        // then
        httpStatus가_CREATED면서_Location이_존재함(response);
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
        final Map<String, String> line1 = 지하철_노선_데이터_생성("1호선", "blue darken-4");
        final Map<String, String> line7 = 지하철_노선_데이터_생성("7호선", "green darken-3");

        지하철_노선_생성요청(Arrays.asList(line1, line7));

        // when
        final ExtractableResponse<Response> responseList = 지하철_노선_목록요청();

        // then
        httpStatus가_OK면서_ResponseBody가_존재함(responseList);
        responseList에_호선이_존재함(responseList, Arrays.asList("1호선", "7호선"));
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
        final Map<String, String> line1 = 지하철_노선_데이터_생성("1호선", "blue darken-4");
        final ExtractableResponse<Response> createResponse = 지하철_노선_생성요청(line1);

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> getResponse = 지하철_노선_목록요청(uri);

        // then
        httpStatus가_OK면서_ResponseBody가_존재함(getResponse);
        responseList에_호선이_존재함(getResponse, "1호선");
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
        final Map<String, String> line1 = 지하철_노선_데이터_생성("1호선", "blue darken-4");
        final ExtractableResponse<Response> response = 지하철_노선_생성요청(line1);

        // when
        final String uri = response.header("Location");
        final Map<String, String> editedLine = 지하철_노선_데이터_생성("7호선", "green darken-3");

        final ExtractableResponse<Response> editResponse = 지하철_노선_수정요청(editedLine, uri);

        // then
        httpStatus가_OK면서_ResponseBody가_존재함(editResponse);
        responseList에_호선이_존재함(editResponse, "7호선");
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
        final Map<String, String> line1 = 지하철_노선_데이터_생성("1호선", "blue darken-4");
        final ExtractableResponse<Response> response = 지하철_노선_생성요청(line1);

        // when
        final String uri = response.header("Location");
        final ExtractableResponse<Response> deleteResponse = 지하철_노선_삭제요청(uri);

        // then
        httpStatus가_NO_CONTENT(deleteResponse);
    }

    @DisplayName("지하철 노선 중복 생성 실패")
    @Test
    @Transactional
    void duplicateCheck() {
        // given
        final Map<String, String> line1 = 지하철_노선_데이터_생성("1호선", "blue darken-4");

        // when
        지하철_노선_생성요청(line1);
        final ExtractableResponse<Response> duplicateResponse = 지하철_노선_생성요청(line1);

        // then
        assertThat(duplicateResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
