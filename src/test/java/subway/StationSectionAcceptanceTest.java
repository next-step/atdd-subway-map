package subway;


import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DisplayName("지하철 역 노선 구간 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationSectionAcceptanceTest {


    /**
     * Given 지하철 역 A, B, C, D를 생성 후
     * Given A, B역을 잇는 노선을 생성 후
     * When B, C역을 잇는 구간을 생성하면
     * Then 노선의 상행 종점역은 A가 되고, 하행 종점역은 C가 된다.
     */


    /**
     * Given 지하철 역 A, B, C, D를 생성 후
     * Given A, B, C역을 잇는 노선을 생성 후
     * When B, D역을 잇는 구간을 생성하면
     * Then "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다." 오류 메시지가 나온다.
     */


    /**
     * Given 지하철 역 A, B, C, D를 생성 후
     * Given A, B, C역을 잇는 노선을 생성 후
     * When D, A역을 잇는 구간을 생성하면
     * Then "새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다." 오류 메시지가 나온다.
     */


    /**
     * Given 지하철 역 A, B, C, D를 생성 후
     * Given A, B, C역을 잇는 노선을 생성 후
     * When C역을 삭제하면
     * Then 노선을 하행 종점역은 B가 된다.
     */

    /**
     * Given 지하철 역 A, B, C, D를 생성 후
     * Given A, B, C역을 잇는 노선을 생성 후
     * When A역을 삭제하면
     * When B역을 삭제하면
     * Then "지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다." 오류 메시지가 나온다.
     */

    /**
     * Given 지하철 역 A, B, C, D를 생성 후
     * Given A, B역을 잇는 노선을 생성 후
     * When B역을 삭제하면
     * Then "지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다." 오류 메시지가 나온다.
     */



}
