package subway.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineFixture {


    public static ExtractableResponse<Response> createLine(String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String,Object> param =new HashMap<>();
        param.put("name",name);
        param.put("color",color);
        param.put("upStationId",upStationId);
        param.put("downStationId",downStationId);
        param.put("distance",distance);

        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> getLines() {
        return RestAssured.given().log().all()
                .when().get("lines")
                .then().log().all()
                .extract();
    }
}
