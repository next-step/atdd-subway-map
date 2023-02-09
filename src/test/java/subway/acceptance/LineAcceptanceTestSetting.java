package subway.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import subway.DatabaseCleanUp;


@ActiveProfiles("acceptance")
@Transactional
public class LineAcceptanceTestSetting {

    protected static final int LENGTH_TWO = 2;
    protected static final String SHINBUNDANG_LINE = "신분당선";
    protected static final String RED = "bg-red-600";
    protected static final long DISTANCE_TEN = 10L;
    protected static final String BUNDANG_LINE = "분당선";
    protected static final String GREEN = "bg-green-600";
    protected static final long DISTANCE_FIFTEEN = 15L;
    protected static final String ANOTHER_BUNDANG_LINE = "다른분당선";
    protected static final String YELLOW = "bg-yellow-600";

    @LocalServerPort
    int port;

    StationAcceptanceTest stationAcceptanceTest;
    protected Long firstStationId;
    protected Long secondStationId;
    protected Long thirdStationId;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanUp.execute();

        stationAcceptanceTest = new StationAcceptanceTest();
        firstStationId = stationAcceptanceTest.saveStation("지하철역");
        secondStationId = stationAcceptanceTest.saveStation("새로운지하철역");
        thirdStationId = stationAcceptanceTest.saveStation("또다른지하철역");
    }
}
