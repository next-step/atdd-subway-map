package subway.fixture;

import io.restassured.RestAssured;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import subway.station.StationResponse;

public class StationFixture {

  public static StationResponse createStation(final String stationName) {
    return RestAssured
        .given()
        .body(Map.of("name", stationName))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/stations")
        .then().extract().as(StationResponse.class);
  }

  public static List<StationResponse> getStations() {
    return Arrays.asList(
        RestAssured
            .given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/stations")
            .then().extract().as(StationResponse[].class)
    );
  }

}
