package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.request.StationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.request.LineRequest.*;
import static nextstep.subway.acceptance.request.LineRequest.lineCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;

class LineSectionAcceptanceTest extends AcceptanceTest {

  private static final String LINE_NAME_A = "신분당선";
  private static final String LINE_COLOR_A = "bg-red-600";
  private static final int FIRST_DISTANCE = 2;

  @DisplayName("노선에 구간 추가")
  @Test
  void addNewStationSectionTest() {
    // given
    ExtractableResponse<Response> createFirstStationResponse =
      StationRequest.stationCreateRequest("강남역");
    ExtractableResponse<Response> createSecondStationResponse =
      StationRequest.stationCreateRequest("역삼역");
    ExtractableResponse<Response> createThirdStationResponse =
      StationRequest.stationCreateRequest("선릉역");

    Long upStationId = createFirstStationResponse.jsonPath().getLong("id");
    Long downStationId = createSecondStationResponse.jsonPath().getLong("id");
    Long newDownStationId = createThirdStationResponse.jsonPath().getLong("id");

    ExtractableResponse<Response> response =
      lineCreateRequest(
        LINE_NAME_A, LINE_COLOR_A, upStationId, downStationId, FIRST_DISTANCE);

    String uri = response.header(LOCATION);

    Map<String, Object> createRequest = new HashMap<>();
    createRequest.put(UP_STATION_ID, downStationId);
    createRequest.put(DOWN_STATION_ID, newDownStationId);
    createRequest.put(DISTANCE, 3);

    // when
    ExtractableResponse<Response> newSectionResponse = RestAssured.given()
      .log()
      .all()
      .body(createRequest)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .when()
      .post(uri + "/sections")
      .then()
      .log()
      .all()
      .extract();

    // then
    assertThat(newSectionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }

  @DisplayName("노선 구간 제거")
  @Test
  void deleteStationSectionTest() {
    // given
    ExtractableResponse<Response> createFirstStationResponse =
      StationRequest.stationCreateRequest("강남역");
    ExtractableResponse<Response> createSecondStationResponse =
      StationRequest.stationCreateRequest("역삼역");
    ExtractableResponse<Response> createThirdStationResponse =
      StationRequest.stationCreateRequest("선릉역");

    Long upStationId = createFirstStationResponse.jsonPath().getLong("id");
    Long downStationId = createSecondStationResponse.jsonPath().getLong("id");
    Long newDownStationId = createThirdStationResponse.jsonPath().getLong("id");

    ExtractableResponse<Response> response =
      lineCreateRequest(
        LINE_NAME_A, LINE_COLOR_A, upStationId, downStationId, FIRST_DISTANCE);


    Map<String, Object> createRequest = new HashMap<>();
    createRequest.put(UP_STATION_ID, downStationId);
    createRequest.put(DOWN_STATION_ID, newDownStationId);
    createRequest.put(DISTANCE, 3);

    String uri = response.header(LOCATION);

    ExtractableResponse<Response> newSectionResponse = RestAssured.given()
      .log()
      .all()
      .body(createRequest)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .when()
      .post(uri + "/sections")
      .then()
      .log()
      .all()
      .extract();

    // when
    ExtractableResponse<Response> deleteSectionResponse = RestAssured.given()
      .log()
      .all()
      .body(createRequest)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .when()
      .delete(uri + "/sections?stationId=" + newDownStationId)
      .then()
      .log()
      .all()
      .extract();

    // then
    assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }

}
