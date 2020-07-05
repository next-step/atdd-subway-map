package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class LineAcceptanceStep {
    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(String name, String color,
                                                               LocalTime startTime, LocalTime endTime, int intervalTime) {
        return 지하철_노선_생성_요청(name, color, startTime, endTime, intervalTime);
    }
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color,
                                                            LocalTime startTime, LocalTime endTime, int intervalTime) {
        Map<String, String> params = 지하철_노선_요청값(name, color, startTime, endTime, intervalTime);

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines").
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines").
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");
        long id = Long.parseLong(uri.split("/lines/")[1]);

        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines/" + id).
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> createResponse,
                                                             String name, String color,
                                                             LocalTime startTime, LocalTime endTime, int intervalTime) {
        String uri = createResponse.header("Location");
        long id = Long.parseLong(uri.split("/lines/")[1]);

        Map<String, String> params = 지하철_노선_요청값(name, color, startTime, endTime, intervalTime);

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                put("/lines/" + id).
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");
        long id = Long.parseLong(uri.split("/lines/")[1]);

        return RestAssured.given().log().all().
                when().
                delete("/lines/" + id).
                then().
                log().all().
                extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response,
                                     List<ExtractableResponse<Response>> createResponses) {
        final List<Long> expectedLineIds = createResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/lines/")[1]))
                .collect(toList());

        final List<Long> actualLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(it -> it.getId())
                .collect(toList());

        assertThat(actualLineIds).containsAll(expectedLineIds);
    }

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private static Map<String, String> 지하철_노선_요청값(String name, String color,
                                                 LocalTime startTime, LocalTime endTime, int intervalTime) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("startTime", startTime.format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", endTime.format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", String.valueOf(intervalTime));
        return params;
    }
}
