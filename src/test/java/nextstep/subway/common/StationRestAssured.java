package nextstep.subway.common;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.springframework.http.MediaType;

public class StationRestAssured {

  public StationRestAssured() {}

  public List<StationResponse> saveAllStation(List<String> names) {
    List<StationResponse> stationResponses = new ArrayList<>();
    for (String name : names) {
      stationResponses.add(saveStation(name).as(StationResponse.class));
    }

    return stationResponses;
  }

  public ExtractableResponse<Response> saveStation(String name) {
    StationRequest stationRequest = new StationRequest(name);

    return RestAssured.given().log().all()
        .body(stationRequest, ObjectMapperType.JACKSON_2)
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
