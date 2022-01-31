package nextstep.subway.acceptance.request;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineRequest {
  public static final String PATH_PREFIX = "/lines";
  public static final String NAME = "name";
  public static final String COLOR = "color";
  public static final String LOCATION = "Location";

  /** 반복되는 생성 코드를 줄이기 위해 createRequest 를 따로 작성 */
  public static ExtractableResponse<Response> lineCreateRequest(String name, String color, Long upStationId, Long downStationId, int distance) {

    Map<String, String> createRequest = new HashMap<>();
    createRequest.put(NAME, name);
    createRequest.put(COLOR, color);
    return RestAssured.given()
      .log()
      .all()
      .body(createRequest)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .when()
      .post(PATH_PREFIX)
      .then()
      .log()
      .all()
      .extract();
  }

  public static ExtractableResponse<Response> specificLineReadRequest(String url) {
    return RestAssured.given().log().all().when().get(url).then().log().all().extract();
  }

  public static ExtractableResponse<Response> lineUpdateRequest(String uri, String name, String color) {

    Map<String, String> updateRequest = new HashMap<>();
    updateRequest.put(NAME, name);
    updateRequest.put(COLOR, color);

    return RestAssured.given()
      .log()
      .all()
      .body(updateRequest)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .when()
      .put(uri)
      .then()
      .log()
      .all()
      .extract();
  }
}
