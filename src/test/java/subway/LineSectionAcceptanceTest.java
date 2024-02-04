package subway;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import subway.common.Line;
import subway.common.Station;
import subway.controller.line.LineResponse;
import subway.controller.station.StationResponse;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DisplayName("지하철 노선 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class LineSectionAcceptanceTest {

    /**
     * When  A역-B역 지하철 노선에 B역-C역 구간을 등록하면
     * Then B역-C역 구간이 등록 된다 (201)
     * Then A역-B역 노선의 하행역이 C역으로 변경되어야 한다
     */
    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void enrollSectionToLine() {

    }

    /**
     * When A역-B역 노선에 D역-B역 구간을 등록하면
     * Then 구간등록에 실패한다 (409)
     * Then 실패 메시지는 "이미 포함된 하행역" 이어야 한다.
     */
    @DisplayName("구간의 하행역이 이미 노선에 포함되어있는 경우 등록을 실패한다.")
    @Test
    void failEnrollSectionToLineWithAlreadyExist() {

    }

    /**
     * When A역-B역 노선에 C역-D역 구간을 등록하면
     * Then 구간 등록에 실패한다 (400)
     * Then 실패 메시지는 "잘못된 상행역" 이어야 한다.
     */
    @DisplayName("새로운 구간의 상행역과 기존 노선의 하행역이 같지 않으면 등록을 실패한다.")
    @Test
    void failEnrollSectionToLineWithInvalidUpStation() {

    }

    /**
     * Given A역-B역 노선에 B역-C역 구간을 등록하고
     * When 하행역이 C역인 구간을 삭제 요청하면
     * Then B역-C역 구간이 삭제된다 (204)
     * Then A역-B역 노선의 새로운 하행역은 B역이 되어야 한다
     */
    @DisplayName("지하철 노선 구간을 삭제한다.")
    @Test
    void deleteLineSection() {

    }

    /**
     * Given A역-B역 노선에 B역-C역 구간을 등록하고
     * When 하행역이 B역인 구간을 삭제 요청하면
     * Then 구간 삭제를 실패한다 (400)
     * Then 실패 메시지는 "잘못된 하행역" 이어야 한다.
     */
    @DisplayName("삭제할 구간이 노선의 마지막 구간이 아닌 경우 삭제를 실패한다.")
    @Test
    void failDeleteLineSectionWithInvalidDownStation() {

    }

    /**
     * When A역-B역 노선에서 하행역이 B역인 구간을 삭제 요청하면
     * Then 구간 삭제를 실패한다 (400)
     * Then 실패 메시지는 "유일한 구간" 이어야 한다.
     */
    @DisplayName("삭제할 구간이 노선의 유일한 구간인 경우 삭제를 실패한다.")
    @Test
    void failDeleteLineSectionWithLastSection() {

    }
}
