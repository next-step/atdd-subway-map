package subway.common;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("acceptance")
public abstract class AcceptanceTest {

    @Autowired
    DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setup() {
        databaseCleanup.execute();
    }
}
