package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class BaseTest {

    @Autowired
    private DataBaseCleaner dataBaseCleaner;

    @BeforeEach
    void setUp() {
        dataBaseCleaner.execute();
    }

    protected ExtractableResponse<Response> callCreateApi(final Object requestBody, final String path) {
        final ExtractableResponse<Response> response =
                given()
                    .log().all()
                    .body(requestBody)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post(path)
                .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .log().all()
                .extract();

        return response;
    }

}

