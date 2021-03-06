package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class StationRequestBuilder {

  public static Map<String,String> createStationRequestParams(String name) {
    Map<String, String> params = new HashMap<>();
    params.put("name", name);
    return params;
  }

  public static ExtractableResponse<Response> requestCreateStation(String name) {
    return RestAssured.given().log().all()
        .body(createStationRequestParams(name))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .post("/stations")
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> requestFindStations() {
    return RestAssured.given().log().all()
        .when()
        .get("/stations")
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> requestRemoveStation(String uri) {
   return RestAssured.given().log().all()
        .when()
        .delete(uri)
        .then().log().all()
        .extract();
  }
}
