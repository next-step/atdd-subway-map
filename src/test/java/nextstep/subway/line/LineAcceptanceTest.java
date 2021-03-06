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
        Map<String, String> params = createLineTwo();

        // when
        ExtractableResponse<Response> response = createLineWithParameter(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }


    private ExtractableResponse<Response> createLineWithParameter(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    // fixtures
    private Map<String, String> createLineTwo() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-green-600");
        return params;
    }

    private Map<String, String> createLineOne() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "bg-blue-600");
        return params;
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = createLineWithParameter(createLineTwo());
        ExtractableResponse<Response> createResponse2 = createLineWithParameter(createLineOne());

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
        ExtractableResponse<Response> createResponse = createLineWithParameter(createLineTwo());
        Long createId = parseIdFromResponseHeader(createResponse);

        // when
        ExtractableResponse<Response> getResponse = getLineById(createId);

        // then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> resultIds = getResponse.jsonPath().getList(".", LineResponse.class)
                .stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultIds).contains(createId);
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
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse = createLineWithParameter(createLineTwo());
        Long createId = parseIdFromResponseHeader(createResponse);

        // when
        // 지하철_노선_수정_요청
        ExtractableResponse<Response> modifyResponse = RestAssured.given().log().all()
            .when()
            .put("lines/" + createId)
            .then().log().all()
            .extract();

        // then
        // 지하철_노선_수정됨
        assertThat(modifyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse = createLineWithParameter(createLineTwo());
        Long createId = parseIdFromResponseHeader(createResponse);

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> deleteRespones = RestAssured.given().log().all()
                .when()
                .delete("lines/" + createId)
                .then().log().all()
                .extract();

        // then
        // 지하철_노선_삭제됨
        assertThat(deleteRespones.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }
}
