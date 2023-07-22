package subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class RestAssuredUtil {

    private final DatabaseCleanup databaseCleanup;

    public RestAssuredUtil(DatabaseCleanup databaseCleanup1) {
        this.databaseCleanup = databaseCleanup1;
    }

    public void initializePort(int port) {
        RestAssured.port = port;
    }

    public void cleanup() {
        databaseCleanup.afterPropertiesSet();
        databaseCleanup.clean();
    }

    public static ExtractableResponse<Response> findByIdWithOk(String url, Long id) {
        return RestAssured.given().log().all()
            .when()
            .get(url, id)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    public static ExtractableResponse<Response> findById(String url, Long id) {
        return RestAssured.given().log().all()
            .when()
            .get(url, id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> findAllWithOk(String url) {
        return RestAssured.given().log().all()
            .when()
            .get(url)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    public static ExtractableResponse<Response> createWithCreated(String url, Map<String, Object> param) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(param)
            .when()
            .post(url)
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
    }

    public static ExtractableResponse<Response> createWithBadRequest(String url, Map<String, Object> param) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(param)
            .when()
            .post(url)
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .extract();
    }

    public static ExtractableResponse<Response> updateWithOk(String url, Long id, Map<String, Object> param) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(param)
            .when()
            .put(url, id)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    public static ExtractableResponse<Response> deleteWithNoContent(String url, Long id) {
        return RestAssured.given().log().all()
            .when()
            .delete(url, id)
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .extract();
    }

    public static ExtractableResponse<Response> deleteWithBadRequest(String url, Long id) {
        return RestAssured.given().log().all()
            .when()
            .delete(url, id)
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .extract();
    }
    public static ExtractableResponse<Response> delete(String url, Long id) {
        return RestAssured.given().log().all()
            .when()
            .delete(url, id)
            .then().log().all()
            .extract();
    }
}
