package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.common.exception.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql("/subway.sql")
@DisplayName("지하철역 구간 관련 기능")
public class SectionAcceptanceTest extends BasicAcceptanceTest {

  private static final String donongStation = "도농역";
  private static final String gooriStation = "구리역";
  private static final String ducksoStation = "덕소역";

  private static final String firstLine = "1호선";

  private static final String blue = "blue";

  private static final int DISTANCE = 10;

  private long lineId;
  private long donongStationId;
  private long gooriStationId;
  private long ducksoStationId;

  @BeforeEach
  void setup() {
    List<StationResponse> stationResponses = stationRestAssured.saveAllStation(Arrays.asList(donongStation, gooriStation, ducksoStation));
    donongStationId = stationResponses.get(0).getId();
    gooriStationId = stationResponses.get(1).getId();
    ducksoStationId = stationResponses.get(2).getId();

    lineId = lineRestAssured.saveLine(firstLine, donongStationId, gooriStationId).jsonPath().getLong("id");
  }

  /**
   * Given 지하철 구간을 생성하고
   * When 지하철 노선 목록 조회시
   * Then 지하철 노선에 등록한 구간 정보를 확인할 수 있다.
   */
  @Test
  void 지하철_구간_생성() {
    saveSectionAssert(lineId, ducksoStationId, gooriStationId);

    ExtractableResponse<Response> findAllLineResponse = lineRestAssured.findAllLine();

    List<String> names = findAllLineResponse.jsonPath().getList("name", String.class);
    assertThat(names).contains(firstLine);

    List<String> colors = findAllLineResponse.jsonPath().getList("color", String.class);
    assertThat(colors).contains(blue);

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
    ExtractableResponse<Response> saveSectionResponse = saveSectionNoAssert(lineId, ducksoStationId, donongStationId);

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
    ExtractableResponse<Response> saveSectionResponse = saveSectionNoAssert(lineId, donongStationId, gooriStationId);

    assertThat(saveSectionResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    assertThat(saveSectionResponse.jsonPath().getString("message")).isEqualTo(ErrorMessage.LINE_CONTAINS_STATION);
  }

  /**
   * Given 지하철 구간을 생성하고
   * When 생성한 지하철 구간을 삭제하면
   * Then 해당 지하철 구간 정보는 삭제된다
   */
  @Test
  void 지하철_구간_제거() {
    saveSectionAssert(lineId, ducksoStationId, gooriStationId);

    ExtractableResponse<Response> response = sectionRestAssured.deleteSection(lineId, ducksoStationId);
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }

  /**
   * Given 지하철 구간을 생성하고
   * When 생성한 지하철 구간을 삭제할 때 마지막 구간(종점역)이 아니면
   * Then 예외 발생
   */
  @Test
  void 지하철_하행_종점역_아닌_다른_역제거_오류() {
    saveSectionAssert(lineId, ducksoStationId, gooriStationId);

    ExtractableResponse<Response> response = sectionRestAssured.deleteSection(lineId, gooriStationId);
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    assertThat(response.jsonPath().getString("message")).isEqualTo(ErrorMessage.SECTION_NO_LAST_DELETE);
  }

  private ExtractableResponse<Response> saveSectionAssert(long lineId, long downStationId, long upStationId) {
    ExtractableResponse<Response> response = sectionRestAssured.saveSection(lineId, downStationId, upStationId, DISTANCE);
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    return response;
  }

  private ExtractableResponse<Response> saveSectionNoAssert(long lineId, long downStationId, long upStationId) {
    return sectionRestAssured.saveSection(lineId, downStationId, upStationId, DISTANCE);
  }

  /**
   * Given 지하철 구간을 생성하고
   * When 생성한 지하철 구간을 삭제할 때 구간이 1개라면 삭제시
   * Then 예외 발생
   */
  @Test
  void 지하철_구간_오직_1개일_경우_삭제_오류() {
    ExtractableResponse<Response> response = sectionRestAssured.deleteSection(lineId, gooriStationId);
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    assertThat(response.jsonPath().getString("message")).isEqualTo(ErrorMessage.SECTION_ONE_NO_DELETE);
  }
}