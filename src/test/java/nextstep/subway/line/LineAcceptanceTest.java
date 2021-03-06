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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(createLine("bg-red-60", "신분당선"));

        // then
        // 지하철_노선_생성됨
        지하철_노선_생성됨(response);
    }

    public static Map<String, String> createLine(String color, String name){
        Map<String, String> params = new HashMap<>();
        params.put("color", color);
        params.put("name", name);
        return params;
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params){
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> response1 = 지하철_노선_등록되어_있음(createLine("bg-red-60", "신분당선"));
        ExtractableResponse<Response> response2 = 지하철_노선_등록되어_있음(createLine("bg-green-600", "2호선"));

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        // 지하철_노선_목록_응답됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        // 지하철_노선_목록_포함됨
        List<Long> expectedLineIds = Arrays.asList(response1, response2).stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath()
                .getList(".", LineResponse.class)
                .stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(Map<String, String> params){
        return 지하철_노선_생성_요청(params);
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(){
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> response = 지하철_노선_등록되어_있음(createLine("bg-red-60", "신분당선"));
        String uri = response.header("Location");

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> resultReponse = 지하철_노선_조회_요청(uri);

        // then
        // 지하철_노선_응답됨
        지하철_노선_응답됨(resultReponse);
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(String uri){
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public void 지하철_노선_응답됨(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class));
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> response = 지하철_노선_등록되어_있음(createLine("bg-red-60", "신분당선"));
        String uri = response.header("Location");

        // when
        // 지하철_노선_수정_요청
        ExtractableResponse<Response> resultResponse = 지하철_노선_수정_요청(createLine("bg-blue-600", "구분당선"), uri);

        // then
        // 지하철_노선_수정됨
        지하철_노선_수정됨(resultResponse);
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Map<String, String> params, String uri){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }


    public void 지하철_노선_수정됨(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> requestResponse = 지하철_노선_등록되어_있음(createLine("bg-red-60", "신분당선"));
        String uri = requestResponse.header("Location");

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(uri);

        // then
        // 지하철_노선_삭제됨
        지하철_노선_삭제됨(response);
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(String uri){
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
