package subway.util;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SubwayUtils {

    public static Response createLine(Map<String, String> lineRequest){

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().body(lineRequest)
                .post("/lines");
    }

    public static Response createStation(String name){
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().body(params)
                .post("/stations");
    }

    public static Response getAllLines(){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines");
    }

    public static Response getLine(Long id){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines/"+id);
    }

    public static Response modifyLine(Long id, Map<String, String> req){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(req)
                .put("/lines/"+id);
    }
    public static Response createSection(Long id, Map<String, String> request){

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().body(request)
                .post("/lines/"+id+"/sections");
    }

    public static Response getSections(Long id){

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines/"+id+"/sections");
    }

    public static Response deleteSections(Long id, Map<String, String> request){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().body(request)
                .delete("/lines/"+id+"/sections");
    }

    public static Map<String, String> createLineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, String> map = new HashMap<>();
        map.put("name",name);
        map.put("color", color);
        map.put("upStationId", String.valueOf(upStationId));
        map.put("downStationId", String.valueOf(downStationId));
        map.put("distance", String.valueOf(distance));
        return map;

    }
    public static Map<String, String> createModifyRequest(String name, String color) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("color", color);
        return map;
    }
}
