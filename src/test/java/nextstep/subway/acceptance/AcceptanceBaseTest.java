package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Map;

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

    protected ExtractableResponse<Response> testRestApi(
            final HttpMethod method,
            final String url
    ) {
        return testRestApi(method, url, Collections.emptyMap());
    }

    protected ExtractableResponse<Response> testRestApi(
            final HttpMethod method,
            final String url,
            final Map<String, Object> body,
            final Object ...pathParams
    ) {
        final RequestSpecification request = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body);
        switch (method) {
            case GET:
                return request
                        .when().get(url, pathParams)
                        .then().extract();
            case PUT:
                return request
                        .when().put(url, pathParams)
                        .then().extract();
            case PATCH:
                return request
                        .when().patch(url, pathParams)
                        .then().extract();
            case DELETE:
                return request
                        .when().delete(url, pathParams)
                        .then().extract();
            case POST:
            default:
                return request
                        .when().post(url, pathParams)
                        .then().extract();
        }
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
