package subway.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("지하철 노선 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    /**
     * Given : 3개의 역, 1개의 노선을 등록
     * When  : 구간을 추가 하면
     * Then  : 추가한 구간이 조회 된다
     */
    @Test
    void 지하철_노선_구간_등록_성공() {
    }

    /**
     * Given : 3개의 역, 1개의 노선을 등록
     * When  : 새로운 구간의 상행역이 등록된 노선의 하행 종점이 아닌 구간을 추가 하면
     * Then  : 구간 등록에 실패 한다
     */
    @Test
    void 지하철_노선_구간_등록_실패_새로운_구간의_상행이_등록된_노선의_하행종점과_다르면_등록_불가() {
    }

    /**
     * Given : 3개의 역, 1개의 노선을 등록
     * When  : 새로운 구간의 하행역이 등록된 노선의 역인 구간을 추가 하면
     * Then  : 구간 등록에 실패 한다
     */
    @Test
    void 지하철_노선_구간_등록_실패_새로운_구간의_하행역이_등록된_노선의_역_등록_불가() {

    }

    /**
     * Given : 3개의 역, 1개의 노선, 1개의 구간을 등록
     * When  : 구간을 제거 하면
     * Then  : 구간이 제거 된다
     */
    @Test
    void 지하철_노선_구간_제거_성공() {
    }

    /**
     * Given : 3개의 역, 1개의 노선, 1개의 구간을 등록
     * When  : 마지막 이 아닌 구간을 제거 하면
     * Then  : 구간 제거에 실패 한다
     */
    @Test
    void 지하철_노선_구간_실패_마지막이_아닌_구간_제거_불가() {
    }

    /**
     * Given : 3개의 역, 1개의 노선, 1개의 구간을 등록
     * When  : 구간이 하나인 구간을 제거 하면
     * Then  : 구간 제거에 실패 한다
     */
    @Test
    void 지하철_노선_구간_실패_구간이_하나인_구간_제거_불가() {
    }
}
