package nextstep.subway.acceptance.request;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationRequest {
  private static final String NAME = "name";
  public static final String PATH_PREFIX = "/stations";

  public static ExtractableResponse<Response> stationCreateRequest(String name) {

    Map<String, String> params = new HashMap<>();
    params.put(NAME, name);

    return RestAssured.given()
      .log()
      .all()
      .body(params)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .when()
      .post(PATH_PREFIX)
      .then()
      .log()
      .all()
      .extract();
  }
}
