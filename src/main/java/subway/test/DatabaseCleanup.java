package subway.test;

import com.google.common.base.CaseFormat;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile(value = "acceptanceTest")
public class DatabaseCleanup implements InitializingBean {

    @PersistenceContext
    private EntityManager em;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        em.unwrap(Session.class)
            .doWork(this::extractTableNames);
    }

    @Transactional
    public void execute() {
        em.unwrap(Session.class)
            .doWork(this::truncate);
    }

    private void truncate(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("SET REFERENTIAL_INTEGRITY FALSE");

        for (String tableName : tableNames) {
            statement.executeUpdate("TRUNCATE TABLE " + tableName);
            statement.executeUpdate("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1");
        }
    }

    private void extractTableNames(Connection conn) {
        tableNames = em.getMetamodel()
            .getEntities().stream()
            .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
            .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
            .collect(Collectors.toList());

//        List<String> tableNames = new ArrayList<>();
//
//        ResultSet resultSet = conn.getMetaData()
//            .getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"});
//
//        while (resultSet.next()) {
//            tableNames.add(resultSet.getString("table_name"));
//        }
//        this.tableNames = tableNames;
    }
}
