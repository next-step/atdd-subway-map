package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.DataBaseClean;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.UNDEFINED_PORT;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql("/truncate.sql")
public class AbstractAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DataBaseClean dataBaseClean;

    @BeforeEach
    public void setUp() throws Exception {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
            dataBaseClean.afterPropertiesSet();
        }
        dataBaseClean.execute();
    }
}
