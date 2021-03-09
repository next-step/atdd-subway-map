package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineSteps {

    public static String getResponseURI(ExtractableResponse<Response> response){

        if(response==null) {
            return null;
        }

        return response.header("Location");
    }

    public static ExtractableResponse<Response> 지하철_노선_등록(String name, String color){

        ExtractableResponse<Response> createResponse;
        Map<String, String> param1 = new HashMap<>();

        param1.put("color", color);
        param1.put("name", name);

        createResponse = RestAssured.given().log().all()
                .body(param1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        return createResponse;
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> request){
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete(getResponseURI(request))
                .then().log().all().extract();

        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> request){
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get(getResponseURI(request))
                .then().log().all().extract();
        return response;
    }

    public static ExtractableResponse<Response> 지하철_전체노선_조회_요청(ExtractableResponse<Response> request){
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get(getResponseURI(request).split("/")[1])
                .then().log().all().extract();
        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> request,Map<String, String> param){
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(getResponseURI(request))
                .then().log().all().extract();

        return response;
    }

}
