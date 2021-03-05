package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void createLine() {
        // when
        ExtractableResponse<Response> response = requestCreateLineDx();

        // then
        assertCreateLine(response);
    }

    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = requestCreateLineDx();
        ExtractableResponse<Response> createResponse2 = requestCreateLine2();

        // when
        ExtractableResponse<Response> response = requestGetLines();

        // then
        assertGetLines(response);
        assertIncludeLines(createResponse1, createResponse2, response);
    }

    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = requestCreateLineDx();

        // when
        ExtractableResponse<Response> response = requestGetLine(createResponse);

        // then
        assertGetLine(response);
    }

    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = requestCreateLineDx();

        // when
        ExtractableResponse<Response> response = requestUpdateLine(createResponse);

        // then
        assertUpdateLine(response);
    }

    @Test
    @DisplayName("지하철 노선을 제거한다.")
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = requestCreateLineDx();

        // when
        ExtractableResponse<Response> response = requestDeleteLine(createResponse);

        // then
        assertDeleteLine(response);
    }

    private ExtractableResponse<Response> requestCreateLineDx() {
        Map<String, String> params = makeLineParams("bg-red-600", "신분당선");
        return requestCreateLine(params);
    }

    private ExtractableResponse<Response> requestCreateLine2() {
        Map<String, String> params = makeLineParams("bg-green-600", "2호선");
        return requestCreateLine(params);
    }

    private ExtractableResponse<Response> requestCreateLine(Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestGetLines() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestGetLine(ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestUpdateLine(ExtractableResponse<Response> createResponse) {
        Map<String, String> newParams = makeLineParams("bg-blue-600", "구분당선");
        String uri = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .body(newParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestDeleteLine(ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    private void assertCreateLine(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void assertGetLines(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void assertIncludeLines(ExtractableResponse<Response> createResponse1, ExtractableResponse<Response> createResponse2, ExtractableResponse<Response> response) {
        List<Long> expectedIds = Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultIds).containsAll(expectedIds);
    }

    private void assertGetLine(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void assertUpdateLine(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void assertDeleteLine(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Map<String, String> makeLineParams(String color, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("color", color);
        params.put("name", name);
        return params;
    }
}
