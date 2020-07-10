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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LineAcceptanceStep {

    public static ExtractableResponse<Response> 노선_등록되어_있음(String name) {
        return 노선_생성_요청(name);
    }

    public static ExtractableResponse<Response> 노선_생성_요청(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", "GREEN");
        params.put("startTime", LocalTime.of(05, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", "5");

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines").
                then().
                log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_조회_요청(Long lineId) {
        return 노선_조회_요청(String.format("/lines/%d", lineId));
    }

    public static ExtractableResponse<Response> 노선_조회_요청(String uri) {
        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get(uri).
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 노선_목록_조회_요청() {
        return RestAssured.given().log().all().
                when().
                get("/lines").
                then().
                log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_수정_요청(String uri) {
        Map<String, String> params = new HashMap<>();
        params.put("name", "name2");
        params.put("color", "bg-red-602");
        params.put("startTime", LocalTime.of(02, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", LocalTime.of(22, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", "2");

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                put(uri).
                then().
                log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_제거_요청(String uri) {
        return RestAssured.given().log().all().
                when().
                delete(uri).
                then().
                log().all()
                .extract();
    }

    public static void 노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 노선_목록_포함됨(ExtractableResponse<Response> response, int expectedSize) {
        assertThat(response.body().jsonPath().getList("$").size()).isEqualTo(expectedSize);
    }

    public static void 노선_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    public static void 노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
