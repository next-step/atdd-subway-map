package subway.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    /**
     * Given 지하철 노선을 하나 생성한다.
     * When 지하철 구간을 생성하고, 새로운 구간의 상행역이 노선의 하행 종점역이다.
     * Then 해당 노선에서 구간 조회 시 구간을 찾을 수 있다.
     */
    @DisplayName("구간의 상행역을 하행종점역에 등록한다.")
    @Test
    void 구간의_상행역을_노선의_하행종점역에_등록() {

    }

    /**
     * Given 지하철 노선을 생성한다.
     * When 지하철 구간을 생성하고, 새로운 구간의 상행역이 노선의 하행 종점역이 아니다.
     * Then 노선에 등록시 오류가 난다.
     */
    @DisplayName("구간의 상행역을 노선의 상행종점역에 등록한다.")
    @Test
    void 구간_상행역을_노선의_상행종점역에_등록() {

    }

    /**
     * Given 구간이 2개인 지하철 노선을 생성한다.
     * When 상행 종점역이 포함된 구간을 제거한다.
     * Then 오류가 발생한다.
     */
    @DisplayName("노선의 상행종점역을 제거한다.")
    @Test
    void 노선의_상행종점역을_제거() {

    }

    /**
     * Given 구간이 1개인 지하철 노선을 생성한다.
     * When 하행 종점역을 제거한다.
     * Then 오류가 발생한다.
     */
    @DisplayName("구간이 한 개인 노선의 하행종점역을 제거한다.")
    @Test
    void 구간이_한개인_노선의_하행종점역을_제거() {

    }

    /**
     * Given 구간이 2개인 지하철 노선을 생성한다.
     * When 노선에 등록된 하행 종점역을 제거한다.
     * Then 오류가 발생한다.
     */
    @DisplayName("구간이 두 개인 노선의 하행종점역을 제거한다.")
    @Test
    void 노선의_하행종점역을_제거() {

    }

}
