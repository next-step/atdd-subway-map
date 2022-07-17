package nextstep.subway.common;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.springframework.http.MediaType;

public class SectionRestAssured {

  public SectionRestAssured() {}

  public ExtractableResponse<Response> saveSection(long lineId, long downStationId, long upStationId, int distance) {
    SectionRequest sectionRequest = new SectionRequest(downStationId, upStationId, distance);

    return RestAssured.given().log().all()
        .body(sectionRequest, ObjectMapperType.JACKSON_2)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines/" + lineId + "/sections")
        .then().log().all()
        .extract();
  }

  public ExtractableResponse<Response> deleteSection(Long lineId, long stationId) {
    return RestAssured.given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .delete("/lines/" + lineId + "/sections?stationId=" + stationId)
        .then().log().all()
        .extract();
  }
}
