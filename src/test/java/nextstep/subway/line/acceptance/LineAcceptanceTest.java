package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        // 지하철_노선_생성_요청
        Map<String, String> params = createLineRequestParams("신분당선", "bg-red-600", LocalTime.of(05, 30), LocalTime.of(23, 30), "5");

        ExtractableResponse<Response> response = createLineRequest(params);

        // then
        // 지하철_노선_생성됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLine2() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params = createLineRequestParams("신분당선", "bg-red-600", LocalTime.of(05, 30),  LocalTime.of(23, 30), "5");

        createLineRequest(params);

        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = createLineRequest(params);

        // then
        // 지하철_노선_생성_실패됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params = createLineRequestParams("신분당선", "bg-red-600", LocalTime.of(05, 30),  LocalTime.of(23, 30), "5");

        createLineRequest(params);
        // 지하철_노선_등록되어_있음
        Map<String, String> params2 = createLineRequestParams("2호선", "bg-green-600", LocalTime.of(05, 30),  LocalTime.of(23, 30), "5");
        createLineRequest(params2);

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> response = readLinesRequest();

        // then
        // 지하철_노선_목록_응답됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        // 지하철_노선_목록_포함됨
        assertThat(response).isNotNull();
        assertThat(response.body().asString()).contains("신분당선").contains("2호선");
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params = createLineRequestParams("신분당선", "bg-red-600", LocalTime.of(05, 30),  LocalTime.of(23, 30), "5");

        ExtractableResponse<Response> createResponse = createLineRequest(params);

        // when
        // 지하철_노선_조회_요청
        String uri = createResponse.header("Location");

        Long id = extractLineId(uri);
        ExtractableResponse<Response> response = readLineRequest(id);

        // then
        // 지하철_노선_응답됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params = createLineRequestParams("신분당선", "bg-red-600", LocalTime.of(05, 30),  LocalTime.of(23, 30), "5");

        ExtractableResponse<Response> createResponse = createLineRequest(params);

        // when
        // 지하철_노선_수정_요청
        String uri = createResponse.header("Location");
        Long id = extractLineId(uri);

        Map<String, String> updateParams = createLineRequestParams("신분당선", "bg-red-600", LocalTime.of(05, 30),  LocalTime.of(23, 30), "10");
        ExtractableResponse<Response> updateResponse = updateLineRequest(updateParams, id);

        // then
        // 지하철_노선_수정됨
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params = createLineRequestParams("신분당선", "bg-red-600", LocalTime.of(05, 30),  LocalTime.of(23, 30), "5");

        ExtractableResponse<Response> createResponse = createLineRequest(params);

        // when
        // 지하철_노선_제거_요청
        String uri = createResponse.header("Location");
        Long id = extractLineId(uri);

        ExtractableResponse<Response> deleteResponse = deleteLineRequest(id);

        // then
        // 지하철_노선_삭제됨
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Map<String, String> createLineRequestParams(String name, String color, LocalTime startTime, LocalTime endTime, String intervalTime) {
        Map<String, String> params = new HashMap<>();

        params.put("name", name);
        params.put("color", color);
        params.put("startTime", startTime.format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", intervalTime);

        return params;
    }

    private ExtractableResponse<Response> createLineRequest(Map<String, String> params) {
        return RestAssured.given().log().all().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(params).
                    when().
                    post("/lines").
                    then().
                    log().all().
                    extract();
    }

    private ExtractableResponse<Response> updateLineRequest(Map<String, String> params, Long id) {
        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                put("/lines/" + id).
                then().
                log().all().
                extract();
    }

    private ExtractableResponse<Response> readLinesRequest() {
        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines").
                then().
                log().all().
                extract();
    }

    private ExtractableResponse<Response> readLineRequest(Long id) {
        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines/" + id).
                then().
                log().all().
                extract();
    }

    private ExtractableResponse<Response> deleteLineRequest(Long id) {
        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                delete("/lines/" + id).
                then().
                log().all().
                extract();
    }

    private Long extractLineId(String uri) {
        return Long.valueOf(uri.split("/lines/")[1]);
    }
}
