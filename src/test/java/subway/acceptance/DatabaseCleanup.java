package subway.acceptance;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import javax.sql.DataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleanup implements InitializingBean {

    public static final String SELECT_ALL_TABLES_QUERY = "show tables";
    private static final String[] CLEANUP_QUERIES = {
        "SET REFERENTIAL_INTEGRITY FALSE",
        "TRUNCATE TABLE %s",
        "ALTER TABLE %s ALTER COLUMN ID RESTART WITH 1",
        "SET REFERENTIAL_INTEGRITY TRUE"
    };

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    private List<String> truncateTableNames;

    private List<String> resetAutoIncrementTableNames;

    @Override
    public void afterPropertiesSet() {
        resetAutoIncrementTableNames = entityManager.getMetamodel().getEntities().stream()
            .filter(entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null)
            .map(EntityType::getName)
            .map(this::convertCamelToSnake)
            .collect(Collectors.toList());

        truncateTableNames = new JdbcTemplate(dataSource).query(SELECT_ALL_TABLES_QUERY, (rs, rowNum) -> rs.getString(1));
    }

    @Transactional
    public void clean() {
        entityManager.flush();
        entityManager.createNativeQuery(CLEANUP_QUERIES[0]).executeUpdate();

        truncateTableNames.stream().forEach(this::executeTruncate);
        resetAutoIncrementTableNames.stream().forEach(this::resetAutoIncrement);

        entityManager.createNativeQuery(CLEANUP_QUERIES[3]).executeUpdate();
    }

    private String convertCamelToSnake(String camelCase) {
        String snakeCase = camelCase.chars()
            .mapToObj(c -> Character.isUpperCase(c) ? "_" + Character.toLowerCase((char) c) : String.valueOf((char) c))
            .collect(Collectors.joining());
        return (snakeCase.charAt(0) == '_')? snakeCase.substring(1) : snakeCase;
    }

    private void executeTruncate(String name) {
        entityManager.createNativeQuery(String.format(CLEANUP_QUERIES[1], name)).executeUpdate();
    }

    private void resetAutoIncrement(String name) {
        entityManager.createNativeQuery(String.format(CLEANUP_QUERIES[2], name)).executeUpdate();
    }
}
