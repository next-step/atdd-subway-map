package subway.helper.db;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.Transactional;
import java.util.Set;

@Component
public class Truncator {
    @PersistenceContext
    private final EntityManager em;

    public Truncator(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void truncateAll() {
        Metamodel metamodel = em.getMetamodel();
        Set<EntityType<?>> entities = metamodel.getEntities();

        for (EntityType<?> entityType : entities) {
            String originalTableName = entityType.getName();
            String snakeCaseTableName = convertCamelCaseToSnakeCase(originalTableName);
            truncateTable(snakeCaseTableName);
        }
    }

    private String convertCamelCaseToSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    private void truncateTable(String tableName) {
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        Query truncateQuery = em.createNativeQuery("TRUNCATE TABLE " + tableName + " RESTART IDENTITY");
        truncateQuery.executeUpdate();

        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}