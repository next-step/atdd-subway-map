package nextstep.subway.line.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class LineAcceptanceStep {

    public static ExtractableResponse<Response> 지하철_노선을_생성한다(String lines, String color, LocalTime startTime, LocalTime endTime, int interval) {
        Map<String, String> lineRequestParameterMap = getLineRequestParameterMap(lines, color, startTime, endTime, interval);
        return RestAssured.given().log().all().
                contentType(ContentType.JSON).
                body(lineRequestParameterMap).
                when().
                post("/lines").
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철_노선을_조회한다(Long lineId) {
        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines/{lineId}", lineId).
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 지하철_노선을_수정한다(String location, Map<String, String> updateLineRequestParams) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(updateLineRequestParams)
                .when()
                .put(location)
                .then()
                .log().all()
                .extract();
    }

    public static Map<String, String> getLineRequestParameterMap(String lines, String color, LocalTime startTime, LocalTime endTime, int interval) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lines);
        params.put("color", color);
        params.put("startTime", startTime.format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", endTime.format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", String.valueOf(interval));
        return params;
    }
}
