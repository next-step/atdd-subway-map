package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineCreateRequest;
import nextstep.subway.applicaion.dto.LineCreateResponse;
import nextstep.subway.applicaion.dto.LineResponse;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LineSteps {
    public static final String DEFAULT_PATH = "/lines";

    public static Map<String, String> getParams(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return params;
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(DEFAULT_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청2(
            String name,
            String color,
            String upStationId,
            String downStationId,
            String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);


        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(DEFAULT_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static LineResponse lineCreateResponseConvertToLineResponse(LineCreateResponse lineCreateResponse) {
        return new LineResponse(
                lineCreateResponse.getId(),
                lineCreateResponse.getName(),
                lineCreateResponse.getColor(),
                Collections.EMPTY_LIST,
                lineCreateResponse.getCreatedDate(),
                lineCreateResponse.getModifiedDate());
    }
}
