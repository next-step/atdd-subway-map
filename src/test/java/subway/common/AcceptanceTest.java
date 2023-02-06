package subway.common;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTest {
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();
    }

    private static RequestSpecification getRestAssured(){
        return RestAssured.given().contentType(ContentType.JSON);
    }

    public static ExtractableResponse<Response> GET(String url) {
        return getRestAssured().when()
                .get(url)
                .then().log().all()
                .extract();

    }

    public static ExtractableResponse<Response> GET(String url, String pathKey, String pathParam) {
        return getRestAssured().when().pathParam(pathKey, pathParam)
                .get(url)
                .then().log().all()
                .extract();

    }

    public static ExtractableResponse<Response> POST(String url, Object bodyParam) {
        return getRestAssured().body(bodyParam)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> POST(String url, Object pathParam, Object bodyParam) {
        return getRestAssured().body(bodyParam)
                .when().post(url, pathParam)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> PUT(String url, Object pathParam, Object bodyParam){
        return getRestAssured().body(bodyParam)
                .when().put(url, pathParam)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> DELETE(String url, Object pathParam){
        return getRestAssured().when()
                .delete(url, pathParam)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> DELETE(String url, Object pathParam, String queryKey, Object queryParam){
        return getRestAssured().param(queryKey, queryParam)
                .when().delete(url, pathParam)
                .then().log().all()
                .extract();
    }
}
