package subway;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import subway.setup.DataBaseCleanUp;

public class BaseTest {

    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    @BeforeEach
    void setUp() {
        dataBaseCleanUp.execute();
    }
}
