package nextstep.subway.acceptance;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile("test")
public class CleanUpSchema {

    private static final String CAMEL_REGEX = "([a-z])([A-Z]+)";
    private static final String TO_SNAKE_REPLACEMENT = "$1_$2";

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @PostConstruct
    public void initialize() {
        this.tableNames = entityManager.getMetamodel().getEntities().stream()
                .map(entityType -> camelToSnake(entityType.getName()))
                .collect(Collectors.toList());
    }

    private static String camelToSnake(String camel) {
        return camel.replaceAll(CAMEL_REGEX, TO_SNAKE_REPLACEMENT).toLowerCase();
    }

    @Transactional(readOnly = true)
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (String tableName : this.tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}
