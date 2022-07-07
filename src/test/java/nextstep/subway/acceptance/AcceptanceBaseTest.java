package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceBaseTest {
    @PersistenceContext
    private EntityManager entityManager;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        cleanUpDatabase();
    }

    private void cleanUpDatabase() {
        entityManager.flush();

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE");

        entityManager.getMetamodel().getEntities().stream()
                .filter(entity -> entity.getJavaType().getAnnotation(Entity.class) != null)
                .map(entity -> entity.getName().toUpperCase())
                .forEach(entityName -> {
                    entityManager.createNativeQuery(
                            "TRUNCATE TABLE " + entityName
                    ).executeUpdate();
                    entityManager.createNativeQuery(
                            "ALTER TABLE " + entityName + " ALTER COLUMN ID RESTART WITH 1"
                    ).executeUpdate();
                });

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
