package subway;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AcceptanceTest {
    private static List<String> tableNames;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    void setTableNames() {
        if (tableNames == null) {
            tableNames = entityManager.getMetamodel().getEntities().stream()
                    .map(entityType -> entityType.getName().toLowerCase())
                    .collect(Collectors.toList());
        }
    }

    @BeforeEach
    void setUp() {
        setTableNames();
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            Query referentialIntegrityFalse = entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE");
            referentialIntegrityFalse.executeUpdate();

            String truncateQuery = "TRUNCATE TABLE %s";
            List<Query> truncateQueries = tableNames.stream()
                    .map(tableName -> String.format(truncateQuery, tableName))
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
