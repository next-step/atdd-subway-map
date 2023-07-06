package config;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import subway.SubwayApplication;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes= {DataBaseCleanUp.class, SubwayApplication.class})
public abstract class AcceptanceTestConfig {

    @Autowired
    DataBaseCleanUp dataBaseCleanUp;

    @BeforeEach
    void setUp() {
        dataBaseCleanUp.clean();
    }
}
