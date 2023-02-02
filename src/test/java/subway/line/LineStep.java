package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

import static subway.line.LineConstant.*;
import static subway.line.LineConstant.BASE_URL;

public class LineStep {
    public static ExtractableResponse<Response> deleteLine(long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(DETAIL_URL, Map.of(ID, id))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateLine(long id, String name, String color) {
        LineUpdateRequest lineRequest = new LineUpdateRequest(name, color);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().put(DETAIL_URL, Map.of(ID, id))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getLine(long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(DETAIL_URL, Map.of(ID, id))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> createLineWithLineRequest(String name, String color, Long upStationId, Long downStationId) {
        LineCreateRequest lineRequest = new LineCreateRequest(name, color, upStationId, downStationId, DISTANCE_VALUE);
        ExtractableResponse<Response> response = createLine(lineRequest);
        return response;
    }

    public static ExtractableResponse<Response> createLine(LineCreateRequest lineRequest) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(BASE_URL)
                .then().log().all()
                .extract();
        return response;
    }

    public static long extractId(ExtractableResponse<Response> createLineResponse) {
        return createLineResponse.jsonPath().getLong(ID);
    }
}
