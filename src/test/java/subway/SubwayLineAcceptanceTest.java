package subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("지하철 노선 인수 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SubwayLineAcceptanceTest {

    private final static int PORT = 8080;

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    // TODO 지하철 노선 생성
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    // TODO 지하철 노선 목록 조회
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void findLines() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    // TODO 지하철 노선 조회
    @DisplayName("지하철 노선 단일 조회")
    @Test
    void findLine() {

    }
    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    // TODO 지하철 노선 수정
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    // TODO 지하철 노선 삭제
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {

    }
}
