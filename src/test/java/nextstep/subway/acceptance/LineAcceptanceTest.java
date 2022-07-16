package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql("/subway.sql")
@DisplayName("지하철역 노선 관련 기능")
public class LineAcceptanceTest extends BasicAcceptanceTest {

  private static final String donongStation = "도농역";
  private static final String gooriStation = "구리역";
  private static final String ducksoStation = "덕소역";

  private static final String firstLine = "1호선";
  private static final String secondLine = "2호선";
  private static final String thirdLine = "3호선";

  /**
   * When 지하철 노선을 생성하면
   * Then 지하철역이 생성된다
   * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
   */
  @Test
  void 지하철_노선_생성() {
    List<StationResponse> stationResponses = stationRestAssured.saveAllStation(Arrays.asList(donongStation, gooriStation));

    saveLine(secondLine, stationResponses.get(0), stationResponses.get(1));

    ExtractableResponse<Response> response = lineRestAssured.findAllLine();

    List<String> names = response.jsonPath().getList("name", String.class);
    assertThat(names).containsAnyOf("2호선");

    List<String> colors = response.jsonPath().getList("color", String.class);
    assertThat(colors).containsAnyOf("blue");

    List<List<LineResponse>> lineResponses = response.jsonPath().getList("stations");
    assertThat(lineResponses.get(0).size()).isEqualTo(2);
  }

  /**
   * Given 2개의 지하철 노선을 생성하고
   * When 지하철 노선 목록을 조회하면
   * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
   */
  @Test
  void 지하철_노선_목록_조회() {
    List<StationResponse> stationResponses = stationRestAssured.saveAllStation(Arrays.asList(donongStation, gooriStation, ducksoStation));

    saveLine(firstLine, stationResponses.get(0), stationResponses.get(1));
    saveLine(secondLine, stationResponses.get(0), stationResponses.get(2));

    ExtractableResponse<Response> response = lineRestAssured.findAllLine();

    List<String> names = response.jsonPath().getList("name", String.class);
    assertThat(names).containsAnyOf(firstLine, secondLine);

    List<String> colors = response.jsonPath().getList("color", String.class);
    assertThat(colors).containsAnyOf("blue", "green");

    List<List<LineResponse>> lineResponses = response.jsonPath().getList("stations");
    assertThat(lineResponses.get(0).size()).isEqualTo(2);
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 조회하면
   * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
   */
  @Test
  void 지하철_노선_조회() {
    List<StationResponse> stationResponses = stationRestAssured.saveAllStation(Arrays.asList(donongStation, gooriStation));

    ExtractableResponse<Response> donongGooriLine = saveLine(firstLine, stationResponses.get(0), stationResponses.get(1));

    ExtractableResponse<Response> response = lineRestAssured.findLine(donongGooriLine.jsonPath().getLong("id"));

    String names = response.jsonPath().getString("name");
    assertThat(names).isEqualTo(firstLine);

    String colors = response.jsonPath().getString("color");
    assertThat(colors).isEqualTo("blue");

    List<LineResponse> lineResponses = response.jsonPath().getList("stations");
    assertThat(lineResponses.size()).isEqualTo(2);
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 수정하면
   * Then 해당 지하철 노선 정보는 수정된다
   */
  @Test
  void 지하철_노선_수정() {
    List<StationResponse> stationResponses = stationRestAssured.saveAllStation(Arrays.asList(donongStation, gooriStation));

    ExtractableResponse<Response> donongGooriLine = saveLine(firstLine, stationResponses.get(0), stationResponses.get(1));

    ExtractableResponse<Response> updateDonongGooriLine = lineRestAssured.updateLine(donongGooriLine.jsonPath().getLong("id"), thirdLine, new Color("orange"));
    assertThat(updateDonongGooriLine.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 삭제하면
   * Then 해당 지하철 노선 정보는 삭제된다
   */
  @Test
  void 지하철_노선_삭제() {
    List<StationResponse> stationResponses = stationRestAssured.saveAllStation(Arrays.asList(donongStation, gooriStation));

    ExtractableResponse<Response> donongGooriLine = saveLine(firstLine, stationResponses.get(0), stationResponses.get(1));

    ExtractableResponse<Response> deleteDonongGooriLine = lineRestAssured.deleteLine(donongGooriLine.jsonPath().getLong("id"));
    assertThat(deleteDonongGooriLine.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }

  private ExtractableResponse<Response> saveLine(String name, StationResponse upStationResponse, StationResponse downeStationResponse) {
    ExtractableResponse<Response> response = lineRestAssured.saveLine(name, upStationResponse, downeStationResponse);
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    return response;
  }
}
