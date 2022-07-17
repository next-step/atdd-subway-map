package nextstep.subway.acceptance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DatabaseCleanup {

    @Autowired
    private DataSource dataSource;

    public void truncate() throws SQLException {

        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement setChecks = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = ?");
            PreparedStatement getTables = connection.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema = SCHEMA()");

            try (ResultSet tableRes = getTables.executeQuery()) {
                setChecks.setBoolean(1, false);
                setChecks.executeUpdate();

                while(tableRes.next()) {
                    String table = tableRes.getString(1);
                    try(PreparedStatement truncateTable = connection.prepareStatement("TRUNCATE TABLE " + table + " RESTART IDENTITY")) {
                        truncateTable.executeUpdate();
                    }
                }
            } finally {
                setChecks.setBoolean(1, false);
                setChecks.executeUpdate();
            }
        }
    }
}
