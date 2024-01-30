package subway.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    // TODO : 지하철노선 생성 인수 테스트 메서드 작성
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    // TODO : 지하철노선 목록 조회 인수 테스트 메서드 작성
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void findAllLine() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    // TODO : 지하철노선 조회 인수 테스트 메서드 작성
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void findLine() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    // TODO : 지하철노선 수정 인수 테스트 메서드 작성
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    // TODO : 지하철노선 삭제 인수 테스트 메서드 작성
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {

    }
}
