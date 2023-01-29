package subway;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import subway.stationLine.StationLine;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationLineAcceptanceTest {

  /**
   * When 지하철 노선을 생성하면
   * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
   */
  @DisplayName("지하철노선 생성 테스트")
  @Test
  void 지하철_노선_생성_테스트() {
    // when
    StationLine line = 지하철_노선_생성();

    assertThat(지하철_노선_조회()).isEqualTo(line);
  }

  /**
   * Given 2개의 지하철 노선을 생성하고
   * When 지하철 노선 목록을 조회하면
   * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
   */
  @DisplayName("지하철노선 목록 조회 테스트")
  @Test
  void 지하철_노선_목록_조회_테스트() {
    StationLine line1 = 지하철_노선_생성();
    StationLine line2 = 지하철_노선_생성();

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
    StationLine created = 지하철_노선_생성();

    // when
    StationLine show = 지하철_노선_조회();

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
    StationLine created = 지하철_노선_생성();

    // when
    StationLine updated = 지하철_노선_수정();

    // then
    assertThat(지하철_노선_조회()).isEqualTo(updated);
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
    StationLine line = 지하철_노선_생성();

    // when
    지하철_노선_삭제();

    // then
    assertThat(지하철_노선_조회()).isNull();
  }

  private StationLine 지하철_노선_생성() {
    return null;
  }

  private StationLine 지하철_노선_조회() {
    return null;
  }

  private List<StationLine> 지하철_노선_목록_조회() {
    return null;
  }

  private StationLine 지하철_노선_수정() {
    return null;
  }

  private void 지하철_노선_삭제() {

  }
}
