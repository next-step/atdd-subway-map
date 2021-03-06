package nextstep.subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class LineRequestBuilder {

  public static Map<String,String> createLineRequestParams(String name, LineColor color) {
    Map<String,String> params = new HashMap<>();
    params.put("name",name);
    params.put("color",LineColor.of(color));
    return params;
  }

  public static ExtractableResponse<Response> requestCreateLine(String name, LineColor color) {
    Map<String,String> params = createLineRequestParams(name,color);

    ExtractableResponse<Response> response =  RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .post("/lines")
        .then()
        .log().all()
        .extract();


    return response;

  }

  public static ExtractableResponse<Response> requestFindLines(){
    return RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/lines")
        .then().log().all().extract();
  }

  public static ExtractableResponse<Response> requestFindLine(long id){
    return RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/lines/{id}",id)
        .then().log().all().extract();
  }

  public static ExtractableResponse<Response> requestUpdateLine(Map params,long id){
    return RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE).body(params)
        .when().put("/lines/{id}",id)
        .then().log().all().extract();
  }

  public static ExtractableResponse<Response> requestDeleteLine(long id) {
    return RestAssured
        .given().log().all()
        .when().delete("/lines/{id}",id)
        .then().log().all().extract();
  }
}
