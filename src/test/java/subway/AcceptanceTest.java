package subway;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.metamodel.Metamodel;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AcceptanceTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            Query referentialIntegrityFalse = entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE");
            referentialIntegrityFalse.executeUpdate();

            String truncateQuery = "TRUNCATE TABLE %s";
            Metamodel metamodel = entityManager.getMetamodel();
            List<Query> truncateQueries = metamodel.getEntities().stream()
                    .map(entity -> String.format(truncateQuery, entity.getName().toLowerCase()))
                    .map(query -> entityManager.createNativeQuery(query))
                    .collect(Collectors.toList());

            truncateQueries.forEach(Query::executeUpdate);

            Query referentialIntegrityTrue = entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE ");
            referentialIntegrityTrue.executeUpdate();

            transactionManager.commit(transaction);
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw e;
        }
    }
}
