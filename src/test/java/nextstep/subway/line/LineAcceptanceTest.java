package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Line;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_등록되어_있음("신분당선", "bg-red-600");

        // then
        // 지하철_노선_생성됨
        지하철_노선_목록_응답됨(response, HttpStatus.CREATED);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600");
        ExtractableResponse<Response> createResponse2 = 지하철_노선_등록되어_있음("2호선", "bg-green-600");

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        // 지하철_노선_목록_응답됨
        지하철_노선_목록_응답됨(response, HttpStatus.OK);

        //지하철_노선_목록_포함됨
        List<Long> createdLineIds = 지하철_노선_아이디_추출(Arrays.asList(createResponse1, createResponse2));
        List<Long> resultLineIds = 지하철_노선_객체_리스트_반환(response);

        지하철_노선_목록_포함됨(createdLineIds, resultLineIds);
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all().extract();

    }

    private List<Long> 지하철_노선_객체_리스트_반환(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", Line.class).stream()
                .map(Line::getId)
                .collect(Collectors.toList());
    }

    private ListAssert<Long> 지하철_노선_목록_포함됨(List<Long> createdLineIds, List<Long> resultLineIds) {
        return Assertions.assertThat(resultLineIds).containsAll(createdLineIds);
    }

    private void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response, HttpStatus status) {
        Assertions.assertThat(response.statusCode()).isEqualTo(status.value());
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600");

        Long createdId = 지하철_노선_아이디_추출(createdResponse1);

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createdId);

        // then
        // 지하철_노선_응답됨
        지하철_노선_응답됨(response, HttpStatus.OK);
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        String path = String.format("/lines/%d", id);

        return RestAssured
                .given().log().all()
                .when().get(path)
                .then().log().all().extract();
    }

    private void 지하철_노선_응답됨(ExtractableResponse<Response> response, HttpStatus status) {
        Assertions.assertThat(response.statusCode()).isEqualTo(status.value());
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음

        // when
        // 지하철_노선_수정_요청

        // then
        // 지하철_노선_수정됨
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음

        // when
        // 지하철_노선_제거_요청

        // then
        // 지하철_노선_삭제됨
    }

    private ExtractableResponse<Response> 지하철_노선_등록되어_있음(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    private List<Long> 지하철_노선_아이디_추출(List<ExtractableResponse<Response>> list) {
        return list.stream()
                .map(this::지하철_노선_아이디_추출)
                .collect(Collectors.toList());
    }

    private Long 지하철_노선_아이디_추출(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

}
