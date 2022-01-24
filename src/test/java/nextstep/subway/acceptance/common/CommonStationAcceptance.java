package nextstep.subway.acceptance.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class CommonStationAcceptance {

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        return RestAssured
                .given().log().all()
                .body(getParamsStationMap(name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all().extract();
    }

    public static Map<String, String> getParamsStationMap(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }

    public static ExtractableResponse<Response> 지하철역_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/stations")
                .then().log().all().extract();
    }

}
