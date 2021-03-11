package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationHelper {
    public static ExtractableResponse<Response> 지하철역_생성_요청(String name){
        return RestAssured.given().log().all()
                .body(파라미터_생성(name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    private static Map<String, String> 파라미터_생성(String name){
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        return param;
    }

    public static Long 생성된_지하철역_ID_가져오기(ExtractableResponse<Response> createResponse){
        return Long.parseLong(
                createResponse
                        .header("Location")
                        .split("/")[2]
        );
    }
}
