package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.utils.AssuredRequest;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.common.ErrorMessages.DUPLICATE_LINE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관리 기능")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LineAcceptanceTest extends AcceptanceTest {

    private static final String END_POINT = "/lines";

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    @Order(1)
    void createLine() {
        // given
        Map<String, String> lineRequest = 노선_요청_정보_샘플1();

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.jsonPath().getString("name")).isEqualTo(lineRequest.get("name")),
                () -> assertThat(response.jsonPath().getString("color")).isEqualTo(lineRequest.get("color"))
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    @Order(3)
    void getLines() {
        // given
        Map<String, String> lineRequest = 노선_요청_정보_샘플1();
        지하철_노선_생성_요청(lineRequest);

        Map<String, String> newLineRequest = 노선_요청_정보_샘플2();
        지하철_노선_생성_요청(newLineRequest);

        List<Map<String, String>> requestList = Arrays.asList(lineRequest, newLineRequest);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> {
                    List<LineResponse> responseList = response.jsonPath().getList("", LineResponse.class);
                    assertThat(responseList.size()).isEqualTo(requestList.size());
                    assertThat(responseList.stream().map(LineResponse::getName).collect(Collectors.toList()))
                            .isEqualTo(requestList.stream().map(map -> map.get("name")).collect(Collectors.toList()));
                }
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    @Order(5)
    void getLine() {
        // given
        Map<String, String> lineRequest = 노선_요청_정보_샘플1();
        ExtractableResponse<Response> givenResponse = 지하철_노선_생성_요청(lineRequest);
        String searchUri = givenResponse.header("Location");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(searchUri);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("name")).isEqualTo(lineRequest.get("name")),
                () -> assertThat(response.jsonPath().getString("color")).isEqualTo(lineRequest.get("color"))
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    @Order(7)
    void updateLine() {
        // given
        Map<String, String> lineRequest = 노선_요청_정보_샘플1();
        ExtractableResponse<Response> givenResponse = 지하철_노선_생성_요청(lineRequest);

        String updateUri = givenResponse.header("Location");
        Map<String, String> updateLineRequest = 노선_요청_정보_수정_샘플1();

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(updateUri, updateLineRequest);

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
    @Order(9)
    void deleteLine() {
        // given
        Map<String, String> lineRequest = 노선_요청_정보_샘플1();
        ExtractableResponse<Response> givenResponse = 지하철_노선_생성_요청(lineRequest);
        String deleteUri = givenResponse.header("Location");

        // when
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(deleteUri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선 생성 에러 - 중복된 이름으로 생성할 수 없다")
    @Test
    @Order(11)
    void createLineError1() {
        // given
        Map<String, String> lineRequest = 노선_요청_정보_샘플1();
        지하철_노선_생성_요청(lineRequest);
        Map<String, String> duplicateRequest = 노선_요청_정보_중복_샘플1();

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(duplicateRequest);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("errorMessage")).isEqualTo(DUPLICATE_LINE_NAME.getMessage())
        );
    }

    private Map<String, String> 노선_요청_정보_샘플1() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파란색");
        return params;
    }

    private Map<String, String> 노선_요청_정보_샘플2() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "연두색");
        return params;
    }

    private Map<String, String> 노선_요청_정보_수정_샘플1() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "비둘기열차");
        params.put("color", "노랑색");
        return params;
    }

    private Map<String, String> 노선_요청_정보_중복_샘플1() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "초록색");
        return params;
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> map) {
        return AssuredRequest.doCreate(END_POINT, map);
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return AssuredRequest.doFind(END_POINT);
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(String uri) {
        return AssuredRequest.doFind(uri);
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(String uri, Map<String, String> map) {
        return AssuredRequest.doUpdate(uri, map);
    }

    private ExtractableResponse<Response> 지하철_노선_삭제_요청(String uri) {
        return AssuredRequest.doDelete(uri);
    }
}
