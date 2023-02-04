package subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@Sql("/sql/truncate-tables.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AbstractAcceptanceTest {
    public static ValidatableResponse post(String path, Object request) {
        return given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request).
                when()
                    .post(path).
                then().log().all();
    }

    public ValidatableResponse get(String path) {
        return given().
                when()
                    .get(path).
                then().log().all();
    }

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    public String 리소스_경로_추출(ValidatableResponse response) {
        return response.extract().header("location");
    }

    public int 상태_코드_추출(ValidatableResponse response) {
        return response.extract().statusCode();
    }
}
