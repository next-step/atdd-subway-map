package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import subway.line.LineAcceptanceTest;
import subway.utils.DatabaseCleanser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanser databaseCleanser;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanser.execute();
    }
}
