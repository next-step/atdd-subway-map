package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import org.springframework.http.MediaType;

public class SectionAcceptanceTestHelper {

    public static ExtractableResponse<Response> 구간_등록_요청(HashMap<String, String> params) {
        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/lines/1/sections")
                          .then().log().all()
                          .extract();
    }

    public static HashMap<String, String> 구간_파라미터_생성(String upStationId, String downStationId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", "10");
        return params;
    }

    static HashMap<String, String> 구간제거_파라미터_생성(String 신규하행ID) {
        HashMap<String, String> params = new HashMap<>();
        params.put("stationId", 신규하행ID);
        return params;
    }

    static ExtractableResponse<Response> 구간_제거_요청(HashMap<String, String> params) {
        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(
                              MediaType.APPLICATION_JSON_VALUE)
                          .when().delete("/lines/1/sections")
                          .then().log().all()
                          .extract();
    }
}
