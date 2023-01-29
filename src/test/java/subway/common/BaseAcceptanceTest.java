package subway.common;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

public abstract class BaseAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleaner cleaner;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        cleaner.afterPropertiesSet();
        cleaner.execute();
    }
}
