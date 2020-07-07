package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LineAcceptanceStep {
    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(String name, String color, LocalTime startTime, LocalTime endTime, int intervalTime) {
        return 지하철_노선_생성_요청(name, color, startTime, endTime, intervalTime);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color, LocalTime startTime, LocalTime endTime, int intervalTime) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("startTime", startTime.format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", endTime.format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", String.valueOf(intervalTime));

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
                when().
                get("/lines").
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_상세정보_조회_요청(ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header(HttpHeaders.LOCATION);

        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get(uri).
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> createResponse, String name, String color,
                                                             LocalTime startTime, LocalTime endTime, int intervalTime) {
        final String uri = createResponse.header(HttpHeaders.LOCATION);

        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("name", name);
        updateParams.put("color", color);
        updateParams.put("startTime", startTime.format(DateTimeFormatter.ISO_TIME));
        updateParams.put("endTime", endTime.format(DateTimeFormatter.ISO_TIME));
        updateParams.put("intervalTime", String.valueOf(intervalTime));

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(updateParams).
                when().
                put(uri).
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(ExtractableResponse<Response> createResponse) {
        final String uri = createResponse.header(HttpHeaders.LOCATION);

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                delete(uri).
                then().
                log().all().
                extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotBlank();
    }

    public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, int expectedSize) {
        assertThat(response.as(ArrayList.class)).isNotNull()
                .hasSize(expectedSize);
    }

    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> afterUpdateResponse) {
        assertThat(afterUpdateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());


    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
