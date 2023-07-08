package subway.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.AcceptanceTest;

@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    /**
     * Given 지하철 노선과 노선에 속하지 않은 새로운 지하철역을 생성하고
     * When 지하철 노선의 하행역과 새로운 지하철역을 구간으로 등록하면
     * Then 새로운 지하철 구간이 생성되고, 지하철 노선의 하행역이 새로운 지하철역으로 바뀌고, 지하철 노선의 거리가 바뀐다
     */
    @Test
    void 지하철_구간_생성() {

    }
}
