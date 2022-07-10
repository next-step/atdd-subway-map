package nextstep.subway.testsupport;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {
    @LocalServerPort
    protected int port;
    @Autowired
    protected DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    public void setTest() {
        RestAssured.port = port;
        databaseCleanUp.execute();
    }
}
