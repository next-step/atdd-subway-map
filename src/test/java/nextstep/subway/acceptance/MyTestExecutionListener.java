package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(final TestContext testContext) {
        final Integer serverPort = testContext.getApplicationContext().getEnvironment().getProperty("local.server.port", Integer.class);
        if (serverPort == null) {
            throw new IllegalStateException("localServerPort cannot be null");
        }

        RestAssured.port = serverPort;
    }

    @Override
    public void afterTestExecution(final TestContext testContext) throws Exception {
        final Connection connection = getConnection(testContext);
        truncateTables(connection, getTruncateQueries(connection));
        connection.close();
    }

    private Connection getConnection(final TestContext testContext) throws SQLException {
        final DataSource dataSource = testContext.getApplicationContext().getBean(DataSource.class);
        return dataSource.getConnection();
    }

    private List<String> getTruncateQueries(final Connection connection) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ';') AS q FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'");
        final ResultSet resultSet = statement.executeQuery();
        final List<String> queryList = new ArrayList<>();
        while (resultSet.next()) {
            queryList.add(resultSet.getString("q"));
        }
        return queryList;
    }

    private void truncateTables(final Connection connection, final List<String> truncateQueries) {
        execute(connection, "SET REFERENTIAL_INTEGRITY FALSE");
        truncateQueries.forEach(v -> execute(connection, v));
        execute(connection, "SET REFERENTIAL_INTEGRITY TRUE");
    }

    private void execute(final Connection connection, final String v) {
        try {
            connection.prepareStatement(v).execute();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

}
