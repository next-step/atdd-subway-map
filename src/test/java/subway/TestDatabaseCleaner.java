package subway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

/**
 * @see <a href="https://www.baeldung.com/spring-tests#7-state-management">참고</a>
 */
@TestComponent
public class TestDatabaseCleaner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void cleanUpStation() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "station");
        jdbcTemplate.update("ALTER TABLE station ALTER COLUMN id RESTART WITH 1");
    }

    public void cleanUpLine() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "subway_line");
        jdbcTemplate.update("ALTER TABLE subway_line ALTER COLUMN id RESTART WITH 1");
    }
}
