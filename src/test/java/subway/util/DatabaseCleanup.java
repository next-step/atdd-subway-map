package subway.util;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        this.tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(entity -> entity.getJavaType().getAnnotation(Entity.class) != null)
                .map(entity -> convertCamelToSnake(entity.getName()))
                .collect(Collectors.toUnmodifiableList());
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

    public static String convertCamelToSnake(String str) {
        String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";
        String value = "";
        value = str.replaceAll(regex, replacement).toUpperCase();
        return value;
    }
}
