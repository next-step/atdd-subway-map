package subway.fixture;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import subway.line.SectionResponse;

public class SectionFixture {

  public static SectionResponse createSection(
      final Long lineId,
      final Long upStationId,
      final Long downStationId,
      final int distance
  ) {
    Map<String, Object> params = new HashMap<>();
    params.put("upStationId", upStationId);
    params.put("downStationId", downStationId);
    params.put("distance", distance);

    return RestAssured
        .given()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines/{id}/sections", lineId)
        .then().extract().as(SectionResponse.class);
  }

}
