package subway.common;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractTestDataBaseCleanUp {

    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    @AfterEach
    void tearDown() {
        dataBaseCleanUp.cleanUp();
    }
}
