package subway.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DisplayName("구간 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {
    /**
     * Given 노선을 생성한다.
     * When 상행역이 노선의 하행종점역인 구간을 생성한다.
     * Then 노선의 하행종점역이 새로운 구간의 하하행역임을 확인한다.
     */
    @DisplayName("노선에 구간이 정상적으로 등록된다.")
    @Test
    void 구간_등록() {

    }
}
