package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import subway.constants.Endpoint;
import subway.support.AcceptanceTest;
import subway.support.DatabaseCleanUp;
import subway.support.RestAssuredClient;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.UNDEFINED_PORT;

@DisplayName("지하철 노선 관련 기능")
@AcceptanceTest
public class LineAcceptanceTest {

    @LocalServerPort
    private int port;

    private static final String LINE_BASE_URL = Endpoint.LINE_BASE_URL.getUrl();

    private static final String LINE_ID_KEY = "id";

    private static final String LINE_NAME_KEY = "name";

    private Long gangnameStationId;

    private Long gwanggyoStationId;

    private Long cheongnyangniStationId;

    private Long chuncheonStation;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
        }
        databaseCleanUp.execute();

        this.gangnameStationId = saveStation("강남역");
        this.gwanggyoStationId = saveStation("광교역");
        this.cheongnyangniStationId = saveStation("청량리역");
        this.chuncheonStation = saveStation("춘천역");
    }

    /**
     * <pre>
     * When 지하철 노선을 생성하면
     * Then 지하철 노선이 생성된다
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     * </pre>
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
    }

    /**
     * <pre>
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     * </pre>
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void readLines() {
    }

    /**
     * <pre>
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     * </pre>
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void readLine() {
    }

    /**
     * <pre>
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     * </pre>
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
    }

    /**
     * <pre>
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     * </pre>
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
    }

    /**
     * <pre>
     * stationName 이라는 이름을 가진
     * 지하철역을 생성하는 API를 호출하고
     * 저장된 지하철역의 id를 반환하는 함수
     * </pre>
     *
     * @param stationName
     * @return saved station id
     */
    private Long saveStation(String stationName) {
        Map<String, String> station = Map.of("name", stationName);
        return RestAssuredClient.post(Endpoint.STATION_BASE_URL.getUrl(), station)
                .jsonPath()
                .getLong("id");
    }

    /**
     * <pre>
     * 지하철 노선을 생성하는 API를 호출하는 함수
     * </pre>
     *
     * @return ExtractableResponse
     */
    private ExtractableResponse<Response> saveStation() {
        return RestAssuredClient.post(LINE_BASE_URL, new HashMap<String, Object>());
    }

}
