package common;

import common.util.DatabaseCleanup;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "acceptanceTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({DatabaseCleanup.class})
public abstract class AbstractAcceptanceTest {
    @LocalServerPort
    protected int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    protected void beforeEach()  {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port;
        }

        databaseCleanup.execute();
    }
}
