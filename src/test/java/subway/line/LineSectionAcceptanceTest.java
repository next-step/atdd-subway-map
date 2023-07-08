package subway.line;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import subway.constants.Endpoint;
import subway.support.AcceptanceTest;
import subway.support.DatabaseCleanUp;

import static io.restassured.RestAssured.UNDEFINED_PORT;

@DisplayName("지하철 노선의 구간 관련 기능")
@AcceptanceTest
public class LineSectionAcceptanceTest {

    @LocalServerPort
    private int port;

    private static final String LINE_BASE_URL = Endpoint.LINE_BASE_URL.getUrl();

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
        }
        databaseCleanUp.execute();
    }

    /**
     * <pre>
     * When 지하철 노선의 구간을 생성하면
     * Then 지하철 노선 상세 조회 시 등록한 구간의 하행 종점역을 찾을 수 있다
     * </pre>
     */
    @DisplayName("지하철 노선의 구간을 생성한다.")
    @Test
    void createLineSection() {

    }

    /**
     * <pre>
     * Given 지하철 노선을 생성하고
     * When 지하철 노선에 포함되어 있는 지하철역을 새로운 구간의 하행 종점역으로 생성하면
     * Then 구간 생성에 실패한다.
     * </pre>
     */
    @DisplayName("이미 등록되어 있는 역이 하행 종점역인 구간을 생성한다.")
    @Test
    void createConflictDownStation() {

    }

    /**
     * <pre>
     * Given 지하철 노선을 생성하고
     * When 존재하지 않는 지하철역을 새로운 구간의 하행 종점역으로 생성하면
     * Then 구간 생성에 실패한다.
     * </pre>
     */
    @DisplayName("존재하지 않는 역이 하행 종점역인 구간을 생성한다.")
    @Test
    void createNotExistDownStation() {

    }

    /**
     * <pre>
     * Given 지하철 노선의 구간을 생성하고
     * When 생성한 구간을 삭제하면
     * Then 지하철 노선 상세 조회 시 생성한 구간의 하행 종점역을 찾을 수 없다
     * </pre>
     */
    @DisplayName("지하철 노선의 마지막 구간을 삭제한다.")
    @Test
    void deleteLastLineSection() {

    }

    /**
     * <pre>
     * Given 지하철 노선의 구간을 생성하고
     * When 지하철 노선의 중간 구간을 삭제하면
     * Then 구간 삭제에 실패한다.
     * </pre>
     */
    @DisplayName("지하철 노선의 중간 구간을 삭제한다.")
    @Test
    void deleteMiddleLineSection() {

    }

    /**
     * <pre>
     * Given 지하철 노선의 구간을 생성하고
     * When 지하철 노선에 등록되어 있지 않는 지하철역을 삭제하면
     * Then 구간 삭제에 실패한다.
     * </pre>
     */

}
