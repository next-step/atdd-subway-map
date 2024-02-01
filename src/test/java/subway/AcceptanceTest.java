package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.StationCreateRequest;

import static io.restassured.RestAssured.UNDEFINED_PORT;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    protected void setUp() {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
        }
        databaseCleaner.clear();
    }

    protected ExtractableResponse<Response> createStation(StationCreateRequest request, int statusCode) {
        return post("/stations", request, statusCode);
    }

    protected ExtractableResponse<Response> createLine(LineCreateRequest request, int statusCode) {
        return post("/lines", request, statusCode);
    }

    protected ExtractableResponse<Response> get(String path, int statusCode, Object... pathParams) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(path, pathParams)
                .then().log().all()
                .statusCode(statusCode)
                .extract();
    }

    protected ExtractableResponse<Response> post(String path, Object body, int statusCode, Object... pathParams) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path, pathParams)
                .then().log().all()
                .statusCode(statusCode)
                .extract();
    }

    protected ExtractableResponse<Response> put(String path, Object body, int statusCode, Object... pathParams) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path, pathParams)
                .then().log().all()
                .statusCode(statusCode)
                .extract();
    }

    protected ExtractableResponse<Response> delete(String path, int statusCode, Object... pathParams) {
        return RestAssured.given().log().all()
                .when().delete(path, pathParams)
                .then().log().all()
                .statusCode(statusCode)
                .extract();
    }

}
