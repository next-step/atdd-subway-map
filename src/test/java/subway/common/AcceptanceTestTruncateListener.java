package subway.common;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.util.List;

public class AcceptanceTestTruncateListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
        List<String> truncateQueries = jdbcTemplate.queryForList("SELECT TABLE_NAME " +
                "AS t FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'", String.class);

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        truncateQueries.forEach(tableName -> {
            jdbcTemplate.execute("truncate table " + tableName + ";");
            jdbcTemplate.execute("ALTER TABLE " + tableName + " ALTER COLUMN " + "ID RESTART WITH 1");
        });
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

    }
}
