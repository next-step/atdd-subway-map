package subway.acceptance;


import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import subway.exception.ApiException;
import subway.util.DatabaseCleaner;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleaner.execute();
    }

    public void 요청_실패됨(ExtractableResponse<Response> response, ApiException apiException) {
        assertThat(response.statusCode()).isEqualTo(apiException.getStatus().value());
        assertThat(response.jsonPath().getInt("status"))
            .isEqualTo(apiException.getStatus().value());
        assertThat(response.jsonPath().getString("message"))
            .isEqualTo(apiException.getMessage());
    }
}
