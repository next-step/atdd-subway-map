package subway.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("acceptance")
@Component
public class DatabaseCleanup implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = new ArrayList<>();

        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        List<String> entityAnnotationTableNames = entities.stream()
                .filter(this::hasEntityAnnotation)
                .filter(entityType -> !hasTableAnnotation(entityType))
                .map(entityType -> convertCamelToSnake(entityType.getName()))
                .collect(Collectors.toUnmodifiableList());

        this.tableNames.addAll(entityAnnotationTableNames);

        List<String> tableAnnotationTableNames = entities.stream()
                .filter(this::hasEntityAnnotation)
                .filter(this::hasTableAnnotation)
                .map(entityType -> {
                    String tableName = entityType.getJavaType().getAnnotation(Table.class).name();
                    return tableName.isBlank() ? convertCamelToSnake(entityType.getName()) : tableName;
                })
                .collect(Collectors.toUnmodifiableList());

        this.tableNames.addAll(tableAnnotationTableNames);
    }

    private boolean hasEntityAnnotation(final EntityType<?> entityType) {
        return entityType.getJavaType().getAnnotation(Entity.class) != null;
    }

    private boolean hasTableAnnotation(final EntityType<?> entityType) {
        return entityType.getJavaType().getAnnotation(Table.class) != null;
    }

    public static String convertCamelToSnake(String str) {
        String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";
        String value = "";
        value = str.replaceAll(regex, replacement).toUpperCase();
        return value;
    }

    @Transactional
    public void truncateTable() {
        entityManager.flush();
        entityManager.clear();

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName + " RESTART IDENTITY").executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}
