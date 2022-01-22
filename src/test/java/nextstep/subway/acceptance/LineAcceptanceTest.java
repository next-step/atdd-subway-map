package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import nextstep.subway.utils.RequestMethod;
import nextstep.subway.utils.RequestParams;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선 생성을 요청 하면 Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        RequestParams params = new RequestParams("name", "신분당선");
        params.addParams("color", "bg-red-600");

        // when
        ExtractableResponse<Response> response = RequestMethod.post("/lines", params);

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
        RequestParams params1 = new RequestParams("name", "신분당선");
        params1.addParams("color", "bg-red-600");

        RequestParams params2 = new RequestParams("name", "2호선");
        params2.addParams("color", "bg-green-600");

        RequestMethod.post("/lines", params1);
        RequestMethod.post("/lines", params2);

        //when
        ExtractableResponse<Response> response = RequestMethod.get("/lines");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).contains("신분당선", "2호선");
        assertThat(response.jsonPath().getList("color")).contains("bg-green-600", "bg-red-600");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고 When 생성한 지하철 노선 조회를 요청 하면 Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        //given
        RequestParams params = new RequestParams("name", "신분당선");
        params.addParams("color", "bg-red-600");

        ExtractableResponse<Response> createResponse = RequestMethod.post("/lines", params);

        // when
        ExtractableResponse<Response> response = RequestMethod.get(
            "/lines/" + createResponse.jsonPath().getString("id"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고 When 지하철 노선의 정보 수정을 요청 하면 Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @ParameterizedTest
    @MethodSource
    void updateLine(RequestParams params) {
        //given
        ExtractableResponse<Response> createResponse = RequestMethod.post("/lines", params);

        // when
        RequestParams changeParam = new RequestParams("name", "구분당선");
        changeParam.addParams("color", "bg-blue-600");

        ExtractableResponse<Response> response = RequestMethod.put(
            "/lines/" + createResponse.jsonPath().getString("id"), changeParam);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    static Stream<Arguments> updateLine() {
        RequestParams params = new RequestParams("name", "신분당선");
        params.addParams("color", "bg-red-600");
        return Stream.of(
            Arguments.of(
                params
            )
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고 When 생성한 지하철 노선 삭제를 요청 하면 Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //given
        RequestParams params = new RequestParams("name", "신분당선");
        params.addParams("color", "bg-red-600");

        ExtractableResponse<Response> createResponse = RequestMethod.post("/lines", params);

        // when
        ExtractableResponse<Response> response = RequestMethod.delete(
            "/lines/" + createResponse.jsonPath().getString("id"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
