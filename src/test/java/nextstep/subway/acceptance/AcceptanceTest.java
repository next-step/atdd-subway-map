package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import java.sql.SQLException;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    public void teardown() throws SQLException {
        this.databaseCleanup.truncate();
    }

    protected static ExtractableResponse<Response> get(String url) {
        return RestAssured.given().log().all()
                .when().get(url)
                .then().log().all()
                .extract();
    }

    protected static ExtractableResponse<Response> post(String url, Map<String, Object> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    protected static ExtractableResponse<Response> put(String url, Map<String, Object> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(url)
                .then().log().all()
                .extract();
    }

    protected static ExtractableResponse<Response> delete(String url) {
        return RestAssured.given().log().all()
                .when().delete(url)
                .then().log().all()
                .extract();
    }

}
