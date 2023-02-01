package subway.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DisplayName("구간 관리 기능 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SectionAcceptanceTest {

  /**
   * Given 지하철 노선을 생성하고
   * When 구간을 추가하면
   * Then 노선에 구간이 추가된다.
   */
  @Test
  @DisplayName("지하철 구간 생성 테스트")
  void 노선_구간_생성_테스트() {

  }

  /**
   * Given 지하철 노선을 생성하고
   * When 하행선이 아닌 역에 구간을 추가하면
   * Then 에러를 반환한다
   */
  @Test
  @DisplayName("하행선이 아닌 역에 구간 추가시 에러를 반환한다.")
  void 하행선이_아닌_구간_생성_테스트() {

  }

  /**
   * Given 지하철 노선을 생성하고
   * When 추가하려는 구간의 하행역이 이미 노선에 등록된 경우
   * Then 에러를 반환한다.
   */
  @Test
  @DisplayName("이미 등록된 역을 새로운 하행역으로 설정하는 구간은 추가할 수 없다.")
  void 노선에_이미_등록된_역의_구간_생성_테스트() {

  }

  /**
   * Given 구간이 2개 이상인 지하철 노선을 생성하고
   * When 노선의 하행역 구간을 제거하면
   * Then 해당 구간이 지워진 노선이 반환된다.
   */
  @Test
  @DisplayName("노선의 하행역을 포함한 구간을 제거하는 테스트")
  void 노선_하행역_구간_제거_테스트() {

  }

  /**
   * Given 지하철 노선을 생성하고
   * When 노선의 하행역이 아닌 구간을 제거하면
   * Then 에러를 반환한다.
   */
  @Test
  @DisplayName("하행역이 포함된 구간이 아닌 경우 지울 수 없다.")
  void 하행역이_포함안된_구간_삭제_실패_테스트() {

  }

  /**
   * Given 구간이 1개인 지하철 노선을 생성하고
   * When 노선을 제거하면
   * 에러를 반환한다.
   */
  @Test
  @DisplayName("구간이 한 개인 역은 지울 수 없다")
  void 구간이_한개면_삭제_실패_테스트() {

  }
}
