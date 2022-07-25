package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineTestFixtures {
    public static ExtractableResponse<Response> 노선_생성(String name, String color, String upStationId, String downStationId, String distance) {
        Map<String, String> lineParams = new HashMap<>();
        lineParams.put("name", name);
        lineParams.put("color", color);
        lineParams.put("upStationId", upStationId);
        lineParams.put("downStationId", downStationId);
        lineParams.put("distance", distance);

        return RestAssured
                .given().log().all()
                .body(lineParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_조회() {
        return RestAssured
                    .given().log().all()
                    .when()
                    .get("/lines")
                    .then().log().all()
                    .extract();
    }

    public static ExtractableResponse<Response> 노선_조회(ExtractableResponse<Response> 라인) {
        String id = 라인.jsonPath().getString("id");

        return RestAssured
                    .given().log().all()
                    .pathParam("id", id)
                    .when()
                    .get("/lines/{id}")
                    .then().log().all()
                    .extract();
    }

    public static ExtractableResponse<Response> updateLine(String name, String color, ExtractableResponse<Response> 라인) {
        Map<String, String> updateParam = new HashMap<>();
        updateParam.put("name", "구미선");
        updateParam.put("color", "bg-blue-30000");

        String id = 라인.jsonPath().getString("id");

        //수정
         return RestAssured
                    .given().log().all()
                    .pathParam("id",id)
                    .body(updateParam)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .put("/lines/{id}")
                    .then().log().all()
                    .extract();
    }

    public static ExtractableResponse<Response> deleteLine(String id) {
        return RestAssured
                    .given().log().all()
                    .pathParam("id", id)
                    .when()
                    .delete("/lines/{id}")
                    .then().log().all()
                    .extract();
    }
}
