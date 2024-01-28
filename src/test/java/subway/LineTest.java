package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineTest {

  @LocalServerPort
  private int port;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  /**
   * When 노선을 생성하면
   * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
   */
  @DisplayName("노선을 생성한다.")
  @Test
  void createLineSuccess() {
    // given
    final var LINE_NAME = "신분당선";
    final var upStation = StationFixture.createStation("강남역");
    final var downStation = StationFixture.createStation("청계산입구역");

    // when
    Map<String, Object> params = new HashMap<>();
    params.put("name", LINE_NAME);
    params.put("color", "bg-red-600");
    params.put("upStationId", upStation.getId());
    params.put("downStationId", downStation.getId());
    params.put("distance", 10);

    final var response = RestAssured
        .given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines")
        .then().log().all()
        .extract();

    final var line = LineFixture.getLines().stream()
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
    assertThat(upStation.getId()).isIn(stationIds);

    // then
    assertThat(downStation.getId()).isIn(stationIds);
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
    final var 강남역 = StationFixture.createStation("강남역");
    final var 청계산입구역 = StationFixture.createStation("청계산입구역");
    final var 논현역 = StationFixture.createStation("논현역");
    final var 강남구청역 = StationFixture.createStation("강남구청역");
    final var createdLines = List.of(
        LineFixture.createLine("신분당선", "bg-red-600", 강남역.getId(), 청계산입구역.getId(), 10),
        LineFixture.createLine("7호선", "bg-green-300", 논현역.getId(), 강남구청역.getId(), 20)
    );

    // when
    final var response = RestAssured
        .given().log().all()
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
    // given
    final var upStation = StationFixture.createStation("강남역");
    final var downStation = StationFixture.createStation("청계산입구역");
    final var createdLine = LineFixture.createLine("신분당선", "bg-red-600", upStation.getId(), downStation.getId(), 10);

    // when
    final var response = RestAssured
        .given().log().all()
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
  @DisplayName("노선을 수정한다.")
  @Test
  void updateLineSuccess() {
    // given
    final var 신분당선 = LineFixture.createLine("신분당선", "bg-red-600", 1L, 2L, 10);
    final var updateParam = new LineUpdateRequest("2호선", "bg-green-800");

    // when
    Map<String, Object> params = new HashMap<>();
    params.put("name", updateParam.getName());
    params.put("color", updateParam.getColor());

    final var response = RestAssured
        .given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().put("/lines/{id}", 신분당선.getId())
        .then().log().all()
        .extract();

    final var line = LineFixture.getLines().stream()
        .filter(it -> 신분당선.getId().equals(it.getId()))
        .findFirst();

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    // then
    assertThat(line.isPresent()).isTrue();
    assertThat(line.get().getName()).isEqualTo(updateParam.getName());
    assertThat(line.get().getColor()).isEqualTo(updateParam.getColor());
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 삭제하면
   * Then 해당 지하철 노선 정보는 삭제된다
   */
  @DisplayName("지하철역을 삭제한다.")
  @Test
  void deleteLineSuccess() {
    // given
    final var deletedLine = LineFixture.createLine("신분당선", "bg-red-600", 1L, 2L, 10);

    // when
    final var response = RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().delete("/lines/{id}", deletedLine.getId())
        .then().log().all()
        .extract();

    final var remainingLineIds = LineFixture.getLines().stream()
        .map(LineResponse::getId)
        .collect(Collectors.toList());

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    // then
    assertThat(deletedLine.getId()).isNotIn(remainingLineIds);
  }

}
