package subway.helper;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineTestRequestHelper {

    public static ExtractableResponse<Response> 지하철노선_생성(String name, String color, Long upStationId, Long downStationId, Integer distance){
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철노선_조회(){
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_단건_조회(ExtractableResponse<Response> response){
        return RestAssured.given().log().all()
                .when().get("/lines" + "/"+ response.jsonPath().getLong("id"))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_삭제(ExtractableResponse<Response> response){
        return RestAssured.given().log().all()
                .when().delete("/lines" + "/"+ response.jsonPath().getLong("id"))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_수정(ExtractableResponse<Response> response, String name, String color){
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return RestAssured.given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines" + "/"+ response.jsonPath().getLong("id"))
                .then().log().all()
                .extract();
    }
}
