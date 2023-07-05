package config;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@TestComponent
public class DataBaseCleanUp {

    private static final String REVOCATION_OF_ASSOCIATION_INTEGRITY = "SET REFERENTIAL_INTEGRITY FALSE";
    private static final String RESET_REFERENTIAL_INTEGRITY = "SET REFERENTIAL_INTEGRITY TRUE";

    @PersistenceContext
    private EntityManager entityManager;
    private List<String> tableNames;

    @PostConstruct
    public void init() {

        tableNames = entityManager.getMetamodel().getEntities()
            .stream()
            .filter(entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null)
            .map(EntityType::getName)
            .map(this::convertCamelToSnake)
            .collect(Collectors.toList());
    }

    public String convertCamelToSnake(final String entityName) {

        Pattern pattern = Pattern.compile("([a-z])([A-Z])");
        Matcher matcher = pattern.matcher(entityName);

        String convert = matcher.replaceAll(matchResult -> String.format("%s_%s", matchResult.group(1), matchResult.group(2)));

        return convert.toLowerCase();
    }


    @Bean
    @Transactional
    public void clean() {

        entityManager.flush();
        entityManager.createNativeQuery(REVOCATION_OF_ASSOCIATION_INTEGRITY).executeUpdate();

        for (String table : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + table).executeUpdate();
            entityManager.createNativeQuery(
                "ALTER TABLE " + table + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
        }

        entityManager.createNativeQuery(RESET_REFERENTIAL_INTEGRITY).executeUpdate();

    }

}
