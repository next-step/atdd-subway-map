package nextstep.subway.model;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.model.StationEntitiesHelper.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public final class LineEntitiesHelper {

    private static final String REQUEST_URI = "/lines";

    public static ExtractableResponse<Response> 노선_생성_요청(Line line) {
        ExtractableResponse<Response> findUpStationResponse = 지하철역_이름으로_조회_요청(line.getUpStationName());
        Long upStationId = findUpStationResponse.statusCode() == BAD_REQUEST.value() ?
                지하철역_생성_요청(line.getUpStationName()).jsonPath().getLong("id") :
                findUpStationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> findDownStationResponse = 지하철역_이름으로_조회_요청(line.getDownStationName());
        Long downStationId = findDownStationResponse.statusCode() == BAD_REQUEST.value() ?
                지하철역_생성_요청(line.getDownStationName()).jsonPath().getLong("id") :
                findDownStationResponse.jsonPath().getLong("id");

        return RestAssured.given().log().all()
                .body(newLine(line, upStationId, downStationId))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(REQUEST_URI)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when()
                .get(REQUEST_URI)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_단건_조회_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_수정_요청(Line line, String uri) {
        ExtractableResponse<Response> upStationResponse = 지하철역_생성_요청(line.getUpStationName());
        ExtractableResponse<Response> downStationResponse = 지하철역_생성_요청(line.getDownStationName());

        Long upStationId = upStationResponse.jsonPath().getLong("id");
        Long downStationId = downStationResponse.jsonPath().getLong("id");

        return RestAssured.given().log().all()
                .body(newLine(line, upStationId, downStationId))
                .when()
                .contentType(APPLICATION_JSON_VALUE)
                .put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_삭제_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    private static Map<String, Object> newLine(Line line, Long upStationId, Long downStationId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", line.getDistance());
        return params;
    }
}
