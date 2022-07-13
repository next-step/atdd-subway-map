package nextstep.subway.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class StationRestAssured {

  public ExtractableResponse<Response> saveStation(String name) {
    Map<String, String> params = new HashMap<>();
    params.put("name", name);

    return RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/stations")
        .then().log().all()
        .extract();
  }

  public ExtractableResponse<Response> findAllStations() {
    return RestAssured.given().log().all()
        .when().get("/stations")
        .then().log().all()
        .extract();
  }
}
