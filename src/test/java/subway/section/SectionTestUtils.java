package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class SectionTestUtils {

  private SectionTestUtils() {}

  public static ExtractableResponse<Response> 노선에_구간_추가(Long lineId, SectionCreateRequest request) {
    return RestAssured
        .given().body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines/" + lineId + "/sections")
        .then().extract();
  }

  public static ExtractableResponse<Response> 노선에_구간_제거(Long lineId, Long sectionId) {
    return RestAssured
        .given().contentType(MediaType.APPLICATION_JSON_VALUE).queryParam("stationId", sectionId)
        .when().delete("/lines/" + lineId + "/sections")
        .then().extract();
  }

}
