package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void cleanUp() throws SQLException {
        var entityMangerFactory = (EntityManagerFactoryInfo) entityManager.getEntityManagerFactory();
        var dataSource = entityMangerFactory.getDataSource();
        var conn = dataSource.getConnection();
        var metaData = conn.getMetaData();
        var tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
        var statement = conn.createStatement();
        statement.execute("SET REFERENTIAL_INTEGRITY FALSE");
        while (tables.next()) {
            statement.execute("TRUNCATE TABLE " + tables.getString("TABLE_NAME"));
        }
        statement.execute("SET REFERENTIAL_INTEGRITY FALSE");
        statement.close();
        conn.close();
    }
}
