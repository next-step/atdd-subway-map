package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.RequestMethod;
import nextstep.subway.utils.RequestParams;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private static final String DEFAULT_PATH = "/lines";
    private static final String DEFAULT_NAME_KEY = "name";
    private static final String DEFAULT_NAME_VALUE = "신분당선";
    private static final String DEFAULT_COLOR_KEY = "color";
    private static final String DEFAULT_COLOR_VALUE = "bg-red-600";

    /**
     * When 지하철 노선 생성을 요청 하면 Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        RequestParams params = createDefaultParams();

        // when
        ExtractableResponse<Response> response = RequestMethod.post(DEFAULT_PATH, params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고 Given 새로운 지하철 노선 생성을 요청 하고 When 지하철 노선 목록 조회를 요청 하면 Then 두 노선이 포함된 지하철
     * 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        //given
        RequestParams params1 = createDefaultParams();

        RequestParams params2 = new RequestParams("name", "2호선");
        params2.addParams("color", "bg-green-600");

        RequestMethod.post(DEFAULT_PATH, params1);
        RequestMethod.post(DEFAULT_PATH, params2);

        //when
        ExtractableResponse<Response> response = RequestMethod.get(DEFAULT_PATH);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(DEFAULT_NAME_KEY)).contains("신분당선", "2호선");
        assertThat(response.jsonPath().getList(DEFAULT_COLOR_KEY)).contains("bg-green-600",
            "bg-red-600");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고 When 생성한 지하철 노선 조회를 요청 하면 Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        //given
        RequestParams params = createDefaultParams();

        ExtractableResponse<Response> createResponse = RequestMethod.post(DEFAULT_PATH, params);

        // when
        ExtractableResponse<Response> response = RequestMethod.get(
            "/lines/" + createResponse.jsonPath().getString("id"));

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
        //given
        RequestParams params = createDefaultParams();
        ExtractableResponse<Response> createResponse = RequestMethod.post(DEFAULT_PATH, params);

        // when
        RequestParams changeParam = createDefaultParams();

        ExtractableResponse<Response> response = RequestMethod.put(
            "/lines/" + createResponse.jsonPath().getString("id"), changeParam);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고 When 생성한 지하철 노선 삭제를 요청 하면 Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //given
        RequestParams params = createDefaultParams();

        ExtractableResponse<Response> createResponse = RequestMethod.post(DEFAULT_PATH, params);

        // when
        ExtractableResponse<Response> response = RequestMethod.delete(
            "/lines/" + createResponse.jsonPath().getString("id"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고 When 같은 이름으로 지하철 노선 생성을 요청 하면 Then 지하철 노선 생성이 실패한다.
     */
    @Test
    @DisplayName("중복이름으로 지하철 노선 생성 실패")
    void duplicationLineNameExceptionTest() {
        // given
        RequestParams params = createDefaultParams();

        RequestMethod.post(DEFAULT_PATH, params);
        // when
        ExtractableResponse<Response> response = RequestMethod.post(DEFAULT_PATH, params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    private static RequestParams createDefaultParams() {
        RequestParams params = new RequestParams(DEFAULT_NAME_KEY, DEFAULT_NAME_VALUE);
        params.addParams(DEFAULT_COLOR_KEY, DEFAULT_COLOR_VALUE);

        return params;
    }
}
