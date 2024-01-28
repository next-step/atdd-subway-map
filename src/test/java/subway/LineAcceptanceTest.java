package subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        /*
        * TODO
        * When 지하철 노선을 생성하면
        * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
        */
    }

    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void showLines() {
        /*
         * TODO
         * Given 2개의 지하철 노선을 생성하고
         * When 지하철 노선 목록을 조회하면
         * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
         */
    }

    @DisplayName("지하철노선을 조회한다.")
    @Test
    void retrieveLine() {
        /*
         * TODO
         * Given 지하철 노선을 생성하고
         * When 생성한 지하철 노선을 조회하면
         * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
         */
    }

    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        /*
         * TODO
         * Given 지하철 노선을 생성하고
         * When 생성한 지하철 노선을 수정하면
         * Then 해당 지하철 노선 정보는 수정된다
         */
    }

    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {
        /*
         * TODO
         * Given 지하철 노선을 생성하고
         * When 생성한 지하철 노선을 삭제하면
         * Then 해당 지하철 노선 정보는 삭제된다
         */
    }

}
