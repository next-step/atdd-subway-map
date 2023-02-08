package subway.acceptanceSetting;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import subway.restAssured.LineRestAssured;
import subway.restAssured.StationRestAssured;

@ActiveProfiles("acceptance")
@Transactional(readOnly = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTestSetting {

    protected static final int LENGTH_TWO = 2;
    protected static final String SHINBUNDANG_LINE = "신분당선";
    protected static final String BUNDANG_LINE = "분당선";
    protected static final String ANOTHER_BUNDANG_LINE = "다른분당선";
    protected static final String RED = "bg-red-600";
    protected static final String GREEN = "bg-green-600";
    protected static final String YELLOW = "bg-yellow-600";
    protected static final long DISTANCE_TEN = 10L;
    protected static final long DISTANCE_FIFTEEN = 15L;
    protected static final String PATH_NAME = "name";
    protected static final String PATH_COLOR = "color";
    protected static final String PATH_LINE = "line";

    @LocalServerPort
    int port;

    private StationRestAssured stationRestAssured = new StationRestAssured();
    protected LineRestAssured lineRestAssured = new LineRestAssured();
    protected Long firstStationId;
    protected Long secondStationId;
    protected Long thirdStationId;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanUp.execute();

        firstStationId = stationRestAssured.save("지하철역");
        secondStationId = stationRestAssured.save("새로운지하철역");
        thirdStationId = stationRestAssured.save("또다른지하철역");
    }
}
