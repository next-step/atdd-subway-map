package subway;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AcceptanceTestBase {

    private DatabaseCleanup databaseCleanup;

    @Autowired
    public final void setDatabaseCleanup(DatabaseCleanup databaseCleanup) {
        this.databaseCleanup = databaseCleanup;
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.truncateAllTables();
    }
}
