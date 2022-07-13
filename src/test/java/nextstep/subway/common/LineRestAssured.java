package nextstep.subway.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Color;
import nextstep.subway.domain.Station;
import org.springframework.http.MediaType;

public class LineRestAssured {

  public ExtractableResponse<Response> saveLine(String name, StationResponse upStation, StationResponse downStation) {
    Map<String, Object> params = new HashMap<>();
    params.put("name", "1호선");
    params.put("color", Color.BLUE);
    params.put("upStationId", upStation.getId());
    params.put("downStationId", downStation.getId());
    params.put("distance", 10);

    return RestAssured.given().log().all()
        .body(params)
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
    Map<String, Object> params = new HashMap<>();
    params.put("name", name);
    params.put("color", color);

    return RestAssured.given().log().all()
        .body(params)
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
