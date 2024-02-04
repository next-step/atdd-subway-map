package subway.fixture;

import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import java.util.Map;

public class DatabaseCleaner extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {

        final EntityManager em = getEm(testContext);

        ApplicationContext ac = testContext.getApplicationContext();

        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (EntityType<?> entity : em.getMetamodel().getEntities()) {
            String tableName = entity.getJavaType().getAnnotation(Table.class).name();
            em.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            em.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
            em.flush();
        }
    }

    private EntityManager getEm(TestContext testContext) {
        return testContext.getApplicationContext().getBean(EntityManager.class);
    }
}
