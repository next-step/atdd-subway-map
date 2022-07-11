package nextstep.subway.acceptance.template;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class StationRequestTemplate {
    public static ExtractableResponse<Response> 지하철역_생성을_요청한다(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록을_조회한다() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_삭제를_요청한다(long stationId) {
        return RestAssured.given().log().all()
                .when().delete("/stations/" + stationId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역을_생성한다(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return 지하철역_생성을_요청한다(params);
    }
}
