package nextstep.subway.test.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author a1101466 on 2022/07/10
 * @project subway
 * @description
 */
public class StationRequest {
    public static ExtractableResponse<Response> 지하철역_생성(String stationName){
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract()
                ;
    }

    public static List<String> 지하철목록조회(){
        return RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }
}
