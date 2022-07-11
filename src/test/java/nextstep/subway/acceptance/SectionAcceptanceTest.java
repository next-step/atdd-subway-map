package nextstep.subway.acceptance;

import nextstep.subway.testsupport.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("구간관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    /**
     * Given A,B 라는 이름을 가진 2개의 역을 생성한다.
     * Given 노선을 생성하고 A역은 상행역, B역은 하행역으로 등록한다.
     * Given 새로운 구간을 생성하는데, 상행역은 B이며 하행역은 A이다.
     * When 노선에 새로운 구간을 등록한다.
     * Then 노선에 새로운 구간을 등록할 수 없다.
     */

    /**
     * Given A,B,C,D 라는 이름을 가진 4개의 역을 생성한다.
     * Given 노선을 생성하고 A역은 상행역, B역은 하행역으로 등록한다.
     * Given 새로운 구간을 생성하는데, 상행역은 C이며 하행역은 D이다.
     * When 노선에 새로운 구간을 등록한다.
     * Then 노선에 새로운 구간을 등록할 수 없다.
     */

    /**
     * Given A,B,C 라는 이름을 가진 3개의 역을 생성한다.
     * Given 노선을 생성하고 A역은 상행역, B역은 하행역으로 등록한다.
     * Given 새로운 구간을 생성하는데, 상행역은 B이며 하행역은 C이다.
     * When 노선에 새로운 구간을 등록한다.
     * Then 노선을 조회하고 상행역, 하행역을 확인한다.
     * Then 구간을 조회하고 상행역, 하행역을 확인한다.
     */
}
