package subway;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.util.List;

// ref1) https://mangkyu.tistory.com/264
// ref2) https://www.baeldung.com/spring-testexecutionlistener
public class AcceptanceTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestMethod(TestContext testContext) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(testContext);
        List<String> truncateQueries = getTruncateQueries(jdbcTemplate);
        List<String> initIdQueries = getInitIdQueries(jdbcTemplate);
        truncateTables(jdbcTemplate, truncateQueries, initIdQueries);
    }

    private List<String> getTruncateQueries(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForList("SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ';') AS q FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'", String.class);
    }

    private List<String> getInitIdQueries(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForList("SELECT Concat('ALTER TABLE ', TABLE_NAME, ' ALTER COLUMN ', 'ID RESTART WITH 1;') AS q FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'", String.class);
    }

    private JdbcTemplate getJdbcTemplate(TestContext testContext) {
        return testContext.getApplicationContext().getBean(JdbcTemplate.class);
    }

    private void truncateTables(JdbcTemplate jdbcTemplate, List<String> truncateQueries, List<String> initIdQueries) {
        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY FALSE");
        truncateQueries.forEach(q -> execute(jdbcTemplate, q));
        initIdQueries.forEach(q -> execute(jdbcTemplate, q));
        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY TRUE");
    }

    private void execute(JdbcTemplate jdbcTemplate, String query) {
        jdbcTemplate.execute(query);
    }
}
