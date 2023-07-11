package subway.section;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SectionAcceptanceTest {
    /**
     * When 지하철 구간을 추가하면
     * Then 지하철 노선 조회시, 추가된 구간을 확인할 수 있다
     * TODO: 구현 필요
     */

    /**
     * When 하행역이 이미 지하철 노선에 등록된 구간을 등록하면
     * Then 에러가 발생한다
     * TODO: 구현 필요
     */

    /**
     * When 상행역이 노선의 하행 종점역이 아닌 구간을 등록하면
     * Then 에러가 발생한다
     * TODO: 구현 필요
     */

    /**
     * Given 새로운 지하철 구간을 추가하고
     * When 해당 노선의 구간을 제거하면
     * Then 마지막에 추가된 구간이 제거된다.
     * TODO: 구현 필요
     */

    /**
     * When 구간이 1개 뿐인 노선의 구간을 제거하면
     * Then 에러가 발생한다
     * TODO: 구현 필요
     */
}
