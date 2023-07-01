package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RestAssuredTest {

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        RestAssured
                .given()
                .when()
                    .get("http://www.google.com")
                .then()
                    .statusCode(200);
    }
}