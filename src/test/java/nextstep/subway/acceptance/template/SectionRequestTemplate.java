package nextstep.subway.acceptance.template;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SectionRequestTemplate {
    public static ExtractableResponse<Response> 지하철구간_등록을_요청한다(long downStationId, long upStationId, long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("downStationId", downStationId);
        params.put("upStationId", upStationId);
        params.put("distance", distance);

        return 지하철구간_등록을_요청한다(params);
    }

    public static ExtractableResponse<Response> 지하철구간_등록을_요청한다(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/sections/")
                .then().log().all()
                .extract();
    }
}
