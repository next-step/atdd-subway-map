package subway.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class StationFixture {

  public static ExtractableResponse<Response> 지하철역_생성_요청(String stationName) {
    // when
    Map<String, String> params = new HashMap<>();
    params.put("name", stationName);

    return RestAssured.given()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/stations")
        .then()
        .extract();
  }
}
