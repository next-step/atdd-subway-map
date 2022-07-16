package nextstep.subway.acceptance.template;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class SectionRequestTemplate {
    public static ExtractableResponse<Response> 지하철구간_등록을_요청한다(long lineId, long upStationId, long downStationId,
                                                               long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("downStationId", downStationId);
        params.put("upStationId", upStationId);
        params.put("distance", distance);

        return 지하철구간_등록을_요청한다(lineId, params);
    }

    public static ExtractableResponse<Response> 지하철구간_등록을_요청한다(long lineId, Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }
}
