package subway.util;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.LineModifyRequest;
import subway.dto.LineRequest;

import java.util.HashMap;
import java.util.Map;

public class SubwayUtils {

    public static Response createLine(LineRequest lineRequest){

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

    public static Response modifyLine(Long id, LineModifyRequest req){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(req)
                .put("/lines/"+id);
    }
    
}
