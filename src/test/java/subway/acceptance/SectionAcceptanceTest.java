package subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import subway.internal.BaseTestSetup;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends BaseTestSetup {
    /**
     * Given: 특정 지하철 노선이 등록되어 있고
     * When: 구간의 상행역이 노선의 하행종창역이 아니도록 구간을 추가하면
     * Then: 상행역이 잘못되었다는 오류가 발생한다.
     */

    /**
     * Given: 특정 지하철 노선이 등록되어 있고
     * When: 구간의 하행역이 노선에 이미 포함된 역이라면
     * Then: 하행역이 잘못되었다는 오류가 발생한다.
     */

    /**
     * Given: 특정 지하철 노선이 등록되어 있고
     * When: 구간의 상행역이 노선의 하행종창역이 되도록 구간을 추가하면
     * Then: 해당 지하철 구간이 추가된다.
     */
}
