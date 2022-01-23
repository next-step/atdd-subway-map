package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.step.LineSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    final String 아이디 = "id";
    final String 이름 = "name";
    final String 색상 = "color";

    final Map<String, String> 신분당선 = Map.of(이름, "신분당선", 색상, "bg-red-600");
    final Map<String, String> 이호선 = Map.of(이름, "2호선", 색상, "bg-green-600");

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        //given & when
        ExtractableResponse<Response> response = LineSteps.지하철_노선_생성_요청(신분당선);

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
        //given & when
        LineSteps.지하철_노선_생성_요청(신분당선);
        LineSteps.지하철_노선_생성_요청(이호선);

        //when
        ExtractableResponse<Response> searchResponse = LineSteps.지하철_노선_조회_요청();

        // then
        assertThat(searchResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> lineNames = searchResponse.jsonPath().getList(이름);
        assertThat(lineNames).contains(신분당선.get(이름), 이호선.get(이름));
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        //given
        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성_요청(신분당선);

        // when
        final Long createdId = createResponse.jsonPath().getLong(아이디);
        ExtractableResponse<Response> searchResponse = LineSteps.지하철_노선_조회_요청(createdId);

        // then
        assertThat(searchResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성_요청(신분당선);

        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "구분당선");
        params.put("color", "bg-blue-600");

        // when
        final Long createdId = createResponse.jsonPath().getLong(아이디);
        ExtractableResponse<Response> response = LineSteps.지하철_노선_수정_요청(createdId, params);

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
    void deleteLine() {
        //given
        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성_요청(신분당선);

        // when
        final Long createdId = createResponse.jsonPath().getLong(아이디);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{id}", createdId)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
