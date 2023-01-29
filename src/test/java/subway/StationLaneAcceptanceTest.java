package subway;


import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import subway.Mocks.*;
import subway.stationLine.StationLane;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationLaneAcceptanceTest {

  /**
   * When 지하철 노선을 생성하면
   * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
   */
  @DisplayName("지하철노선 생성 테스트")
  @Test
  void 지하철_노선_생성_테스트() {
    // when
    StationLane line = 지하철_노선_생성(Lane.서울2호선, Station.서울대입구역, Station.봉천역);

    assertThat(지하철_노선_조회(line.getName())).isEqualTo(line);
  }

  /**
   * Given 2개의 지하철 노선을 생성하고
   * When 지하철 노선 목록을 조회하면
   * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
   */
  @DisplayName("지하철노선 목록 조회 테스트")
  @Test
  void 지하철_노선_목록_조회_테스트() {
    StationLane line1 = 지하철_노선_생성(Lane.서울2호선, Station.서울대입구역, Station.봉천역);
    StationLane line2 = 지하철_노선_생성(Lane.신분당선, Station.강남역, Station.신사역);

    assertThat(지하철_노선_목록_조회()).containsAll(List.of(line1,line2));
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 조회하면
   * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
   */
  @DisplayName("지하철노선 조회 테스트")
  @Test
  void 지하철_노선_조회_테스트() {
    // given
    StationLane created = 지하철_노선_생성(Lane.서울2호선, Station.서울대입구역, Station.봉천역);

    // when
    StationLane show = 지하철_노선_조회(created.getName());

    // then
    assertThat(show.getId()).isEqualTo(created.getId());
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 수정하면
   * Then 해당 지하철 노선 정보는 수정된다
   */
  @DisplayName("지하철노선 수정 테스트")
  @Test
  void 지하철_노선_수정_테스트() {
    // given
    StationLane created = 지하철_노선_생성(Lane.서울2호선, Station.서울대입구역, Station.봉천역);

    // when
    StationLane updated = 지하철_노선_수정(created.getName());

    // then
    assertThat(지하철_노선_조회(updated.getName())).isEqualTo(updated);
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 삭제하면
   * Then 해당 지하철 노선 정보는 삭제된다.
   */
  @DisplayName("지하철노선 삭제 테스트")
  @Test
  void 지하철_노선_삭제_테스트() {
    // given
    StationLane line = 지하철_노선_생성(Lane.서울2호선, Station.서울대입구역, Station.봉천역);

    // when
    지하철_노선_삭제(line.getName());

    // then
    assertThat(지하철_노선_조회(line.getName())).isNull();
  }

  private StationLane 지하철_노선_생성(String name, String inbound, String outbound) {
    Map<String, String> map = new HashMap<>();
    map.put("name", name);
    map.put("inbound", inbound);
    map.put("outbound", outbound);

    return RestAssured
        .given().body(map).contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/stationline")
        .then()
        .extract().body().as(StationLane.class);
  }

  private StationLane 지하철_노선_조회(String name) {
    return null;
  }

  private List<StationLane> 지하철_노선_목록_조회() {
    return null;
  }

  private StationLane 지하철_노선_수정(String name) {
    return null;
  }

  private void 지하철_노선_삭제(String name) {

  }
}
