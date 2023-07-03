package subway.acceptance;

import static io.restassured.RestAssured.when;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class RestAssuredTest {

    @BeforeEach
    void setup() {
        RestAssured.port = -1;
    }

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        // TODO: 구글 페이지 요청 구현
        when().
            get("https://google.com").
        then().
            statusCode(HttpStatus.OK.value());
    }
}