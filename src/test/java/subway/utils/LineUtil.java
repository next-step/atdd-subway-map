package subway.utils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static subway.utils.AssertUtil.*;

public class LineUtil {

    public static JsonPath createLineResultResponse(String lineName, String color, Long upStationId, Long downStationId, int distance) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all().body(getCreateParam(lineName, color, upStationId, downStationId, distance)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        assertSuccessCreate(response);

        return response.body().jsonPath();
    }

    private static Map<String, Object> getCreateParam(String lineName, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", lineName);
        param.put("color", color);
        param.put("upStationId", upStationId);
        param.put("downStationId", downStationId);
        param.put("distance", distance);

        return param;
    }
}
