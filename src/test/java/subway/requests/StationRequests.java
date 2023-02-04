package subway.requests;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationRequests {
    public static ExtractableResponse<Response> 지하철역_생성_요청하기(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청하기() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }
    
    public static ExtractableResponse<Response> 지하철역_삭제_요청하기(Long stationId) {
        return RestAssured.given().log().all()
                .when().delete("/stations/" + stationId)
                .then().log().all()
                .extract();

    }
}
