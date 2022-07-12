package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class SubwayStationCommon {

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        return RestAssured.given().log().all()
                .body(지하철역_생성_요청_파리미터_생성(name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private static Map<String, String> 지하철역_생성_요청_파리미터_생성(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return params;
    }
}
