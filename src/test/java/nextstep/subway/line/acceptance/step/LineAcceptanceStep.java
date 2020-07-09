package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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
}
