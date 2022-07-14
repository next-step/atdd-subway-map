package nextstep.subway.acceptance;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.sql.SQLException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @Autowired
    private Cleanup cleanup;

    @LocalServerPort
    int port;

    @AfterEach
    public void teardown() throws SQLException {
        this.cleanup.truncate();
    }

}
