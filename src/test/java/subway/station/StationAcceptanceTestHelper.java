package subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class StationAcceptanceTestHelper {

    static Map<String, String> 지하철_파라미터_생성(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }

    static ExtractableResponse<Response> 지하철역_조회_요청() {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().get("/stations")
                          .then().log().all()
                          .extract();
    }

    static ExtractableResponse<Response> 지하철역_제거_요청(Long id) {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().delete("/stations/" + id)
                          .then().log().all()
                          .extract();
    }
}
