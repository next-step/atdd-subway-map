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
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        Map<String, String> params = 노선_2호선();

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 노선 이름으로 노선을 생성한다.")
    @Test
    void createLineWithDuplicatedName() {
        // given
        지하철_노선_생성_요청(노선_2호선());

        // when
        ExtractableResponse<Response> response =  지하철_노선_생성_요청(노선_2호선());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    private Map<String, String> 노선_2호선() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-green-600");
        return params;
    }

    private Map<String, String> 노선_1호선() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "bg-blue-600");
        return params;
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철_노선_생성_요청(노선_2호선());
        ExtractableResponse<Response> createResponse2 = 지하철_노선_생성_요청(노선_1호선());

        // when
        ExtractableResponse<Response> getResponse = RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> expectedIds = Stream.of(createResponse1, createResponse2)
                                 .map(this::parseIdFromResponseHeader)
                                 .collect(Collectors.toList());
        List<Long> resultIds = getResponse.jsonPath().getList(".", LineResponse.class)
                               .stream()
                               .map(LineResponse::getId)
                               .collect(Collectors.toList());
        assertThat(resultIds).containsAll(expectedIds);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(노선_2호선());
        Long createId = parseIdFromResponseHeader(createResponse);

        // when
        ExtractableResponse<Response> getResponse = getLineById(createId);

        // then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        Long resultId = (getResponse.jsonPath().getObject(".", LineResponse.class)).getId();
        assertThat(resultId).isEqualTo(createId);
    }

    private ExtractableResponse<Response> getLineById(Long id) {
        return RestAssured.given().log().all()
                .when()
                .get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private Long parseIdFromResponseHeader(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(노선_2호선());
        Long createId = parseIdFromResponseHeader(createResponse);

        // when
        ExtractableResponse<Response> modifyResponse = RestAssured.given().log().all()
            .when()
            .body(노선_1호선())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .put("lines/" + createId)
            .then().log().all()
            .extract();

        // then
        assertThat(modifyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(노선_2호선());
        Long createId = parseIdFromResponseHeader(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .when()
                .delete("lines/" + createId)
                .then().log().all()
                .extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }
}
