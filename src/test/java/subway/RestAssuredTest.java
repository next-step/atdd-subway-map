package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestAssuredTest {

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        RestAssured
                .given()
                .log()
                .all()
                .when().get("https://google.com")
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
