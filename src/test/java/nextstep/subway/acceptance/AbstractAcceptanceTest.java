package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.subway.util.DataBaseClean;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql("/truncate.sql")
public class AbstractAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DataBaseClean dataBaseClean;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        dataBaseClean.execute();
    }
}
