package subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@DisplayName("지하철 구간 테스트")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SubwaySectionAcceptanceTest {
    /**
     * When 지하철 구간을 추가하면
     * Then 지하철 노선 조회시 추가된 구간을 확인할 수 있다.
     */
    @Test
    void 지하철구간_추가() {

    }

    /**
     * When 지하철 구간이 하행선이 아닌 곳에서 추가하면
     * Then 400 에러가 리턴된다.
     */
    @Test
    void 지하철구간_추가_하행선아닌_경우() {

    }

    /**
     * Given 3개의 역이 포함된 지하철 역이 주어지고
     * When 하행 종점역을 삭제하면
     * Then 정상 삭제가 된다.
     */
    @Test
    void 지하철구간_종점_제거() {

    }

    /**
     * Given 2개의 역이 포함된 지하철 역이 주어지고
     * When 하행 종점역을 삭제하면
     * Then 에러가 발생한다.
     */
    @Test
    void 지하철구간_종점제거_2개역_존재_경우() {

    }

    /**
     * Given 3개의 역이 포함된 지하철 역이 주어지고
     * When 하행 종점역이 아닌 역을 삭제하면
     * Then 에러가 발생한다.
     */
    @Test
    void 지하철구간_종점제거_하행종점역이_아닌_경우() {

    }
}
