package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.Line;
import subway.line.LineRequest;
import subway.station.Station;

public class LineTestUtils {
  public static Line 지하철_노선_생성(String name, Station inbound, Station outbound) {
    LineRequest request = new LineRequest(name, inbound, outbound);

    return RestAssured
        .given().body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines")
        .then()
        .extract().body().as(Line.class);
  }

  public static ExtractableResponse<Response> 지하철_노선_조회(Long id) {
    return RestAssured
        .given().contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/lines/" + id)
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> 지하철_노선_목록_조회() {
    return RestAssured
        .given().contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/lines")
        .then()
        .extract();
  }

  public static Line 지하철_노선_수정(Long id, String name, Station inbound, Station outbound) {
    LineRequest request = new LineRequest(name, inbound, outbound);

    return RestAssured
        .given().body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().patch("/lines/" + id)
        .then()
        .extract().body().as(Line.class);
  }

  public static void 지하철_노선_삭제(Long id) {
    RestAssured
        .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().delete("/lines/" + id);
  }
}
