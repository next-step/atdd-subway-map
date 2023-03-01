package subway.line;

import static subway.station.StationTestUtils.지하철역_생성;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.LineTestDTO.LineStationCreateDTO;

public class LineTestUtils {

  private LineTestUtils() {}

  public static LineResponse 역_과_노선_생성(LineStationCreateDTO request) {
    Long upId = 지하철역_생성(request.getUpStation());
    Long downId = 지하철역_생성(request.getDownStation());
    return 지하철_노선_생성(
        new LineCreateRequest(
            request.getName(), request.getColor(), upId, downId, request.getDistance()
        )
    );
  }

  public static LineResponse 지하철_노선_생성(LineCreateRequest request) {
    return RestAssured
        .given().body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines")
        .then()
        .extract().body().as(LineResponse.class);
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

  public static LineResponse 지하철_노선_수정(Long id, String name, String color) {
    LinePatchRequest request = new LinePatchRequest(name, color);

    return RestAssured
        .given().body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().patch("/lines/" + id)
        .then()
        .extract().body().as(LineResponse.class);
  }

  public static void 지하철_노선_삭제(Long id) {
    RestAssured
        .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().delete("/lines/" + id);
  }
}
