package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.common.LineRestAssured;
import nextstep.subway.common.StationRestAssured;
import nextstep.subway.domain.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql("/subway.sql")
@DisplayName("지하철역 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

  private static final String donongStation = "도농역";
  private static final String gooriStation = "구리역";
  private static final String ducksoStation = "구리역";

  private static final String firstLine = "1호선";
  private static final String secondLine = "2호선";
  private static final String thirdLine = "3호선";

  @LocalServerPort
  int port;

  private final StationRestAssured stationRestAssured = new StationRestAssured();
  private final LineRestAssured lineRestAssured = new LineRestAssured();

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
  }

  /**
   * When 지하철 노선을 생성하면
   * Then 지하철역이 생성된다
   * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
   */
  @Test
  void 지하철_노선_생성() {
    ExtractableResponse<Response> donongStationResponse = stationRestAssured.saveStation(donongStation);
    ExtractableResponse<Response> gooriStationResponse = stationRestAssured.saveStation(gooriStation);
    StationResponse donongStation = donongStationResponse.as(StationResponse.class);
    StationResponse gooriStation = gooriStationResponse.as(StationResponse.class);

    saveLine(secondLine, donongStation, gooriStation);

    ExtractableResponse<Response> response = lineRestAssured.findAllLine();

    List<String> names = response.jsonPath().getList("name", String.class);
    assertThat(names).containsAnyOf("1호선");

    List<String> colors = response.jsonPath().getList("color", String.class);
    assertThat(colors).containsAnyOf(Color.BLUE.name());
  }

  /**
   * Given 2개의 지하철 노선을 생성하고
   * When 지하철 노선 목록을 조회하면
   * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
   */
  @Test
  void 지하철_노선_목록_조회() {
    ExtractableResponse<Response> donongStationResponse = stationRestAssured.saveStation(donongStation);
    ExtractableResponse<Response> gooriStationResponse = stationRestAssured.saveStation(gooriStation);
    ExtractableResponse<Response> ducksoStationResponse = stationRestAssured.saveStation(ducksoStation);
    StationResponse donongStation = donongStationResponse.as(StationResponse.class);
    StationResponse gooriStation = gooriStationResponse.as(StationResponse.class);
    StationResponse ducksoStation = ducksoStationResponse.as(StationResponse.class);

    saveLine(firstLine, donongStation, gooriStation);
    saveLine(secondLine, donongStation, ducksoStation);

    ExtractableResponse<Response> response = lineRestAssured.findAllLine();

    List<String> names = response.jsonPath().getList("name", String.class);
    assertThat(names).containsAnyOf(firstLine, secondLine);

    List<String> colors = response.jsonPath().getList("color", String.class);
    assertThat(colors).containsAnyOf(Color.BLUE.name(), Color.GREEN.name());
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 조회하면
   * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
   */
  @Test
  void 지하철_노선_조회() {
    ExtractableResponse<Response> donongStationResponse = stationRestAssured.saveStation(donongStation);
    ExtractableResponse<Response> gooriStationResponse = stationRestAssured.saveStation(gooriStation);
    StationResponse donongStation = donongStationResponse.as(StationResponse.class);
    StationResponse gooriStation = gooriStationResponse.as(StationResponse.class);

    ExtractableResponse<Response> donongGooriLine = saveLine(firstLine, donongStation, gooriStation);

    ExtractableResponse<Response> response = lineRestAssured.findLine(donongGooriLine.jsonPath().getLong("id"));

    String names = response.jsonPath().getString("name");
    assertThat(names).isEqualTo(firstLine);

    String colors = response.jsonPath().getString("color");
    assertThat(colors).isEqualTo(Color.BLUE.name());
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 수정하면
   * Then 해당 지하철 노선 정보는 수정된다
   */
  @Test
  void 지하철_노선_수정() {
    ExtractableResponse<Response> donongStationResponse = stationRestAssured.saveStation(donongStation);
    ExtractableResponse<Response> gooriStationResponse = stationRestAssured.saveStation(gooriStation);
    StationResponse donongStation = donongStationResponse.as(StationResponse.class);
    StationResponse gooriStation = gooriStationResponse.as(StationResponse.class);

    ExtractableResponse<Response> donongGooriLine = saveLine(firstLine, donongStation, gooriStation);

    ExtractableResponse<Response> updateDonongGooriLine = lineRestAssured.updateLine(donongGooriLine.jsonPath().getLong("id"), thirdLine, Color.ORANGE);
    assertThat(updateDonongGooriLine.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 삭제하면
   * Then 해당 지하철 노선 정보는 삭제된다
   */
  @Test
  void 지하철_노선_삭제() {
    ExtractableResponse<Response> donongStationResponse = stationRestAssured.saveStation(donongStation);
    ExtractableResponse<Response> gooriStationResponse = stationRestAssured.saveStation(gooriStation);
    StationResponse donongStation = donongStationResponse.as(StationResponse.class);
    StationResponse gooriStation = gooriStationResponse.as(StationResponse.class);

    ExtractableResponse<Response> donongGooriLine = saveLine(firstLine, donongStation, gooriStation);

    ExtractableResponse<Response> deleteDonongGooriLine = lineRestAssured.deleteLine(donongGooriLine.jsonPath().getLong("id"));
    assertThat(deleteDonongGooriLine.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }

  private ExtractableResponse<Response> saveLine(String name, StationResponse upStationResponse, StationResponse downeStationResponse1) {
    ExtractableResponse<Response> donongGooriLine = lineRestAssured.saveLine(name, upStationResponse, downeStationResponse1);
    assertThat(donongGooriLine.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    return donongGooriLine;
  }
}
