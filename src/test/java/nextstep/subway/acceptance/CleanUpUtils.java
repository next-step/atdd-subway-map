package nextstep.subway.acceptance;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@Profile("test")
public class CleanUpUtils {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public void execute(String...tableNames) {
        entityManager.flush();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }
    }

}
