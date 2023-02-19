package subway.stations;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationRestAssured {

    private static final String API_PATH = "/stations";

    public static ExtractableResponse<Response> 지하철역_생성(String name) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);

        return RestAssured.given()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(API_PATH)
                .then().log().all()
                .extract();
    }

    public static Long 지하철역_ID_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }
}
