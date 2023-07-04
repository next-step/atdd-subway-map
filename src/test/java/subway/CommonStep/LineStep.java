package subway.CommonStep;

import io.restassured.path.json.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineStep {

    public static ExtractableResponse<Response> 지하철_노선_생성(String stationName1,String stationName2,String lineName,String color,Long distance){
        Long stationId1 = StationStep.지하철역_생성( stationName1).body().as(StationResponse.class).getId();
        Long stationId2  = StationStep.지하철역_생성( stationName2).body().as(StationResponse.class).getId();

        Map<String, String> params = new HashMap<>();
        params.put("name",lineName);
        params.put("color",color);
        params.put("upStationId",String.valueOf(stationId1));
        params.put("downStationId",String.valueOf(stationId2));
        params.put("distance",String.valueOf(distance));

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        return response;
    }

    public static List<String> 지하철노선_목록_전체조회() {

        List<String> lineNames =
                RestAssured.given()
                        .when().get("/lines")
                        .then()
                        .extract().jsonPath().getList("name", String.class);

        return lineNames;
    }

    public static JsonPath 지하철_노선_조회(Long lineId){

        JsonPath result =
                RestAssured.given().log().all()
                        .when().get("/lines/"+lineId)
                        .then().log().all()
                        .extract().jsonPath();

        return result;
    }
}
