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
                // when: https://google.com 로 요청을 보낼 때
                .when()
                    .get("https://google.com")
                // then: 200 응답 코드가 오는지 검증
                .then()
                    .statusCode(HttpStatus.OK.value());
    }
}
