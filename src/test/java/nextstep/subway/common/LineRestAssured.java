package nextstep.subway.common;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.domain.Color;
import org.springframework.http.MediaType;

public class LineRestAssured {

  private static final String blue = "blue";

  public LineRestAssured() {}

  public ExtractableResponse<Response> saveLine(String name, long upStationId, long downStationId) {
    LineRequest lineRequest = new LineRequest(name, new Color(blue), upStationId, downStationId, 10);

    return RestAssured.given().log().all()
        .body(lineRequest, ObjectMapperType.JACKSON_2)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines")
        .then().log().all()
        .extract();
  }

  public ExtractableResponse<Response> findAllLine() {
    return RestAssured.given().log().all()
        .when().get("/lines")
        .then().log().all()
        .extract();
  }

  public ExtractableResponse<Response> findLine(Long id) {
    return RestAssured.given().log().all()
        .when().get("/lines/" + id)
        .then().log().all()
        .extract();
  }

  public ExtractableResponse<Response> updateLine(Long id, String name, Color color) {
    LineUpdateRequest lineUpdateRequest = new LineUpdateRequest(name, color);

    return RestAssured.given().log().all()
        .body(lineUpdateRequest, ObjectMapperType.JACKSON_2)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().put("/lines/" + id)
        .then().log().all()
        .extract();
  }

  public ExtractableResponse<Response> deleteLine(Long id) {
    return RestAssured.given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().delete("/lines/" + id)
        .then().log().all()
        .extract();
  }
}
