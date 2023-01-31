package subway;

import static subway.StationTestUtils.지하철역_생성;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.Line;
import subway.line.LineCreateRequest;
import subway.line.LinePatchRequest;

public class LineTestUtils {

  private LineTestUtils() {}

  public static Line 지하철_역_노선_모두_생성(String name, String color, String upStation, String downStation, Long distance) {
    Long upId = 지하철역_생성(upStation);
    Long downId = 지하철역_생성(downStation);
    return 지하철_노선_생성(name, color, upId, downId, distance);
  }

  public static Line 지하철_노선_생성(String name, String color, Long inbound, Long outbound, Long distance) {
    LineCreateRequest request = new LineCreateRequest(name, color, inbound, outbound, distance);

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

  public static Line 지하철_노선_수정(Long id, String name, String color) {
    LinePatchRequest request = new LinePatchRequest(name, color);

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
