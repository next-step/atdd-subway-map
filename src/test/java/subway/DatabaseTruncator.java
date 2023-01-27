package subway;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Profile("test")
@Component
class DatabaseTruncator implements InitializingBean {

    @Autowired
    private EntityManager em;
    @Autowired
    private DataSource dataSource;
    private List<String> tableNames = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        final DatabaseMetaData dbmd = dataSource.getConnection().getMetaData();
        final ResultSet rs = dbmd.getTables(null, "PUBLIC", null, new String[]{"TABLE"});
        while (rs.next()) {
            tableNames.add(rs.getString("TABLE_NAME"));
        }
    }

    @Transactional
    public void execute() {
        try {
            em.flush();
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
            tableNames.forEach(tableName -> {
                em.createNativeQuery("TRUNCATE TABLE " + tableName + " RESTART IDENTITY").executeUpdate();
            });
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}