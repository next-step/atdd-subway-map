package subway.common;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class SectionApiTest {
    public static ExtractableResponse<Response> 지하철구간을_생성한다(Map<String, Object> param) {
        Map<String, Object> sectionParam = new HashMap<>();
        sectionParam.put("upStationId", param.get("upStationId"));
        sectionParam.put("downStationId", param.get("downStationId"));
        sectionParam.put("distance", param.get("distance"));

        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(sectionParam)
                .when().post("/sections")
                .then().log().all()
                .extract();

        return response;
    }
}
