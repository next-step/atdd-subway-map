package subway;

import io.restassured.RestAssured;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.springframework.http.MediaType;

public class LineFixture {

  public static LineResponse createLine(
      final String name,
      final String color,
      final Long upStationId,
      final Long downStationId,
      final int distance
  ) {
    final var params = new HashMap<>();
    params.put("name", name);
    params.put("color", color);
    params.put("upStationId", upStationId);
    params.put("downStationId", downStationId);
    params.put("distance", distance);

    return RestAssured
        .given()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines")
        .then().extract().as(LineResponse.class);
  }

  public static List<LineResponse> getLines() {
    return Arrays.asList(
        RestAssured
            .given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then().extract().as(LineResponse[].class)
    );
  }

}
