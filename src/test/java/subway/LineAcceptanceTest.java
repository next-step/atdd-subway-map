package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.fixture.LineFixture;
import subway.fixture.SectionFixture;
import subway.fixture.StationFixture;
import subway.line.LineResponse;
import subway.line.LineUpdateRequest;
import subway.station.StationResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

  @Nested
  @DisplayName("지하철 노선 기본 기능")
  class LineTest {

    /**
     * When 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("노선을 생성한다.")
    @Test
    void createLineSuccess() {
      // given
      final var lineName = "신분당선";
      final var upStation = StationFixture.createStation("강남역");
      final var downStation = StationFixture.createStation("청계산입구역");

      // when
      Map<String, Object> params = new HashMap<>();
      params.put("name", lineName);
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
          .filter(it -> lineName.equals(it.getName()))
          .findFirst();

      // then
      assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

      // then
      assertThat(line).isNotEmpty();

      // then
      final var stationIds = line.map(LineResponse::getStations)
          .orElseGet(Collections::emptyList).stream()
          .map(StationResponse::getId)
          .collect(Collectors.toList());

      assertThat(upStation.getId()).isIn(stationIds);
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
      final var upStation = StationFixture.createStation("강남역");
      final var downStation = StationFixture.createStation("청계산입구역");
      final var createdLine = LineFixture.createLine("신분당선", "bg-red-600", upStation.getId(), downStation.getId(), 10);
      final var updateParam = new LineUpdateRequest("2호선", "bg-green-800");

      // when
      Map<String, Object> params = new HashMap<>();
      params.put("name", updateParam.getName());
      params.put("color", updateParam.getColor());

      final var response = RestAssured
          .given().log().all()
          .body(params)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when().put("/lines/{id}", createdLine.getId())
          .then().log().all()
          .extract();

      final var line = LineFixture.getLines().stream()
          .filter(it -> createdLine.getId().equals(it.getId()))
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
      final var upStation = StationFixture.createStation("강남역");
      final var downStation = StationFixture.createStation("청계산입구역");
      final var deletedLine = LineFixture.createLine("신분당선", "bg-red-600", upStation.getId(), downStation.getId(), 10);

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

  @Nested
  @DisplayName("지하철 구간 추가, 제거 기능")
  class SectionTest {

    /**
     * When 지하철 노선에 구간을 추가하면
     * Then 하행 종점역을 상행역으로 하는 구간이 추가된다.
     */
    @DisplayName("노선에 구간을 추가하면 노선 조회 시 연장된 구간이 함께 조회된다.")
    @Test
    void createSectionSuccess() {
      // given
      final var upStation = StationFixture.createStation("강남역");
      final var downStation = StationFixture.createStation("청계산입구역");
      final var line = LineFixture.createLine("신분당선", "bg-red-600", upStation.getId(), downStation.getId(), 10);
      final var newStation = StationFixture.createStation("논현역");

      // when
      Map<String, Object> params = new HashMap<>();
      params.put("upStationId", downStation.getId());
      params.put("downStationId", newStation.getId());
      params.put("distance", 10);

      final var response = RestAssured
          .given().log().all()
          .body(params)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when().post("/lines/{id}/sections", line.getId())
          .then().log().all()
          .extract();

      // then
      assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

      // then
      final var stationIds = LineFixture.getLine(line.getId())
          .getStations()
          .stream()
          .map(StationResponse::getId)
          .collect(Collectors.toList());

      assertThat(stationIds).containsAll(
          List.of(
              upStation.getId(),
              downStation.getId(),
              newStation.getId()
          )
      );
    }

    /**
     * When 구간 생성 시 상행역이 노선의 하행 종점역이 아니라면
     * Then 에러 응답을 반환한다.
     */
    @DisplayName("새 구간의 상행역은 노선의 하행 종점역이어야만 한다.")
    @Test
    void createSectionFailUpStationMustBeLastStation() {
      // given
      final var upStation = StationFixture.createStation("강남역");
      final var downStation = StationFixture.createStation("청계산입구역");
      final var line = LineFixture.createLine("신분당선", "bg-red-600", upStation.getId(), downStation.getId(), 10);
      final var newStation = StationFixture.createStation("논현역");

      // when
      Map<String, Object> params = new HashMap<>();
      params.put("upStationId", upStation.getId());
      params.put("downStationId", newStation.getId());
      params.put("distance", 10);

      final var response = RestAssured
          .given().log().all()
          .body(params)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when().post("/lines/{id}/sections", line.getId())
          .then().log().all()
          .extract();

      // then
      assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

      // then
      // TODO 에러 응답 내용 추가
    }

    /**
     * When 구간 생성 시 하행역이 노선의 다른 구간에 포함되어 있다면
     * Then 에러 응답을 반환한다.
     */
    @DisplayName("현재 노선의 구간에 이미 포함되어 있는 역은 새 구간의 하행역이 될 수 없다.")
    @Test
    void createSectionFailDownStationMustNotBeIncluded() {
      // given
      final var 청계산입구역 = StationFixture.createStation("청계산입구역");
      final var 강남역 = StationFixture.createStation("강남역");
      final var 논현역 = StationFixture.createStation("논현역");
      final var line = LineFixture.createLine("신분당선", "bg-red-600", 청계산입구역.getId(), 강남역.getId(), 10);
      SectionFixture.createSection(강남역.getId(), 논현역.getId(), 10);


      // when
      Map<String, Object> params = new HashMap<>();
      params.put("upStationId", 논현역.getId());
      params.put("downStationId", 강남역.getId());
      params.put("distance", 10);

      final var response = RestAssured
          .given().log().all()
          .body(params)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when().post("/lines/{id}/sections", line.getId())
          .then().log().all()
          .extract();

      // then
      assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

      // then
      // TODO 에러 응답 내용 추가
    }

    /**
     * When 지하철 노선의 구간을 제거하면
     * Then 노선 조회 시 삭제된 구간이 조회되지 않는다.
     */
    @DisplayName("구간을 삭제하면 삭제된 구간이 노선 조회 시 조회되지 않는다.")
    @Test
    void deleteSectionSuccess() {
      // given
      final var 청계산입구역 = StationFixture.createStation("청계산입구역");
      final var 강남역 = StationFixture.createStation("강남역");
      final var 논현역 = StationFixture.createStation("논현역");
      final var line = LineFixture.createLine("신분당선", "bg-red-600", 청계산입구역.getId(), 강남역.getId(), 10);
      SectionFixture.createSection(강남역.getId(), 논현역.getId(), 10);

      // when
      final var response = RestAssured
          .given().log().all()
          .queryParam("stationId", 논현역.getId())
          // TODO default accept header check
//          .accept(ContentType.ANY)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when().delete("/lines/{id}/sections", line.getId())
          .then().log().all()
          .extract();

      // then
      assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

      // then
      final var stationIds = LineFixture.getLine(line.getId()).getStations().stream()
          .map(StationResponse::getId)
          .collect(Collectors.toList());

      assertThat(논현역.getId()).isNotIn(stationIds);
    }

    /**
     * When 삭제할 구간이 하행 종점역이 아니라면
     * Then 에러를 응답한다.
     */
    @DisplayName("삭제할 구간이 하행 종점역이 아니라면 삭제할 수 없다.")
    @Test
    void deleteSectionFailStationMustBeLastStation() {
      // given
      final var 청계산입구역 = StationFixture.createStation("청계산입구역");
      final var 강남역 = StationFixture.createStation("강남역");
      final var 논현역 = StationFixture.createStation("논현역");
      final var line = LineFixture.createLine("신분당선", "bg-red-600", 청계산입구역.getId(), 강남역.getId(), 10);
      SectionFixture.createSection(강남역.getId(), 논현역.getId(), 10);

      // when
      final var response = RestAssured
          .given().log().all()
          .queryParam("stationId", 논현역.getId())
          // TODO default accept header check
//          .accept(ContentType.ANY)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when().delete("/lines/{id}/sections", line.getId())
          .then().log().all()
          .extract();

      // then
      assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

      // then
      // TODO 에러 응답 내용 추가
    }

    /**
     * When 노선에 구간이 하나 밖에 없다면
     * Then 에러를 응답한다.
     */
    @DisplayName("노선에 구간이 하나 밖에 없다면 삭제할 수 없다.")
    @Test
    void deleteSectionFailCanNotDeleteLastSection() {
      // given
      final var 청계산입구역 = StationFixture.createStation("청계산입구역");
      final var 강남역 = StationFixture.createStation("강남역");
      final var line = LineFixture.createLine("신분당선", "bg-red-600", 청계산입구역.getId(), 강남역.getId(), 10);

      // when
      final var response = RestAssured
          .given().log().all()
          .queryParam("stationId", 강남역.getId())
          // TODO default accept header check
//          .accept(ContentType.ANY)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when().delete("/lines/{id}/sections", line.getId())
          .then().log().all()
          .extract();

      // then
      assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

      // then
      // TODO 에러 응답 내용 추가
    }

  }

}
