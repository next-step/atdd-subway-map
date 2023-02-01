package subway.section;

import org.junit.jupiter.api.DisplayName;
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

  /**
   * Given 지하철 노선을 생성하고
   * When 상행선에 구간을 추가하면
   * Then 에러를 반환한다
   */

  /**
   * Given 지하철 노선을 생성하고
   * When 추가하려는 구간의 하행역이 이미 노선에 등록된 경우
   * Then 에러를 반환한다.
   */

  /**
   * Given 구간이 2개 이상인 지하철 노선을 생성하고
   * When 노선의 하행역을 제거하면
   * Then 해당 역이 지워진 노선이 반환된다.
   */

  /**
   * Given 지하철 노선을 생성하고
   * When 노선의 상행역을 제거하면
   * Then 에러를 반환한다.
   */

  /**
   * Given 구간이 1개인 지하철 노선을 생성하고
   * When 노선을 제거하면
   * 에러를 반환한다.
   */
}
