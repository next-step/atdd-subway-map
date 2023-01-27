package subway;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AcceptanceTest {

    @Autowired
    private DatabaseStoreCleanup databaseStoreCleanup;

    @BeforeEach
    public void setUp() {
        databaseStoreCleanup.cleanStore();
    }
}
