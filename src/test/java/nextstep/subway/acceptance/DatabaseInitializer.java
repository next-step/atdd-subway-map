package nextstep.subway.acceptance;

import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
@ActiveProfiles("test")
public class DatabaseInitializer implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        entityManager.unwrap(Session.class)
                .doWork(this::addTableNames);
    }

    private void addTableNames(Connection connection) throws SQLException {
        List<String> tableNames = new ArrayList<>();
        ResultSet tables = connection
                    .getMetaData()
                    .getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"});

        while (tables.next()) {
            tableNames.add(tables.getString("table_name"));
        }

        this.tableNames = tableNames;
    }

    public void execute() {
        entityManager.unwrap(Session.class)
                .doWork(this::initializeTables);
    }

    private void initializeTables(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("SET REFERENTIAL_INTEGRITY FALSE");
        for (String tableName : tableNames) {
            statement.executeUpdate("TRUNCATE TABLE " + tableName);
            statement.executeUpdate("ALTER TABLE " + tableName + " ALTER COLUMN " + tableName + "_ID RESTART WITH 1");
        }
        statement.executeUpdate("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
