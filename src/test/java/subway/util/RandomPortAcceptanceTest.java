package subway.util;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@Configurable
@ActiveProfiles("acceptance")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RandomPortAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
        databaseCleanup.truncateTable();
    }
}
