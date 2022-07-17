package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.util.List;

public class AcceptanceTestExecutionListener extends AbstractTestExecutionListener {

	@Override
	public void beforeTestMethod(final TestContext testContext) {
		final Integer serverPort = testContext.getApplicationContext().getEnvironment().getProperty("local.server.port", Integer.class);
		if (serverPort == null) {
			throw new IllegalStateException("localServerPort cannot be null");
		}

		RestAssured.port = serverPort;
	}

	@Override
	public void afterTestMethod(final TestContext testContext) {
		final JdbcTemplate jdbcTemplate = getJdbcTemplate(testContext);
		final List<String> truncateQueries = getTruncateQueries(jdbcTemplate);
		truncateTables(jdbcTemplate, truncateQueries);
	}

	private List<String> getTruncateQueries(final JdbcTemplate jdbcTemplate) {
		return jdbcTemplate.queryForList("SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ';') AS q FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'", String.class);
	}

	private JdbcTemplate getJdbcTemplate(final TestContext testContext) {
		return testContext.getApplicationContext().getBean(JdbcTemplate.class);
	}

	private void truncateTables(final JdbcTemplate jdbcTemplate, final List<String> truncateQueries) {
		execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY FALSE");
		truncateQueries.forEach(v -> execute(jdbcTemplate, v));
		execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY TRUE");
	}

	private void execute(final JdbcTemplate jdbcTemplate, final String query) {
		jdbcTemplate.execute(query);
	}

}