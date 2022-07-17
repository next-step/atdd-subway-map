package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.common.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@Sql("/subway.sql")
@DisplayName("지하철역 구간 관련 기능")
public class SectionAcceptanceTest extends BasicAcceptanceTest {

  private static final String donongStation = "도농역";
  private static final String gooriStation = "구리역";
  private static final String ducksoStation = "덕소역";

  private static final String firstLine = "1호선";
  private static final String secondLine = "2호선";
  private static final String thirdLine = "3호선";

  private static final String blue = "blue";
  private static final String green = "green";

  /**
   * Given 지하철 구간을 생성하면
   * When 지하철 구간이 생성된다.
   * Then 지하철 노선 목록 조회 시 수정된 노선을 찾을 수 있다
   */
  @Test
  void 지하철_구간_생성() {
    List<StationResponse> stationResponses = stationRestAssured.saveAllStation(Arrays.asList(donongStation, gooriStation, ducksoStation));

    long lineId = lineRestAssured.saveLine(firstLine, stationResponses.get(0), stationResponses.get(1)).jsonPath().getLong("id");

    Map<String, Object> param = new HashMap<>();
    param.put("downStationId", stationResponses.get(2).getId());
    param.put("upStationId", stationResponses.get(1).getId());
    param.put("distance", 10);

    ExtractableResponse<Response> saveSectionResponse = RestAssured.given().log().all()
        .body(param)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines/" + lineId + "/sections")
        .then().log().all()
        .extract();
    assertThat(saveSectionResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    ExtractableResponse<Response> findAllLineResponse = lineRestAssured.findAllLine();

    List<String> names = findAllLineResponse.jsonPath().getList("name", String.class);
    assertThat(names).containsAnyOf(firstLine);

    List<String> colors = findAllLineResponse.jsonPath().getList("color", String.class);
    assertThat(colors).containsAnyOf(blue);

    List<List<LineResponse>> lineResponses = findAllLineResponse.jsonPath().getList("stations");
    assertThat(lineResponses.get(0).size()).isEqualTo(3);
  }

  /**
   * Given 지하철 구간을 생성하면
   * When 새로운 구간의 상행역이 기존 하행 종점역이 아닐 경우
   * Then 예외 발생
   */
  @Test
  void 새로운_구간_상행역_기존_하행_종점역_아니면_오류() {
    List<StationResponse> stationResponses = stationRestAssured.saveAllStation(Arrays.asList(donongStation, gooriStation, ducksoStation));

    long lineId = lineRestAssured.saveLine(firstLine, stationResponses.get(0), stationResponses.get(1)).jsonPath().getLong("id");

    Map<String, Object> param = new HashMap<>();
    param.put("downStationId", stationResponses.get(2).getId());
    param.put("upStationId", stationResponses.get(0).getId());
    param.put("distance", 10);

    ExtractableResponse<Response> saveSectionResponse = RestAssured.given().log().all()
        .body(param)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines/" + lineId + "/sections")
        .then().log().all()
        .extract();

    assertThat(saveSectionResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    assertThat(saveSectionResponse.jsonPath().getString("message")).isEqualTo(ErrorMessage.INVALID_STATION);
  }

  /**
   * Given 지하철 구간을 생성하면
   * When 새로운 구간의 하행역이 기존 노선에 존재하면
   * Then 예외 발생
   */
  @Test
  void 새로운_구간_하행역_기존_노선_존재하면_오류() {
    List<StationResponse> stationResponses = stationRestAssured.saveAllStation(Arrays.asList(donongStation, gooriStation, ducksoStation));

    long lineId = lineRestAssured.saveLine(firstLine, stationResponses.get(0), stationResponses.get(1)).jsonPath().getLong("id");
    Map<String, Object> param = new HashMap<>();
    param.put("downStationId", stationResponses.get(0).getId());
    param.put("upStationId", stationResponses.get(1).getId());
    param.put("distance", 10);

    ExtractableResponse<Response> saveSectionResponse = RestAssured.given().log().all()
        .body(param)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines/" + lineId + "/sections")
        .then().log().all()
        .extract();

    assertThat(saveSectionResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    assertThat(saveSectionResponse.jsonPath().getString("message")).isEqualTo(ErrorMessage.LINE_CONTAINS_STATION);
  }
}