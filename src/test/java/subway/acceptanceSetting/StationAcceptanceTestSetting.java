package subway.acceptanceSetting;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import subway.restAssured.StationRestAssured;

@ActiveProfiles("acceptance")
@Transactional(readOnly = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTestSetting {

    protected static final String STATION_NAME = "지하철역이름";
    protected static final String NEW_STATION_NAME = "새로운지하철역이름";
    protected static final String ANOTHER_STATION_NAME = "또다른지하철역이름";
    protected static final String PATH_NAME = "name";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    protected StationRestAssured stationRestAssured = new StationRestAssured();

    @BeforeEach
    void setUp() {
        databaseCleanUp.execute();
        RestAssured.port = port;
    }
}
