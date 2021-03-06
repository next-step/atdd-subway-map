package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Line;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private Map<String, String> 신분당선;
    private Map<String, String> 이호선;

    @BeforeEach
    void setup() {
        신분당선 = new HashMap<String, String>() {{
            put("name", "신분당선");
            put("color", "bg-red-600");
        }};
        이호선 = new HashMap<String, String>() {{
            put("name", "2호선");
            put("color", "bg-green-600");
        }};
    }


    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_등록(신분당선);

        // then
        응답_결과_확인(response, HttpStatus.CREATED);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철_노선_등록(신분당선);
        ExtractableResponse<Response> createResponse2 = 지하철_노선_등록(이호선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        응답_결과_확인(response, HttpStatus.OK);

        List<Long> createdLineIds = 지하철_노선_아이디_추출(createResponse1, createResponse2);
        List<Long> resultLineIds = 지하철_노선_객체_리스트_반환(response);

        지하철_노선_목록_포함됨(createdLineIds, resultLineIds);
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get(서비스_호출_경로_생성(null))
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

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_등록(신분당선);

        Long createdId = 지하철_노선_아이디_추출(createdResponse1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createdId);

        // then
        응답_결과_확인(response, HttpStatus.OK);
    }

    private void 응답_결과_확인(ExtractableResponse<Response> response, HttpStatus status) {
        Assertions.assertThat(response.statusCode()).isEqualTo(status.value());
    }


    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_등록(신분당선);

        // when
        HashMap<String, String> params = new HashMap<>();
        params.put("name", "구분당선");
        params.put("color", "bg-blue-600");
        Long createdId = 지하철_노선_아이디_추출(createdResponse1);

        ExtractableResponse<Response> response1 = 지하철_노선_수정_요청(createdId, params);
        ExtractableResponse<Response> response2 = 지하철_노선_조회_요청(createdId);

        // then
        지하철_노선_수정됨(response1);
        지하철_노선_수정_확인(response2, params);
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(Long createdId, HashMap<String, String> params) {
        String path = 서비스_호출_경로_생성(createdId);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path)
                .then().log().all().extract();
    }

    private void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선_수정_확인(ExtractableResponse<Response> response, HashMap<String, String> params) {
        Line line = response.jsonPath().getObject(".", Line.class);

        Assertions.assertThat(line.getName()).isEqualTo(params.get("name"));
        Assertions.assertThat(line.getColor()).isEqualTo(params.get("color"));
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        String path = 서비스_호출_경로_생성(id);

        return RestAssured
                .given().log().all()
                .when().get(path)
                .then().log().all().extract();
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_등록(신분당선);
        Long createdId = 지하철_노선_아이디_추출(createdResponse1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createdId);

        // then
        지하철_노선_삭제됨(response);
    }

    private ExtractableResponse<Response> 지하철_노선_제거_요청(Long createdId) {
        String path = 서비스_호출_경로_생성(createdId);
        return RestAssured
                .given().log().all()
                .when().delete(path)
                .then().log().all().extract();
    }

    private void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 지하철_노선_등록(Map<String, String> params) {
        return 지하철_노선_등록(params.get("name"), params.get("color"));
    }

    private ExtractableResponse<Response> 지하철_노선_등록(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(서비스_호출_경로_생성(null))
                .then().log().all().extract();
    }

    private List<Long> 지하철_노선_아이디_추출(ExtractableResponse<Response>... list) {
        return Stream.of(list)
                .map(this::지하철_노선_아이디_추출)
                .collect(Collectors.toList());
    }

    private Long 지하철_노선_아이디_추출(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    private String 서비스_호출_경로_생성(Long createdId) {
        String path = "/lines";
        if (Objects.nonNull(createdId)) {
            return path + "/" + createdId;
        }

        return path;
    }

}
