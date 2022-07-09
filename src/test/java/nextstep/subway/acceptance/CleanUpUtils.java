package nextstep.subway.acceptance;

import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
@ActiveProfiles("test")
public class CleanUpUtils {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public void execute(List<String> tableNames) {
        entityManager.flush();
        tableNames.forEach(table -> {
            entityManager.createNativeQuery("TRUNCATE TABLE " + table).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + table + " ALTER COLUMN id RESTART WITH 1").executeUpdate();
        });
    }
}
