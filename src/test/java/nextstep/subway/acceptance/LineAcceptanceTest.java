package nextstep.subway.acceptance;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.subway.applicaion.dto.ShowLineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private static final String SHINBUNDANG_NAME = "신분당선";

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        Map<String, String> shinbundangLine = createShinbundangLine();

        // when
        ExtractableResponse<Response> response = callCreateLines(shinbundangLine);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank()
            .isEqualTo("/lines/1");
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
        Map<String, String> shinbundangLine = createShinbundangLine();
        Map<String, String> number2Line = createNumber2Line();

        // when
        ExtractableResponse<Response> shinbundangResponse = callCreateLines(shinbundangLine);
        ExtractableResponse<Response> number2Response = callCreateLines(number2Line);

        // then
        assertThat(shinbundangResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(number2Response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(shinbundangResponse.header("Location")).isNotBlank()
            .isEqualTo("/lines/1");
        assertThat(number2Response.header("Location")).isNotBlank()
            .isEqualTo("/lines/2");
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
        Map<String, String> shinbundangLine = createShinbundangLine();
        callCreateLines(shinbundangLine);

        // when
        ExtractableResponse<Response> response = callGetLines();

        // then
        List<String> lineNames = response.jsonPath()
            .getList(".", ShowLineResponse.class)
            .stream()
            .map(ShowLineResponse::getLineName)
            .collect(toList());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isNotNull();
        assertThat(lineNames).contains(SHINBUNDANG_NAME);
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
        Map<String, String> shinbundangLine = createShinbundangLine();
        callCreateLines(shinbundangLine);

        Map<String, String> param = new HashMap<>();
        param.put("id", "1");
        param.put("name", "구분당선");
        param.put("color", "blue");

        // when
        ExtractableResponse<Response> responseUpdate = callUpdateLines(param);

        // then
        ExtractableResponse<Response> response = callGetLines();
        List<String> lineNames = response.jsonPath()
            .getList(".", ShowLineResponse.class)
            .stream()
            .map(ShowLineResponse::getLineName)
            .collect(toList());

        assertThat(responseUpdate.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(lineNames).contains("구분당선");
        assertThat(lineNames).doesNotContain(SHINBUNDANG_NAME);
    }

    /**
     * When 없는 지하철 노선의 정보 수정을 요청 하면
     * Then 400 응답
     */
    @DisplayName("지하철 노선 수정 요청 시 노선을 못 찾으면 400응답 처리")
    @Test
    void updateLine_fail() {
        // given
        Map<String, String> param = new HashMap<>();
        param.put("id", "1");
        param.put("name", "구분당선");
        param.put("color", "blue");

        // when
        ExtractableResponse<Response> response = callUpdateLines(param);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
    }

    private ExtractableResponse<Response> callCreateLines(Map<String, String> lineParams) {
        return RestAssured.given()
            .log()
            .all()
            .body(lineParams)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("lines")
            .then()
            .log()
            .all()
            .extract();
    }

    private ExtractableResponse<Response> callGetLines() {
        return RestAssured.given()
            .log()
            .all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("lines")
            .then()
            .log()
            .all()
            .extract();
    }

    private ExtractableResponse<Response> callUpdateLines(Map<String, String> lineParams) {
        return RestAssured.given()
            .log()
            .all()
            .body(lineParams)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put("lines/" + lineParams.get("id"))
            .then()
            .log()
            .all()
            .extract();
    }


    private Map<String, String> createShinbundangLine() {
        Map<String, String> result = new HashMap();
        result.put("name", SHINBUNDANG_NAME);
        result.put("color", "red");

        return result;
    }

    private Map<String, String> createNumber2Line() {
        Map<String, String> result = new HashMap();
        result.put("name", "2호선");
        result.put("color", "green");

        return result;
    }

}
