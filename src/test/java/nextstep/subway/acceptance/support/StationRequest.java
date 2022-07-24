package nextstep.subway.acceptance.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StationRequest {
    public static long 지하철역_생성_요청후_식별자_반환(String stationName){
        return 지하철역_생성_요청(stationName).jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/stations")
                          .then().log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록조회_요청() {
        return RestAssured.given().log().all()
                          .when().get("/stations")
                          .then().log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청(long stationId) {
        return RestAssured.given().log().all()
                          .when().delete("/stations/" + stationId)
                          .then().log().all()
                          .extract();
    }
}
