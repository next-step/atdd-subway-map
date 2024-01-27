package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineTest {

  @LocalServerPort
  private int port;

  /**
   * When 노선을 생성하면
   * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
   */
  @DisplayName("노선을 생성한다.")
  @Test
  void createLineSuccess() {
    // given
    final var LINE_NAME = "신분당선";
    final var UP_STATION_ID = 1;
    final var DOWN_STATION_ID = 2;

    // when
    Map<String, Object> params = new HashMap<>();
    params.put("name", LINE_NAME);
    params.put("color", "bg-red-600");
    params.put("upStationId", UP_STATION_ID);
    params.put("downStationId", DOWN_STATION_ID);
    params.put("distance", 10);

    final var response = RestAssured
        .given().log().all()
        .body(params)
        .port(port)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines")
        .then().log().all()
        .extract();

    final var line = getLines().stream()
        .filter(it -> LINE_NAME.equals(it.getName()))
        .findFirst();
    final var stationIds = line.map(LineResponse::getStations)
        .orElseGet(Collections::emptyList).stream()
        .map(StationResponse::getId)
            .collect(Collectors.toList());

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    // then
    assertThat(line).isNotEmpty();

    // then
    assertThat(UP_STATION_ID).isIn(stationIds);

    // then
    assertThat(DOWN_STATION_ID).isIn(stationIds);
  }

  /**
   * Given 2개의 지하철 노선을 생성하고
   * When 지하철 노선 목록을 조회하면
   * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
   */
  @DisplayName("노선 목록을 조회한다.")
  @Test
  void getLinesSuccess() {
    // given
    final var createdLines = List.of(
        createLine("신분당선", "bg-red-600", 1, 2, 10),
        createLine("7호선", "bg-green-300", 3, 4, 20)
    );

    // when
    final var response = RestAssured
        .given().log().all()
        .port(port)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/lines")
        .then().log().all()
        .extract();

    final var lineIds = response.jsonPath().getList("id", Long.class);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    // then
    createdLines.forEach(
        createdLine -> assertThat(createdLine.getId()).isIn(lineIds)
    );
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 조회하면
   * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
   */
  @DisplayName("노선을 조회한다.")
  @Test
  void getLineSuccess() {
    // when
    final var createdLine = createLine("신분당선", "bg-red-600", 1, 2, 10);

    // when
    final var response = RestAssured
        .given().log().all()
        .port(port)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/lines/{id}", createdLine.getId())
        .then().log().all()
        .extract();

    final var line = response.as(LineResponse.class);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    // then
    assertThat(createdLine.getId()).isEqualTo(line.getId());
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 수정하면
   * Then 해당 지하철 노선 정보는 수정된다
   */

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 삭제하면
   * Then 해당 지하철 노선 정보는 삭제된다
   */

  private LineResponse createLine(
      final String name,
      final String color,
      final Integer upStationId,
      final Integer downStationId,
      final Integer distance
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
        .port(port)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines")
        .then().extract().as(LineResponse.class);
  }

  private List<LineResponse> getLines() {
    return Arrays.asList(
        RestAssured
        .given()
        .port(port)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines")
        .then().extract().as(LineResponse[].class)
    );
  }

}
