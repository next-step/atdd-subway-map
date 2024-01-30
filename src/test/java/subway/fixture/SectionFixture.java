package subway.fixture;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class SectionFixture {

  public static void createSection(final Long upStationId, final Long downStationId, final int distance) {
    Map<String, Object> params = new HashMap<>();
    params.put("upStationId", upStationId);
    params.put("downStationId", downStationId);
    params.put("distance", distance);

    RestAssured
        .given()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines/{id}/sections")
        .then();
  }

}
