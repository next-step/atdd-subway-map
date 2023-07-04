package subway.util;

import com.google.common.base.CaseFormat;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseCleaner {

    @PersistenceContext
    private EntityManager entityManager;
    private List<String> tableNames;

    @PostConstruct
    public void init() {
        tableNames = entityManager.getMetamodel()
            .getEntities()
            .stream()
            .filter(e -> e.getJavaType().isAnnotationPresent(Entity.class))
            .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        tableNames.forEach(name -> {
            entityManager.createNativeQuery(String.format("TRUNCATE TABLE %s", name)).executeUpdate();
            entityManager.createNativeQuery(String.format("ALTER TABLE %s ALTER COLUMN ID RESTART WITH 1", name))
                .executeUpdate();
        });
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}
