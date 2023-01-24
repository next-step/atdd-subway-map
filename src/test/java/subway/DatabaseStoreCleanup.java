package subway;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@ActiveProfiles("local")
public class DatabaseStoreCleanup implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void afterPropertiesSet() {
    }

    @Transactional
    public void cleanStore() {
        entityManager.flush();
        entityManager.createNativeQuery("TRUNCATE TABLE station").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE station ALTER COLUMN ID RESTART WITH 1").executeUpdate();
    }
}
