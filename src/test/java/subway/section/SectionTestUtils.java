package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.Line;

public class SectionTestUtils {

  private SectionTestUtils() {}

  public static ExtractableResponse<Response> 노선에_구간_추가(Line line, SectionCreateRequest request) {
    return RestAssured
        .given().body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines/" + line.getId() + "/sections")
        .then().extract();
  }

  public static ExtractableResponse<Response> 노선에_구간_제거(Line line, Long sectionId) {
    return RestAssured
        .given().contentType(MediaType.APPLICATION_JSON_VALUE).queryParam("stationId", sectionId)
        .when().delete("/lines/" + line.getId() + "/sections")
        .then().extract();
  }

}
