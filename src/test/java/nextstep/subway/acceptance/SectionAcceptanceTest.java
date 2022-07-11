package nextstep.subway.acceptance;

import org.junit.jupiter.api.DisplayName;

@DisplayName("구간관리 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    /**
     * Given 지하철 노선을 등록하고
     * When 노선에 구간을 등록하면
     * Then 지하철 노선 조회시 등록한 구간을 확인할 수 있다
     */

    /**
     * Given 지하철 노선을 등록하고
     * When 하행역이 노선 내 포함된 구간을 노선에 등록하면
     * Then 에러가 발생한다
     */

    /**
     * Given 지하철 노선을 등록하고
     * When 상행역이 노선의 하행 종점역이 아닌 구간을 노선에 등록하면
     * Then 에러가 발생한다
     */

    /**
     * Given 지하철 노선을 등록하고
     * When 존재하지 않는 역을 포함한 구간을 노선에 등록하면
     * Then 에러가 발생한다
     */

    /**
     * Given 2개 이상의 구간이 포함된 지하철 노선을 등록하고
     * When 마지막 구간을 제거하면
     * Then 지하철 노선 조회시 마지막 구간이 제거된 노선을 확인할 수 있다
     */

    /**
     * Given 1개 구간만 포함된 지하철 노선을 등록하고
     * When 하행종점역을 제거하면
     * Then 에러가 발생한다
     */

    /**
     * Given 2개 이상의 구간이 포함된 지하철 노선을 등록하고
     * When 하행종점역이 아닌 역을 제거하면
     * Then 에러가 발생한다
     */

    /**
     * Given 지하철 노선을 등록하고
     * When 노선에 등록되지 않은 역을 제거하면
     * Then 에러가 발생한다
     */
}
